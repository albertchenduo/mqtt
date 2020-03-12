package com.example.mqtt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author chenduo
 * @Date 2020/3/12 8:35
 **/
@Component
public class MqttConfiguration {

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
}
