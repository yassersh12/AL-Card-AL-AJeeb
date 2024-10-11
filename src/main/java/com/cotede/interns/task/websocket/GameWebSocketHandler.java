package com.cotede.interns.task.websocket;
import com.cotede.interns.task.user.UserSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameWebSocketHandler extends TextWebSocketHandler {

    private List<UserSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserSession userSession = UserSession.builder().session(session).build();
        if (sessions.size() == 1) {
            userSession.setPlayerNumber(1);
            session.sendMessage(new TextMessage("Waiting for the second player to join..."));
        } else if (sessions.size() == 2) {
            userSession.setPlayerNumber(2);
            startGame();
        }
        else {
            throw new Exception("Too many sessions");
        }

        sessions.add(userSession);
    }

    private void startGame() throws IOException {
        for (UserSession userSession : sessions) {
            userSession.getSession()
                    .sendMessage(new TextMessage("Both players joined. The game is starting now!"));
        }

        // Game service startgame with userSessions
    }


    //NEED MODIFICATION !!!! USER SESSION
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Retrieve the message sent from the client

        String clientMessage = message.getPayload();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

}
