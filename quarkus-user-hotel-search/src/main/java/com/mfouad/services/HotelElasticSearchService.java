package com.mfouad.services;

import java.io.IOException;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mfouad.proto.Hotel;
import com.mfouad.proto.HotelSearchServiceGrpc.HotelSearchServiceImplBase;
import com.mfouad.proto.HotlesResponse;
import com.mfouad.proto.HotlesResponse.Builder;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.inject.Inject;

@GrpcService
public class HotelElasticSearchService extends HotelSearchServiceImplBase {
	
	 @Inject
	   RestClient restClient;
	 
	 Logger log= Logger.getLogger(HotelManagmenterviceGrpcImpl.class);
	 
	 public void insertNewHotel(Hotel hotel) throws IOException  {
		 
		
		 Request request = new Request(
	                "POST",
	                "/active_hotels/_doc/" + hotel.getHotelId()); 
		 request.setJsonEntity(JsonFormat.printer().print(hotel));
		 restClient.performRequest(request);
		 
		 
		
	 }
	 
	@Override
	public void findHotel(Hotel request, StreamObserver<HotlesResponse> responseObserver)  {
		// TODO Auto-generated method stub
		StringBuilder query = new StringBuilder("{");
		query.append("\"query\": {").append("\"bool\": {").append("\"must\": [")
				.append("{ \"term\": { \"countryId\": \"").append("1").append("\" } },")
				.append("{ \"match\": { \"hotelName\": \"").append("hotel").append("\" } }").append("]").append("}")
				.append("}").append("}");

		Request findHotelRequest = new Request("GET", "active_hotels/_search");
		findHotelRequest.setJsonEntity(query.toString());
		
		try {
			Builder hotelResponseBuilder = HotlesResponse.newBuilder();
			Response resp = restClient.performRequest(findHotelRequest);
			
			
			 if (resp.getStatusLine().getStatusCode() != 200) {
			        String errorBody = EntityUtils.toString(resp.getEntity());
			        throw new RuntimeException("Elasticsearch request failed: " + 
			            resp.getStatusLine() + " - " + errorBody);
			    }
			
			String responseBody = EntityUtils.toString(resp.getEntity());
			  log.info("Elasticsearch response: "+ responseBody);
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			
			JsonNode hitsNode = rootNode.path("hits").path("hits");
		    if (!hitsNode.isArray()) {
		        throw new RuntimeException("Invalid Elasticsearch response format: missing hits array");
		    }

			
			
		    hitsNode.forEach(hit -> {
					try {
						Hotel.Builder hotelBuilder = Hotel.newBuilder();
						JsonFormat.parser().merge(hit.get("_source").toString(), hotelBuilder);
						Hotel hotel = hotelBuilder.build();
						log.info("Found hotel: " + hotel.getHotelName());
						hotelResponseBuilder.addHotels(hotel);
					} catch (InvalidProtocolBufferException e) {
						e.printStackTrace();
						 log.error("Failed to parse hotel from Elasticsearch hit: {}", hit, e);
					}
		
});
		    responseObserver.onNext(hotelResponseBuilder.build());
		    responseObserver.onCompleted();
			
		} catch (IOException e) {
		    log.error("Elasticsearch communication failed", e);
		    responseObserver.onError(Status.INTERNAL
		        .withDescription("Failed to communicate with search service")
		        .withCause(e)
		        .asRuntimeException());
		} catch (RuntimeException e) {
		    log.error("Search processing failed", e);
		    responseObserver.onError(Status.INTERNAL
		        .withDescription("Search processing failed: " + e.getMessage())
		        .withCause(e)
		        .asRuntimeException());
		}
		
//		super.findHotel(request, responseObserver);
	}
	 
	 

}
