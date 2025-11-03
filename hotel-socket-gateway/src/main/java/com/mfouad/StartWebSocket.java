package com.mfouad;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.mfouad.dto.UpdateHotelActivationDTO;
import com.mfouad.service.HotelApisServices;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/start-websocket/{name}")
@ApplicationScoped
public class StartWebSocket {
	
	Map<String, Session> actionSessions = new ConcurrentHashMap<String, Session>();
	
	@Inject
	ClientManager clientManager;
	
	@Inject
	@RestClient
	HotelApisServices hotelApisServices;

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) {
        System.out.println("onOpen> " + name);
    }

    @OnClose
    public void onClose(Session session, @PathParam("name") String name) {
        System.out.println("onClose> " + name);
        clientManager.removeClient(name);
    }

    @OnError
    public void onError(Session session, @PathParam("name") String name, Throwable throwable) {
        System.out.println("onError> " + name + ": " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("name") String name) {
        System.out.println("onMessage> " + name + ": " + message);
		String requestId = UUID.randomUUID().toString();
		clientManager.addClientRequest(name,  requestId
				, ClientRequests.builder().status("PENDING").RequestMessage(message)
				.requestDate(new java.util.Date()).build());
		try {
		hotelApisServices.uploadFile(new UpdateHotelActivationDTO(requestId, "sdfsdf", "1", false));
		}
		catch (Exception e) {
			e.printStackTrace();
			clientManager.removeClient(name);
		}
        
    }
}
