package com.mfouad;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mfouad.proto.Hotel;
import com.mfouad.services.HotelElasticSearchService;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class HotelElasticSearchServiceTest {
	
	@Inject
	HotelElasticSearchService service;
	
	@BeforeEach
	public void init() {
		
	}
	
	@Test
	public void convertProtoclBufToJson() {
		Hotel h =Hotel.newBuilder().setHotelId("113131313sf")
				.setAddress("first address")
				.setCountryId("1")
				.setDescription("coutnry")
				.setPricePerNight(100)
				.build();
		 try {
				String jsonString = JsonFormat.printer().print(h);
			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				 fail("MyCustomException was expected but not thrown.");
			}
	}
	
	@Test
	public void insertNewHotelTest() {
		
		assertNotNull(service);
		
		Hotel h =Hotel.newBuilder().setHotelId("113131313sf")
				.setAddress("first address")
				.setCountryId("1")
				.setDescription("coutnry")
				.setPricePerNight(100)
				.build();
		
		try {
		service.insertNewHotel(h);
		}
		catch (Exception e) {
			 fail("can not post data  was expected but not thrown."+e.getMessage());
		}
		
		
	}

}
