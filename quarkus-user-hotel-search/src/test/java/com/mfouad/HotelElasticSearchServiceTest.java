package com.mfouad;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.logging.Logger;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
//import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mfouad.proto.Hotel;
import com.mfouad.services.HotelElasticSearchService;

import io.quarkus.grpc.GrpcService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class HotelElasticSearchServiceTest {

	@Inject
	@GrpcService
	HotelElasticSearchService service;
	
	private static final Logger log =  Logger.getLogger("HotelElasticSearchServiceTest");

	@Inject
	RestClient restClient;

	@BeforeEach
	public void init() {

	}

	@Test
	public void convertProtoclBufToJson() {
		Hotel h = Hotel.newBuilder().setHotelId("113131313sf").setAddress("first address").setCountryId("1")
				.setDescription("coutnry").setPricePerNight(100).setHotelName("hotel name").build();
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

		Hotel h = Hotel.newBuilder().setHotelId("113131313sf").setAddress("first address").setCountryId("1")
				.setDescription("coutnry").setPricePerNight(100).setHotelName("hotel name").build();

		try {
			service.insertNewHotel(h);
		} catch (Exception e) {
			fail("can not post data  was expected but not thrown." + e.getMessage());
		}

	}

	@Test
	public void findHotels() {

		insertNewHotelTest();

		StringBuilder query = new StringBuilder("{");
		query.append("\"query\": {").append("\"bool\": {").append("\"must\": [")
				.append("{ \"term\": { \"countryId\": \"").append("1").append("\" } },")
				.append("{ \"match\": { \"hotelName\": \"").append("hotel").append("\" } }").append("]").append("}")
				.append("}").append("}");

		Request findHotelRequest = new Request("GET", "active_hotels/_search");
		findHotelRequest.setJsonEntity(query.toString());
		try {
			Response resp = restClient.performRequest(findHotelRequest);
			String responseBody = EntityUtils.toString(resp.getEntity());
			
			assertNotNull(responseBody);
			log.info(responseBody);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode node = objectMapper.readTree(responseBody);
			assertNotNull(node);
			assertNotNull(node.findValue("hits"));
			assertNotNull(node.findValue("hits").findValue("hits"));
			node.findValue("hits").findValue("hits").forEach(hit -> {
				assertNotNull(hit.get("_source"));
					try {
						Hotel.Builder hotelBuilder = Hotel.newBuilder();
						JsonFormat.parser().merge(hit.get("_source").toString(), hotelBuilder);
						Hotel hotel = hotelBuilder.build();
						assertNotNull(hotel);
						log.info("Found hotel: " + hotel.getHotelName());
					} catch (InvalidProtocolBufferException e) {
						fail("Failed to parse hotel data: " + e.getMessage());
					}
			});
			
		} catch (Exception e) {
			fail("can not find data  was expected but not thrown." + e.getMessage());
		}
	}
}
