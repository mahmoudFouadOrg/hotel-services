package com.mfouad.services;

import java.io.IOException;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;

import com.google.protobuf.util.JsonFormat;
import com.mfouad.proto.Hotel;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class HotelElasticSearchService {
	
	 @Inject
	   RestClient restClient;
	 
	 public void insertNewHotel(Hotel hotel) throws IOException  {
		 
		
		 Request request = new Request(
	                "POST",
	                "/active_hotels/_doc/" + hotel.getHotelId()); 
		 request.setJsonEntity(JsonFormat.printer().print(hotel));
		 restClient.performRequest(request);
		 
		 
		
	 }

}
