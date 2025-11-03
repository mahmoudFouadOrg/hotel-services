package com.mfouad;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ClientManager {
	
	
	private ConcurrentHashMap<String, Map<String,ClientRequests>> activeClientRequests;
	
	
	@PostConstruct
	private void init() {
		activeClientRequests = new ConcurrentHashMap<>();
	}
	
	
	public void addClientRequest(String clientName,String requestId, ClientRequests clientRequests) {
		
		if(clientExists(clientName)) {
			
			activeClientRequests.get(clientName).put(requestId, clientRequests);
		}
		else {
			
		Map<String,ClientRequests> newRequest=	new ConcurrentHashMap<String, ClientRequests>();
		newRequest.put(requestId, clientRequests);
			
			activeClientRequests.put(clientName, newRequest);
			
			 System.out.println("add new request for " + clientName + ": " + clientRequests.getRequestDate() +" meesage : "+clientRequests.getRequestMessage());
		}
		
    }
	
	public void updateClientRequest(String clientName, String requestId,String status,ClientRequests clientRequests) {
		
		if(clientExists(clientName)) {
			if (activeClientRequests.get(clientName).containsKey(requestId)) {
				ClientRequests clientExistRequest = activeClientRequests.get(clientName).get(requestId);
				
			}
		}
		
		
	}
	
	public boolean clientExists(String clientName) {
		
		activeClientRequests.size();
		activeClientRequests.keySet().forEach(key -> {
			System.out.println("existing client key : " + key);
		});
		return activeClientRequests.containsKey(clientName);
	}
	
	public void removeClient(String clientName) {
		        activeClientRequests.remove(clientName);
	}

}
