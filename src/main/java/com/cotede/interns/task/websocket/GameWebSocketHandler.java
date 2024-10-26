package com.cotede.interns.task.websocket;

import com.cotede.interns.task.game.GameCreationResponse;
import com.cotede.interns.task.game.GameService;
import com.cotede.interns.task.round.Round;
import com.cotede.interns.task.round.RoundCreationResponse;
import com.cotede.interns.task.round.RoundService;
import com.cotede.interns.task.user.*;

import com.cotede.interns.task.userAttack.UserAttack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class GameWebSocketHandler extends TextWebSocketHandler {
    private final RoundService roundService;
    private final Map<WebSocketSession, UserSession> sessions = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final GameService gameService;
    private boolean gameStarted = false;
    private List<UserResponse> userResponses = new ArrayList<>();
    private long gameId;
    private User player1;
    private User player2;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserSession userSession = UserSession.builder().build();
        sessions.put(session, userSession);
        userSession.setActive(false);

        int playerNumber = sessions.size();
        userSession.setPlayerNumber(playerNumber);

        session.sendMessage(new TextMessage("Please enter your username and password in JSON format."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UserSession userSession = sessions.get(session);
        if (!userSession.isActive()) {
            // Handle login
            String payload = message.getPayload();
            UserInfo userInfo = objectMapper.readValue(payload, UserInfo.class);
            Optional<User> userFromDb = userRepository.findByUsername(userInfo.getUsername());
            if (userFromDb.isPresent()) {
                User storedUser = userFromDb.get();
                if (userInfo.getPassword().equals(storedUser.getPassword())) {
                    session.sendMessage(new TextMessage("Login successful!"));
                    userSession.setActive(true);
                    userSession.setUsername(userInfo.getUsername());
                    if (userSession.getPlayerNumber() == 1) {
                        player1 = storedUser;
                        session.sendMessage(new TextMessage("Waiting for the second player to log in..."));
                    } else if (userSession.getPlayerNumber() == 2) {
                        player2 = storedUser;
                    }
                    // Check if both players are logged in
                    if (allPlayersLoggedIn()) {
                        startGame();
                    }
                } else {
                    session.sendMessage(new TextMessage("Invalid password!"));
                }
            } else {
                session.sendMessage(new TextMessage("User not found!"));
            }
        } else if (!gameStarted) {
            session.sendMessage(new TextMessage("Game not started yet!"));
        } else {
            // Handle game messages
            String payload = message.getPayload();
            UserResponse attack = objectMapper.readValue(payload, UserResponse.class);
            attack.setUsername(userSession.getUsername());
            userResponses.add(attack);
            if (userResponses.size() == 2) {
                sendAttacksToServer(userResponses);
                userResponses.clear();
            }
        }
    }

    private boolean allPlayersLoggedIn() {
        if (sessions.size() < 2) {
            return false;
        }
        for (UserSession userSession : sessions.values()) {
            if (!userSession.isActive()) {
                return false;
            }
        }
        return true;
    }

    private void startGame() throws Exception {
        gameStarted = true;
        GameCreationResponse gameWithRound = gameService.createGame(player1.getUsername(), player2.getUsername());
        gameId = gameWithRound.getGameId();

        for (WebSocketSession session : sessions.keySet()) {
            session.sendMessage(new TextMessage("Both players joined. The game is starting now!"));
            session.sendMessage(new TextMessage(gameWithRound.getRoundCreationResponse().toString()));
        }
    }

    private void sendAttacksToServer(List<UserResponse> attacks) throws Exception {
        Round round = roundService.createRound(attacks, gameId);
        evaluateRoundResults(round);
    }

    private void evaluateRoundResults(Round round) throws Exception {
        if (round.getUserAttacks().isEmpty()) {
            System.out.println("No attacks were made in this round.");
            return;
        }

        for (UserAttack attack : round.getUserAttacks()) {
            System.out.println("Evaluating attack from: " + attack.getUser().getUsername());
            if (attack.getUser().getUsername().equals(player1.getUsername())) {
                player2.setHealth(player2.getHealth() - attack.getDamage());
                player1.setAvgCreativity(player1.getAvgCreativity() + attack.getCreativity());
            } else {
                player1.setHealth(player1.getHealth() - attack.getDamage());
                player2.setAvgCreativity(player2.getAvgCreativity() + attack.getCreativity());
            }
        }

        if (player1.getHealth() <= 0 || player2.getHealth() <= 0) {
            for (WebSocketSession session : sessions.keySet()) {
                session.sendMessage(new TextMessage("The game has finished"));
            }
            gameStarted = false; // Reset game status
        } else {
            RoundCreationResponse roundCreationResponse = roundService.createRoundResponse();
            for (WebSocketSession session : sessions.keySet()) {
                session.sendMessage(new TextMessage(roundCreationResponse.toString()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }
}
