package com.cotede.interns.task.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiEvaluationsResponse {
    List<AiUserAttack> aiUserAttacks;
    private String summary;
}
