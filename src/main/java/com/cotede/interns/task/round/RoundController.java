package com.cotede.interns.task.round;

import com.cotede.interns.task.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rounds")
public class RoundController {

    @Autowired
    private RoundService roundService;

    @PostMapping
    public ApiResponse<Round> createRound(@RequestBody Round round) {
        Round createdRound = roundService.createRound(round);
        return ApiResponse.<Round>builder()
                .content(createdRound)
                .status(HttpStatus.CREATED)
                .message("Round created successfully")
                .build();
    }

    @GetMapping("/{roundId}")
    public ApiResponse<Round> getRoundById(@PathVariable Long roundId) {
        return roundService.getRoundById(roundId)
                .map(round -> ApiResponse.<Round>builder()
                        .content(round)
                        .status(HttpStatus.OK)
                        .message("Round found")
                        .build())
                .orElseGet(() -> ApiResponse.<Round>builder()
                        .content(null)
                        .status(HttpStatus.NOT_FOUND)
                        .message("Round not found")
                        .build());
    }

    @GetMapping
    public ApiResponse<List<Round>> getAllRounds() {
        List<Round> rounds = roundService.getAllRounds();
        return ApiResponse.<List<Round>>builder()
                .content(rounds)
                .status(HttpStatus.OK)
                .message("Rounds retrieved successfully")
                .build();
    }

    @DeleteMapping("/{roundId}")
    public ApiResponse<Void> deleteRound(@PathVariable Long roundId) {
        roundService.deleteRound(roundId);
        return ApiResponse.<Void>builder()
                .content(null)
                .status(HttpStatus.NO_CONTENT)
                .message("Round deleted successfully")
                .build();
    }
}
