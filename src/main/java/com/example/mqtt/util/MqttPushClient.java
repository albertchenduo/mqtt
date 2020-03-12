package com.example.mqtt.util;

import com.example.mqtt.config.PushCallback;
import com.example.mqtt.entity.PushPayLoad;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Description
 * @Author chenduo
 * @Date 2020/3/12 8:45
 **/
@Slf4j
public class MqttPushClient {

	@Value("${com.mqtt.host}")
	private String host;

	@Value("${com.mqtt.clientid}")
	private String clientid;

	@Value("${com.mqtt.topic}")
	private String topic;

	@Value("${com.mqtt.username}")
	private String username;

	@Value("${com.mqtt.password}")
	private String password;

	@Value("${com.mqtt.timeout}")
	private int timeout;

	@Value("${com.mqtt.keepalive}")
	private int keepalive;

	private MqttClient client;
	//多线程可见
	private static volatile MqttPushClient mqttPushClient = null;

	public static MqttPushClient getInstance(){

		if(null == mqttPushClient){
			synchronized (MqttPushClient.class){
				//防止多线程 A线程不为null B线程置为null的情况
				if(null == mqttPushClient){
					mqttPushClient = new MqttPushClient();
				}
			}

		}
		return mqttPushClient;

	}

	private MqttPushClient() {
		connect();
	}


	private void connect(){
		try {
			client = new MqttClient(host, clientid, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setUserName(username);
			options.setPassword(password.toCharArray());
			options.setConnectionTimeout(timeout);
			options.setKeepAliveInterval(keepalive);
			try {
				client.setCallback(new PushCallback());
				client.connect(options);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发布，默认qos为0，非持久化
	 * @param topic
	 * @param pushMessage
	 */
	public void publish(String topic,PushPayLoad pushMessage){
		publish(0, false, topic, pushMessage);
	}

	/**
	 * 发布
	 * @param qos
	 * @param retained
	 * @param topic
	 * @param pushMessage
	 */
	public void publish(int qos,boolean retained,String topic,PushPayLoad pushMessage){
		MqttMessage message = new MqttMessage();
		message.setQos(qos);
		message.setRetained(retained);
		message.setPayload(pushMessage.toString().getBytes());
		MqttTopic mTopic = client.getTopic(topic);
		if(null == mTopic){
			log.error("topic not exist");
		}
		MqttDeliveryToken token;
		try {
			token = mTopic.publish(message);
			token.waitForCompletion();
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 订阅某个主题，qos默认为0
	 * @param topic
	 */
	public void subscribe(String topic){
		subscribe(topic,0);
	}

	/**
	 * 订阅某个主题
	 * @param topic
	 * @param qos
	 */
	public void subscribe(String topic,int qos){
		try {
			client.subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		String kdTopic = "good";
		PushPayLoad pushMessage = PushPayLoad.getPushPayloadBuider().setMobile("15345715326")
				.setContent("designModel")
				.bulid();
		MqttPushClient.getInstance().publish(0, false, kdTopic, pushMessage);
	}
}
