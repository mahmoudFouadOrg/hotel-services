package org.mfouad.entities;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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

@NamedQueries({
    @NamedQuery(name = "hotel.getImages", query = "select e.images from HotelEntity e where e.id = ?1"),
})

public class HotelEntity  extends PanacheEntityBase{
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuild", strategy = "org.mfouad.entities.UuidGenerator")
	private String id;
	private String name;
	private Long countryId; // Assuming this is a foreign key to a Country entity;
	private String description;
	private double pricePerNight;
	private boolean active;
	private short stars; // Assuming this is a rating out of 5 stars
	private String address;
	private short rooms;

	@OneToMany(fetch = FetchType.LAZY,cascade = jakarta.persistence.CascadeType.ALL)
	private List<HotelImage> images;


	
	public static List<HotelImage> getHotelImages(String id){
		return find("#hotel.getImages", id).list();
	}
	
}
