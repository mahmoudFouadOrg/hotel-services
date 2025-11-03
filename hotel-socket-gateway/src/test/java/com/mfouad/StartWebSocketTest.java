package com.mfouad;

import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;


@QuarkusTest
public class StartWebSocketTest {
	
	@TestHTTPResource("/start-websocket/UUSDF-SDF")
    URI uri;
	
	@Inject
	ClientManager clientManager;
	
	private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

	@Test
	public void start() throws Exception {
		
		Session session = ContainerProvider.getWebSocketContainer()
				.connectToServer(Client.class, uri);
		
		 Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
		 
		 System.out.println("sending message hello");
		 session.getAsyncRemote().sendText("hello world");
		 Assertions.assertTrue(clientManager.clientExists("UUSDF-SDF"));

	}
	
	@ClientEndpoint
	 static class Client {

	    @OnOpen
	    public void open(Session session) {
	        MESSAGES.add("CONNECT");
	        // Send a message to indicate that we are ready,
	        // as the message handler may not be registered immediately after this callback.
	        session.getAsyncRemote().sendText("_ready_");
	    }

	    @OnMessage
	    void message(String msg) {
	    	System.out.println(" in cleint on message "+msg);
	        MESSAGES.add(msg);
	    }
	    

	}

}


