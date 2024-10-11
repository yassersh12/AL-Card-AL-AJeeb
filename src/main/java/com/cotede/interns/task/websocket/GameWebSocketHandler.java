package com.cotede.interns.task.websocket;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        if (sessions.size() == 1) {
            session.sendMessage(new TextMessage("Waiting for the second player to join..."));
        } else if (sessions.size() == 2) {
            startGame();
        }
        else {
            throw new Exception("Too many sessions");
        }
    }

    private void startGame() throws IOException {
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage("Both players joined. The game is starting now!"));
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Retrieve the message sent from the client
        String clientMessage = message.getPayload();

        // Process the message (log it, send a response, etc.)
      //  System.out.println("Message received from client: " + clientMessage);

        for (WebSocketSession gameSession : sessions) {
            gameSession.sendMessage(new TextMessage(clientMessage));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

}
