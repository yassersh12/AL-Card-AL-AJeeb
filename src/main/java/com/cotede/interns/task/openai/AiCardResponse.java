package com.cotede.interns.task.openai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiCardResponse {
    private String object;
    private String cardDescription;
    private String summary;
}
