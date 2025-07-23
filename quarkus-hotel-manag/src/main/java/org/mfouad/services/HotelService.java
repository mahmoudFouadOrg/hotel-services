package org.mfouad.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mfouad.entities.HotelEntity;

import com.mfouad.dto.CreateHotelReq;
import com.mfouad.dto.HotelJsonReq;
import com.mfouad.dto.HotelRes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class HotelService {
	
	
	@Transactional
	public void createNewHotel(HotelJsonReq req) {

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
	
	public List<HotelRes>  findHotels(Long countryId, String name) {
		List<HotelEntity> hotels =new ArrayList<HotelEntity>();
		 if (countryId != null && (name == null || name.isEmpty())) {
			hotels= HotelEntity.find( "countryId = ?1", countryId).list();
		} else if (countryId == null && name != null && !name.isEmpty()) {
			hotels= HotelEntity.find( "name like  ?1", "%"+name+"%").list();
		} else {
			hotels= HotelEntity.find( "countryId = ?1 and name like  ?2", countryId,"%"+name+"%").list();
		}
		 
		 List<HotelRes> res =hotels.stream().map(hotel -> HotelRes.builder()
				 .id(hotel.getId())
				 .name(hotel.getName())
				 .description(hotel.getDescription())
				 .pricePerNight(hotel.getPricePerNight())
				 .active(hotel.isActive())
				 .stars(hotel.getStars())
				 .address(hotel.getAddress())
				 .build()).collect(Collectors.toList());
		 
		 return res;
	}

}
