package com.mfouad.gateway;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelJson  implements Serializable {
	
	
	private String hotelName;
	private String hotel_id;
	String countryId ;
	String address ;
	String description ;
	float pricePerNight ;
	short stars ;
	short rooms =7;

}
