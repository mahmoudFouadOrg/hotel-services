package com.mfouad.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateHotelReq {
	
	@NotBlank(message = "name may not be blank")
	private String name;
	
	private Long countryId; // Assuming this is a foreign key to a Country entity;
	private String description;
	private double pricePerNight;
	private boolean active;
	@Min(value = 1, message = "stars must be at least 1")
	@Max(value = 5, message = "stars must be at most 5")
	private short stars; // Assuming this is a rating out of 5 stars
	
	@NotBlank(message = "address may not be blank")
	private String address;


	@Min(value = 3, message = "rooms must be at least 1")
	private short rooms;
	
	
}
