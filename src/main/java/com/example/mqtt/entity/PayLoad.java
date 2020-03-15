package com.example.mqtt.entity;

import lombok.Data;

@Data
public class PayLoad {

	private String topic;

	private Integer qos;

	private String conctent;
}
