package com.cotede.interns.task.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiUserAttack {
    private Long damage;
    private Long creativity;
    private String description;
}
