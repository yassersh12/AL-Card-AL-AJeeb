package com.cotede.interns.task.game;

import com.cotede.interns.task.round.Round;
import com.cotede.interns.task.round.RoundCreationResponse;
import com.cotede.interns.task.round.RoundService;
import com.cotede.interns.task.user.User;
import com.cotede.interns.task.user.UserService;
import com.cotede.interns.task.user.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final RoundService roundService;
    private Game currentGame;
    private List<UserSession> userSessions;

    public GameCreationResponse createGame(String username1, String username2) throws Exception {
        /*userSessions.add(userSession1);
        userSessions.add(userSession2);*/

        Optional<User> player1 = userService.getUserByUsername(username1);
        Optional<User> player2 = userService.getUserByUsername(username2);

        GameStatus status = GameStatus.ON_GOING;

        currentGame = Game.builder()
                .player1(player1.get())
                .player2(player2.get())
                .status(status)
                .build();

        gameRepository.save(currentGame);

        RoundCreationResponse roundCreationResponse = roundService.createRoundResponse();
        GameCreationResponse response = new GameCreationResponse(currentGame.getGameId(),
                currentGame.getStatus(), roundCreationResponse);

        return response;
    }

    public Optional<Game> getGameById(Long gameId) {
        return gameRepository.findById(gameId);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public void deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
    }
}
