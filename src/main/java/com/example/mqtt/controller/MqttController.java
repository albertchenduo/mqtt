package com.example.mqtt.controller;

import com.example.mqtt.entity.PayLoad;
import com.example.mqtt.util.MqttUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqtt")
public class MqttController {
	@Autowired
	private MqttUtil mqttUtil;


	@PostMapping("/send")
	public void send(@RequestBody PayLoad payload){

		mqttUtil.send(payload.getTopic(),payload.getQos(),payload.getConctent());
	}
}
