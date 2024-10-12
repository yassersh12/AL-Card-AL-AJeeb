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
    private String object1;
    private String object2;
    private String cardDescription1;
    private String cardDescription2;
    private String summary;
}
