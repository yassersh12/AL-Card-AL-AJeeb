package com.cotede.interns.task.game;

import com.cotede.interns.task.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ApiResponse<Game> createGame(@RequestBody Game game) {
        Game createdGame = gameService.createGame(game);
        return ApiResponse.<Game>builder()
                .content(createdGame)
                .status(HttpStatus.CREATED)
                .message("Game created successfully")
                .build();
    }

    @GetMapping("/{gameId}")
    public ApiResponse<Game> getGameById(@PathVariable Long gameId) {
        return gameService.getGameById(gameId)
                .map(game -> ApiResponse.<Game>builder()
                        .content(game)
                        .status(HttpStatus.OK)
                        .message("Game found")
                        .build())
                .orElseGet(() -> ApiResponse.<Game>builder()
                        .content(null)
                        .status(HttpStatus.NOT_FOUND)
                        .message("Game not found")
                        .build());
    }

    @GetMapping
    public ApiResponse<List<Game>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return ApiResponse.<List<Game>>builder()
                .content(games)
                .status(HttpStatus.OK)
                .message("Games retrieved successfully")
                .build();
    }

    @DeleteMapping("/{gameId}")
    public ApiResponse<Void> deleteGame(@PathVariable Long gameId) {
        gameService.deleteGame(gameId);
        return ApiResponse.<Void>builder()
                .content(null)
                .status(HttpStatus.NO_CONTENT)
                .message("Game deleted successfully")
                .build();
    }
}
