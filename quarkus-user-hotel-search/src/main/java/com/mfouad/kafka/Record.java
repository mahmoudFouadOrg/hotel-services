package com.mfouad.kafka;


import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
	
	private String id;
    private String hotel_id;
    private String command;
    private Long created_at;  // MicroTimestamp
    private String status;
    
    public static Record fromJson(JsonObject json) {
        Record record = new Record();
        record.setId(json.getString("id"));
        record.setHotel_id(json.getString("hotel_id"));
        record.setCommand(json.getString("command"));
        record.setCreated_at(json.getLong("created_at"));
        record.setStatus(json.getString("status"));
        return record;
    }

}
