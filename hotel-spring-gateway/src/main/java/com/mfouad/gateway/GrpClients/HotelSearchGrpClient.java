package com.mfouad.gateway.GrpClients;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.mfouad.gateway.filters.HotelJson;
import com.mfouad.proto.Hotel;
import com.mfouad.proto.HotelSearchServiceGrpc;
import com.mfouad.proto.HotlesResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/hotel-search")
public class HotelSearchGrpClient {
	
	
	@GrpcClient("grpc-server")
	HotelSearchServiceGrpc.HotelSearchServiceStub hotelSearchServiceFutureStub;
	
	
	@PostMapping("/find")
		public Flux<String> fintHotels(@RequestBody HotelJson hotelJson ){
			
			
			
			 return Flux.create(emitter -> {
			        hotelSearchServiceFutureStub.findHotel(
			            Hotel.newBuilder()
			                .setHotelName(hotelJson.getHotelName())
			                .setCountryId(hotelJson.getCountryId())
			                .build(),
			            new StreamObserver<HotlesResponse>() {
			                @Override
			                public void onNext(HotlesResponse value) {
			                	String jsonResponse;
								try {
									jsonResponse = JsonFormat.printer()
									        .print(value);
									emitter.next(jsonResponse);
								} catch (InvalidProtocolBufferException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									emitter.error(new ResponseStatusException(
				                            HttpStatus.INTERNAL_SERVER_ERROR,
				                            "Failed to convert protobuf to JSON",
				                            e));
									
								}
			                }

			                @Override
			                public void onError(Throwable t) {
			                    emitter.error(t);
			                }

			                @Override
			                public void onCompleted() {
			                    emitter.complete();
			                }
			            });
			    });
	
		}
	

}
