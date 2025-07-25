package org.mfouad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mfouad.dto.HotelRes;

import static io.restassured.RestAssured.given;
//import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;


import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class HotelResourceTest {

	@BeforeEach
	void init() {
		// Initialize any required data or state before each test
	}

	@Test
	void createHotelWithInvalidData() {
		
		given().multiPart("data", "{\"name\":\"Test Hotel\",\"countryId\":1,\"pricePerNight\":100.0,\"stars\":4}")
				.multiPart("files", "image.jpg", "image content".getBytes(), "image/jpeg")
				.when().post("/").then()
				.statusCode(400);
		
		given().multiPart("data", "{\"name\":\"Test Hotel\",\"countryId\":1,\"pricePerNight\":100.0,\"stars\":10,\"rooms\":5}")
		.multiPart("files", "image.jpg", "image content".getBytes(), "image/jpeg")
		.when().post("/").then()
		.statusCode(400);
	}
	
	@Test
	void testRommCount() {
		
		given().multiPart("hotel", "{\"name\":\"Test Hotel\",\"address\":\"address 1\",\"countryId\":1,\"pricePerNight\":100.0,\"stars\":4,\"rooms\":3}","application/json")
		.when().post("/").then()
		.statusCode(200);
		
		HotelRes res=
		given().when().get("/?countryId=1")
		.then()
		.body("rooms", containsString("3"))
		.extract().as(HotelRes.class);
		
	}

	
}
