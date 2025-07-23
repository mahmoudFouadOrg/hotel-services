package com.mfouad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRes {
	
	private String id;
	private String name;
	private String description;
	private double pricePerNight;
	private boolean active;
	private short stars; // Assuming this is a rating out of 5 stars
	private String address;
	private short rooms;

}
