package com.mfouad.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.mfouad.kafka.HotelOutboxEvent;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HotelStatusConusmer {

	 @Incoming("hotel-outbox")
		public void consume(ConsumerRecord<String, JsonObject> kafakRecord) {
			System.out.println("Received message: " + kafakRecord.value());
			 String key = kafakRecord.key(); // Can be `null` if the incoming record has no key
			 JsonObject value = kafakRecord.value(); // Can be `null` if the incoming record has no value
			    String topic = kafakRecord.topic();
			    int partition = kafakRecord.partition();
			    try {
			    	com.mfouad.kafka.Record rcored = com.mfouad.kafka.Record.fromJson(value.getJsonObject("payload").getJsonObject("after"));
			    	System.out.println("revice message for hotel id : " + rcored.getHotel_id()+" message id: "+key);
					System.out.println("revice message to apply command : " + rcored.getCommand());
			    }
			    catch (Exception e) {
					e.printStackTrace();
				}
			    
	 }

}
