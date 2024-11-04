package com.cotede.interns.task.websocket;

import com.cotede.interns.task.game.GameCreationResponse;
import com.cotede.interns.task.game.GameService;
import com.cotede.interns.task.round.Round;
import com.cotede.interns.task.round.RoundCreationResponse;
import com.cotede.interns.task.round.RoundResult;
import com.cotede.interns.task.round.RoundService;
import com.cotede.interns.task.user.*;

import com.cotede.interns.task.userAttack.UserAttack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class GameWebSocketHandler extends TextWebSocketHandler
{
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
    private boolean player1Answered = false;
    private boolean player2Answered = false;

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
            handleLogin(session, message, userSession);
        } else if (!gameStarted) {
            session.sendMessage(new TextMessage("Game not started yet!"));
        } else {
            handleGameRound(session, message, userSession);
        }
    }

    private void handleLogin(WebSocketSession session, TextMessage message, UserSession userSession) throws Exception {
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
                    player1.setHealth(100L);
                    player1.setAvgCreativity(0L);
                    session.sendMessage(new TextMessage("Waiting for the second player to log in..."));
                } else if (userSession.getPlayerNumber() == 2) {
                    player2 = storedUser;
                    player2.setHealth(100L);
                    player2.setAvgCreativity(0L);
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
    }

    private void handleGameRound(WebSocketSession session, TextMessage message, UserSession userSession) throws Exception {
        // Check if both players have answered or if this player already answered
        if (userResponses.size() == 2) {
            session.sendMessage(new TextMessage("The next round has not started yet."));
            return;
        }

        if ((userSession.getPlayerNumber() == 1 && player1Answered) || (userSession.getPlayerNumber() == 2 && player2Answered)) {
            session.sendMessage(new TextMessage("Still waiting for the second player to answer."));
            return;
        }

        // Process player answer
        String payload = message.getPayload();
        UserResponse attack = objectMapper.readValue(payload, UserResponse.class);
        attack.setUsername(userSession.getUsername());
        userResponses.add(attack);

        if (userSession.getPlayerNumber() == 1) {
            player1Answered = true;
        } else {
            player2Answered = true;
        }

        // Start a new round if both players have answered
        if (userResponses.size() == 2) {
            sendAttacksToServer(userResponses);
            userResponses.clear();
            player1Answered = false;
            player2Answered = false;
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
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(gameWithRound.getRoundCreationResponse());
        for (WebSocketSession session : sessions.keySet()) {
            session.sendMessage(new TextMessage("Both players joined. The game is starting now!"));
            session.sendMessage(new TextMessage(jsonResponse));
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

        // Variables to track damage dealt to each player
        long damageToPlayer1 = 0L;
        long damageToPlayer2 = 0L;

        // Process attacks and update health
        for (UserAttack attack : round.getUserAttacks()) {
            System.out.println("Evaluating attack from: " + attack.getUser().getUsername());
            if (attack.getUser().getUsername().equals(player1.getUsername())) {
                // Player1 attacks Player2
                damageToPlayer2 += attack.getDamage();
                player2.setHealth(player2.getHealth() - attack.getDamage());
                player1.setAvgCreativity(player1.getAvgCreativity() + attack.getCreativity());
            } else {
                // Player2 attacks Player1
                damageToPlayer1 += attack.getDamage();
                player1.setHealth(player1.getHealth() - attack.getDamage());
                player2.setAvgCreativity(player2.getAvgCreativity() + attack.getCreativity());
            }
        }

        // Ensure health doesn't drop below zero
        player1.setHealth(Math.max(player1.getHealth(), 0L));
        player2.setHealth(Math.max(player2.getHealth(), 0L));

        // Check if the game has ended
        if (player1.getHealth() <= 0L || player2.getHealth() <= 0L) {
            gameStarted = false;
            String winner = player1.getHealth() <= 0L ? player2.getUsername() : player1.getUsername();

            // Notify each player about the game result
            for (Map.Entry<WebSocketSession, UserSession> entry : sessions.entrySet()) {
                WebSocketSession session = entry.getKey();
                UserSession userSession = entry.getValue();
                String message = "The game has finished. The winner is " + winner + ".";
                session.sendMessage(new TextMessage(message));
            }
        } else {
            // Prepare round results for each player
            RoundResult resultForPlayer1 = new RoundResult(
                    player1.getHealth(),
                    player2.getHealth(),
                    damageToPlayer1,
                    damageToPlayer2
            );

            RoundResult resultForPlayer2 = new RoundResult(
                    player2.getHealth(),
                    player1.getHealth(),
                    damageToPlayer2,
                    damageToPlayer1
            );

            ObjectMapper objectMapper = new ObjectMapper();

            // Send the results to each player
            for (Map.Entry<WebSocketSession, UserSession> entry : sessions.entrySet()) {
                WebSocketSession session = entry.getKey();
                UserSession userSession = entry.getValue();
                RoundResult result = userSession.getUsername().equals(player1.getUsername())
                        ? resultForPlayer1
                        : resultForPlayer2;

                String jsonResponse = objectMapper.writeValueAsString(result);
                session.sendMessage(new TextMessage(jsonResponse));
            }

            // Start the next round
            RoundCreationResponse roundCreationResponse = roundService.createRoundResponse();
            String jsonResponse = objectMapper.writeValueAsString(roundCreationResponse);
            for (WebSocketSession session : sessions.keySet()) {
                session.sendMessage(new TextMessage(jsonResponse));
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

}