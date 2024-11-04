package com.cotede.interns.task.round;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoundResult {
    private long yourHealth;
    private long opponentHealth;
    private long damageYouReceived;
    private long damageYouDealt;


    // Getters and setters (omitted for brevity)
}
