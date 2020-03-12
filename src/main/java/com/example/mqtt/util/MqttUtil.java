package com.example.mqtt.util;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.MessageFormat;

/**
 * @Description
 * @Author chenduo
 * @Date 2020/3/10 10:38
 **/
public class MqttUtil {
	/**
	 * 连接mqtt
	 *
	 * @param broker
	 * @param clientId
	 * @param userName
	 * @param password
	 */
	public static MqttClient connect(String broker, String clientId, String userName, String password) {
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
			System.out.println("Connecting to broker:" + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			return sampleClient;
		} catch (MqttException me) {
			System.out.println("reason: " + me.getReasonCode());
			System.out.println("msg: " + me.getMessage());
			System.out.println("loc: " + me.getLocalizedMessage());
			System.out.println("cause: " + me.getCause());
			System.out.println("exception: " + me);
			me.printStackTrace();
		}
		return null;
	}

	/**
	 * 断开连接
	 *
	 * @param sampleClient
	 */
	public static void disconnect(MqttClient sampleClient) {
		try {
			sampleClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭客户端
	 *
	 * @param sampleClient
	 */
	public static void close(MqttClient sampleClient) {
		try {
			sampleClient.close();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 订阅topic
	 *
	 * @param sampleClient
	 * @param topic
	 */
	public static void subscribe(MqttClient sampleClient, String topic) {
		System.out.println("Subscribe to topic:" + topic);
		try {
			sampleClient.subscribe(topic);
			sampleClient.setCallback(new MqttCallback() {
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
	 * @param sampleClient
	 * @param content
	 * @param qos
	 * @param topic
	 */
	public static void publish(MqttClient sampleClient, String content, int qos, String topic) {
		try {
			System.out.println("Publishing message:" + content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			sampleClient.publish(topic, message);
			System.out.println("Message published");
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * QoS0 代表，Sender 发送的一条消息，Receiver 最多能收到一次，也就是说 Sender 尽力向 Receiver 发送消息，如果发送失败，也就算了；
	 * QoS1 代表，Sender 发送的一条消息，Receiver 至少能收到一次，也就是说 Sender 向 Receiver 发送消息，如果发送失败，会继续重试，直到 Receiver 收到消息为止，但是因为重传的原因，Receiver 有可能会收到重复的消息；
	 * QoS2 代表，Sender 发送的一条消息，Receiver 确保能收到而且只收到一次，也就是说 Sender 尽力向 Receiver 发送消息，如果发送失败，会继续重试，直到 Receiver 收到消息为止，同时保证 Receiver 不会因为消息重传而收到重复的消息
	 */

	/**
	 * 组装消息
	 *
	 * @param broker   断路器ip
	 * @param clientId 客户端id
	 * @param userName 用户名
	 * @param password 密码
	 * @param topic    订阅的topic
	 * @param qos      qos等级  QoS0，At most once，至多一次   QoS1，At least once，至少一次   QoS2，Exactly once，确保只有一次
	 * @param content  消息内容
	 */
	public static void assemble(String broker, String clientId, String userName, String password, String topic, int qos, String content) {
		//连接
		MqttClient client = connect(broker, clientId, userName, password);
		//订阅
		subscribe(client, topic);
		//发布消息
		publish(client,content,qos,topic);
		//断开连接
		disconnect(client);
		//关闭客户端
		close(client);


		//Use the memory persistence
//		MemoryPersistence persistence = new MemoryPersistence();
//
//		try {
//			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
//			MqttConnectOptions connOpts = new MqttConnectOptions();
//			connOpts.setCleanSession(true);
//			connOpts.setUserName(userName);
//			connOpts.setPassword(password.toCharArray());
//			System.out.println("Connecting to broker:" + broker);
//			sampleClient.connect(connOpts);
//			System.out.println("Connected");
//
//			//不订阅就没有回调方法执行
//			System.out.println("Subscribe to topic:" + topic);
//			sampleClient.subscribe(topic);
//			sampleClient.setCallback(new MqttCallback() {
//				@Override
//				public void messageArrived(String topic, MqttMessage message) throws Exception {
//					String theMsg = MessageFormat.format("{0} is arrived for topic {1}.", new String(message.getPayload()), topic);
//					System.out.println(theMsg);
//				}
//
//				@Override
//				public void deliveryComplete(IMqttDeliveryToken token) {
//				}
//
//				@Override
//				public void connectionLost(Throwable throwable) {
//				}
//			});
//
//
//			System.out.println("Publishing message:" + content);
//			MqttMessage message = new MqttMessage(content.getBytes());
//			message.setQos(qos);
//			sampleClient.publish(topic, message);
//			System.out.println("Message published");
//			//断开连接
//			sampleClient.disconnect();
//			//关闭客户端
//			sampleClient.close();
//
//		} catch (MqttException me) {
//			System.out.println("reason: " + me.getReasonCode());
//			System.out.println("msg: " + me.getMessage());
//			System.out.println("loc: " + me.getLocalizedMessage());
//			System.out.println("cause: " + me.getCause());
//			System.out.println("exception: " + me);
//			me.printStackTrace();
//		}
	}

	public static void main(String[] args) {
		String broker = "tcp://localhost:1883";
		String clientId = "JavaSample";
		String topic = "device/NXP-058659730253-963945118132721-22";
		String userName = "test";
		String password = "test";
		int qos = 0;
		String content = "Message from MqttPublishSample disconnect close";

		assemble(broker, clientId, userName, password, topic, qos, content);
	}
}
