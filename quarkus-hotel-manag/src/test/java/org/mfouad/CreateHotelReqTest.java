package org.mfouad;

import org.junit.jupiter.api.Test;
import org.mfouad.entities.HotelEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfouad.dto.HotelJsonReq;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
public class CreateHotelReqTest extends TestCases{
	
	 private ObjectMapper mapper = new ObjectMapper();
	 
	 
	@Test
	@Transactional
	public void testCreateHotelReq() {
		
		String json = "{\"name\":\"Test Hotel\",\"countryId\":1,\"pricePerNight\":100.0,\"stars\":4,\"address\":\"123 Test St\",\"rooms\":5}";
		
	        try {
				HotelJsonReq req = mapper.readValue(json, HotelJsonReq.class);
				assert req.getName().equals("Test Hotel");
				assert req.getRooms() == 5;
				
				HotelEntity hotelEntity = HotelEntity.builder().active(req.isActive())
						.address(req.getAddress()).countryId(req.getCountryId()).description(req.getDescription())
						.name(req.getName()).pricePerNight(req.getPricePerNight()).stars(req.getStars())
						.rooms(req.getRooms())
						.build();
				
				assert hotelEntity.getRooms() == 5;
				
				
				HotelEntity.persist(hotelEntity);
				
				assert hotelEntity.isPersistent();
				
				HotelEntity db =HotelEntity.findAll().firstResult();
				assert db.getRooms() == 5;
				
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	

}
