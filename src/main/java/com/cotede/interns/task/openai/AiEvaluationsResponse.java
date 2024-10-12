package com.cotede.interns.task.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiEvaluationsResponse {
    private Long damage1;
    private Long damage2;
    private Long creativity1;
    private Long creativity2;
    private String description1;
    private String description2;
    private String summary;
}
