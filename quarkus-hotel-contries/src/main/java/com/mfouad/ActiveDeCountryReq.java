package com.mfouad;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActiveDeCountryReq {
	
	
	String requestId;
	String userID;
	String countryID;
	boolean activate;

}
