package com.cotede.interns.task.game;

import com.cotede.interns.task.round.RoundCreationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameCreationResponse {
    private Long gameId;
    private GameStatus gameStatus;
    private RoundCreationResponse roundCreationResponse;
}
