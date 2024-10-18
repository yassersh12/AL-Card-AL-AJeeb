package com.cotede.interns.task.user;

import java.util.List;

public class UserUtility
{
    public static String userResponsesToPrompt(List<UserResponse> userResponses) {
        StringBuilder prompt = new StringBuilder();
        for (UserResponse userResponse : userResponses) {
            prompt.append("Username: ")
                    .append(userResponse.getUsername())
                    .append(", Attack Description: ")
                    .append(userResponse.getAttackDescription())
                    .append(". ");
        }
        return prompt.toString();
    }
}
