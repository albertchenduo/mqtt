package com.example.mqtt.util;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
@Component
public class MqttUtil {


	private MqttClient mqttClient;

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}


	public MqttUtil(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	/**
	 * 订阅topic
	 * @param topic
	 */
	private void subscribe(String topic){
		System.out.println("Subscribe to topic:" + topic);
		try {
			mqttClient.subscribe(topic);
			mqttClient.setCallback(new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					String theMsg = MessageFormat.format("{0} is arrived for topic {1}.", new String(message.getPayload()), topic);
					System.out.println(theMsg);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}

				@Override
				public void connectionLost(Throwable throwable) {
				}
			});
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发布消息
	 * @param content
	 * @param qos
	 * @param topic
	 */
	private void publish(String content, int qos, String topic) {
		try {
			System.out.println("Publishing message:" + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			mqttClient.publish(topic, message);
			System.out.println("Message published");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭客户端
	 */
	private void close() {
		try {
			mqttClient.close();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开连接
	 */
	private void disconnect() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送
	 * @param topic
	 * @param qos
	 * @param content
	 */
	public void send(String topic,int qos,String content){
		//订阅
		subscribe(topic);
		//发布消息
		publish(content,qos,topic);
		//断开连接
//		disconnect();
		//关闭客户端
//		close();
	}
}
