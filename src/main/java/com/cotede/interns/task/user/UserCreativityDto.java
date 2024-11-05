package com.cotede.interns.task.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreativityDto {
    private int rank;
    private String username;
    private Long avgCreativity;
}
