package com.mfouad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateHotelActivationDTO {
	
	String requestId;
	String userID;
	String countryID;
	boolean activate;

}
