package org.mfouad.services;

import com.mfouad.dto.CreateHotelReq;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class HotelService {
	
	
	
	public void createNewHotel(CreateHotelReq req) {

		if (req == null || req.getName() == null || req.getName().isEmpty()) {
			throw new IllegalArgumentException("Hotel name cannot be null or empty");
		}

		if (req.getCountryId() == null) {
			throw new IllegalArgumentException("Country ID cannot be null");
		}

		if (req.getPricePerNight() <= 0) {
			throw new IllegalArgumentException("Price per night must be greater than zero");
		}

		if (req.getStars() < 1 || req.getStars() > 5) {
			throw new IllegalArgumentException("Stars must be between 1 and 5");
		}

		// Assuming a Hotel entity exists and is managed by an EntityManager
		// You would typically persist the hotel entity here
		
	}

}
