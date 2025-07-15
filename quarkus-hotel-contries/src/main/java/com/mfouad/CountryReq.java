package com.mfouad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@XmlRootElement

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CountryReq {
	
	String name;

}
