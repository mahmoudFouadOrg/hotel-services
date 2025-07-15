package com.mfouad;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@QuarkusIntegrationTest
public class CountryServiceTest {
	
	@Inject
	EntityManager entityManager;
	
	@Inject
	CountryService countryService;
	
	@Test
    @org.junit.jupiter.api.Order(1)
    @Transactional
    void testCountriesInsertion() {
    	 entityManager.persist(Country.builder().name("Egypt").build());
    	 entityManager.flush();
    	 CriteriaQuery<Country> cr = entityManager.getCriteriaBuilder().createQuery(Country.class);
    	 cr.from(Country.class);
    	 
    	 
    	 	List<Country>data= entityManager.createQuery(cr).getResultList();
    	 	assertThat(data.size()).isGreaterThan(0);
    	
    }
    
    
    @Test
    void testCountryCreationEndPoint(){
    	countryService.createCountry(CountryReq.builder().name("Egypt").build());
    	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    	 CriteriaQuery<Country> cr = builder.createQuery(Country.class);
    	Root<Country> root = cr.from(Country.class);
    	
    	cr.select(root).where(builder.equal(root.get("name"), "egypt"));
    	 
    	 
    	 	List<Country>data= entityManager.createQuery(cr).getResultList();
    	 	assertThat(data.size()).isGreaterThan(0);
    }
    

}
