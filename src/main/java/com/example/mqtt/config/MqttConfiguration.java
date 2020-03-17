package com.example.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author chenduo
 * @Date 2020/3/12 8:35
 **/
@Configuration
public class MqttConfiguration {
	@Value("${com.mqtt.broker}")
	private String broker;

	@Value("${com.mqtt.clientid}")
	private String clientId;

//	@Value("${com.mqtt.topic}")
//	private String topic;

	@Value("${com.mqtt.username}")
	private String username;

	@Value("${com.mqtt.password}")
	private String password;

	@Value("${com.mqtt.timeout}")
	private int timeout;

	@Value("${com.mqtt.keepalive}")
	private int keepalive;


	@Bean
	public MqttConnectOptions getMqttConnectOptions(){
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
		return connOpts;
	}

	@Bean
	public MqttClient assembleMqttClient(){
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);

			System.out.println("Connecting to broker:" + broker);
			sampleClient.connect(getMqttConnectOptions());
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

}
