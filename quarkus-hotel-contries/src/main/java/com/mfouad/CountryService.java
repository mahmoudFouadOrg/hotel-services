package com.mfouad;

import java.util.List;
import java.util.Optional;

import com.mfouad.exceptions.CountryNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CountryService {
	
	@Inject
	EntityManager entityManager;
	
	@Transactional()
	public void createCountry(CountryReq countryReq) {
        if (countryReq == null || countryReq.getName() == null || countryReq.getName().isEmpty()) {
            throw new IllegalArgumentException("Country name cannot be null or empty");
        }
        
        Country country = Country.builder().name(countryReq.getName()).active(true).build();
    	entityManager.persist(country);
    	entityManager.flush();
    }
	
	public Country getCountryNameById(Long id) {
		
		Country c= entityManager.find(Country.class, id);
		
		if(c==null)
			throw new CountryNotFoundException("Country with id " + id + " not found");
			
			return c;
				
	}
	
	@Transactional()
	public void activateDeactive(Long id) {
		
		Country c= entityManager.find(Country.class, id);
		
		if(c==null)
			throw new CountryNotFoundException("Country with id " + id + " not found");
		
		c.setActive(!c.isActive());
		entityManager.merge(c);
		entityManager.flush();
		
		
	}
	
	public List<Country> getAcitveCountries() {
		
		 return entityManager.createQuery("SELECT c FROM Country c where c.active =true", Country.class).getResultList();
		
	}
	
	

}
