package org.mfouad.entities;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelEntity  extends PanacheEntityBase{
	
	@Id
	private String id;
	private String name;
	private Long countryId; // Assuming this is a foreign key to a Country entity;
	private String description;
	private double pricePerNight;
	private boolean active;
	private short stars; // Assuming this is a rating out of 5 stars
	private String address;
	private short rooms;

	@OneToMany()
	private List<HotelImage> images;

	// You can add methods or additional logic if needed
	// For example, a method to get the full address of the hotel

}
