package com.mfouad.services;

import org.jboss.logging.Logger;

import com.mfouad.proto.Hotel;
import com.mfouad.proto.HotelAcDetivate;
import com.mfouad.proto.HotelManagmenterviceGrpc.HotelManagmenterviceImplBase;
import com.mfouad.proto.HotelStatus;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

@GrpcService
public class HotelManagmenterviceGrpcImpl extends HotelManagmenterviceImplBase{

	
	Logger log= Logger.getLogger(HotelManagmenterviceGrpcImpl.class);
	
	@Override
	public void addNewHotel(Hotel request, StreamObserver<HotelStatus> responseObserver) {
		// TODO Auto-generated method stub
		log.info("recive rquest "+request.getCountryId());
		responseObserver.onNext(HotelStatus.newBuilder().setStatus("created").build()); 
		
		super.addNewHotel(request, responseObserver);
	}
	
	
	@Override
	public void acDeActivate(HotelAcDetivate request, StreamObserver<HotelStatus> responseObserver) {
		// TODO Auto-generated method stub
		super.acDeActivate(request, responseObserver);
	}
	
	
}
