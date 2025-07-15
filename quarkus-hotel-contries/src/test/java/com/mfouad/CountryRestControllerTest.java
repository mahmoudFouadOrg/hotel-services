package com.mfouad;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@QuarkusTest
public class CountryRestControllerTest {
	
	@Inject
	EntityManager en;
	
	@BeforeEach
	@Transactional
	void init() {
		en.persist( Country.builder().name("egypt").build());
		
	}
	    
	    @Test
	    void testCountriesEndpoint() {
	        given()
	          .when().get("/countries")
	          .then()
	             .statusCode(200);
	    }
	    
	    @Test
	    void testgetCountryNameEndpoint() {
	    	
	    	Country c = en.find(Country.class, 1);
	    	
	    	Country res=
	    	given()
	    	.when().get("/countries/1")
	    	.then()
	    	.statusCode(200)
	    	.body("name", containsString(c.getName())
	    			)
	    	.extract().as(Country.class)
	    	;
	    	
	    	assertThat(res.isActive()).isEqualTo(c.isActive());
	    	
	    	given()
	    	.when().get("/countries/2")
	    	.then()
	    	.statusCode(404);
	    }
	
}
