package org.mfouad.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.mfouad.entities.HotelEntity;
import org.mfouad.entities.HotelImage;

import com.mfouad.dto.CreateHotelReq;
import com.mfouad.dto.CreateHotelReq.HotelFilePart;
import com.mfouad.dto.HotelImageRes;
import com.mfouad.dto.HotelJsonReq;
import com.mfouad.dto.HotelRes;
import com.mfouad.dto.UploadFileReq;
import com.mfouad.dto.UploadFileRes;
import com.mfouad.proto.Hotel;
import com.mfouad.proto.HotelManagmenterviceClient;
import com.mfouad.proto.HotelManagmenterviceGrpc.HotelManagmenterviceBlockingStub;

import io.quarkus.grpc.GrpcClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class HotelService {
	
	@Inject
	@RestClient
	FileMangeClientService service;
	
	@GrpcClient("hotel-management-grpc")
	HotelManagmenterviceBlockingStub hotelManagmenterviceClient;
	
	
	   private static final Logger log =  Logger.getLogger(HotelService.class);
	
	
	@Transactional
	public void createNewHotel(CreateHotelReq request) {
		HotelJsonReq req = request.getHotelJsonReq();
		
		List<UploadFileRes> files = new ArrayList<>();

		
		  if (request.getFiles() != null && !request.getFiles().isEmpty()) {
              for (HotelFilePart filePart : request.getFiles()  ) {
                  // Process each file, e.g., save to disk or DB
//                  System.out.println("Received file: " + filePart.getFileName());
                  try {
                	  files.add(service.uploadFile(new UploadFileReq(getBytesFromInputStream(filePart.getFile()), filePart.getFileName())));
				} catch (IOException e) {
					e.printStackTrace();
				}
                  
              }
          }

		HotelEntity hotel = HotelEntity.builder().active(req.isActive())
				.address(req.getAddress()).countryId(req.getCountryId()).description(req.getDescription())
				.name(req.getName()).pricePerNight(req.getPricePerNight()).stars(req.getStars())
				.rooms(req.getRooms())
				.images(files.stream().map(f -> HotelImage.builder().name(f.getFileName()) .path(f.getUrl()).build()).collect(Collectors.toList()))
				.build();
		
		HotelEntity.persist(hotel);
		
		Hotel gropHotel = Hotel.newBuilder().setHotelId(hotel.getId())
				.setHotelName(hotel.getName())
				.setDescription(hotel.getDescription()).setPricePerNight((float)hotel.getPricePerNight())
				.setStars(hotel.getStars()).setRooms(hotel.getRooms())
				.setAddress(hotel.getAddress()).setCountryId(hotel.getCountryId().toString()).build();
		hotelManagmenterviceClient.addNewHotel(gropHotel);
		
		
	}
	
	  public static byte[] getBytesFromInputStream(InputStream input) throws IOException {
	        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	        byte[] data = new byte[4096]; // 4KB buffer
	        int bytesRead;

	        while ((bytesRead = input.read(data, 0, data.length)) != -1) {
	            buffer.write(data, 0, bytesRead);
	        }

	        return buffer.toByteArray();
	    }
	
	public List<HotelRes>  findHotels(Long countryId, String name) {
		
		log.info("get hotels by countryId: " + countryId + " and name: " + name);
		
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
				 .rooms(hotel.getRooms())
				 .address(hotel.getAddress())
				 .build()).collect(Collectors.toList());
		 
		 log.info("found hotels: " + res.size());
		 return res;
	}
	
	public List<HotelImageRes> getHotelImages(String hotelId){
		List<HotelImage> data = HotelEntity.getHotelImages(hotelId);
		
		return data.stream().map(entity -> HotelImageRes.builder()
				.id(entity.getId())
				.name(entity.getName())
				.path(entity.getPath())
				.build()
				).collect(Collectors.toList());
		
	}

}
