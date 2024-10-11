package com.cotede.interns.task.user;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSession {
    private String username;
    private String password;
    private int playerNumber;
    private WebSocketSession session;
}
