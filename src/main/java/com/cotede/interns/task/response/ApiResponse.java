package com.cotede.interns.task.response;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@Builder
public class ApiResponse<T> extends ResponseEntity<T> {
    private T content;
    private HttpStatus status;
    private String message;
    public ApiResponse(T body, HttpStatus status) {
        super(body, status);
        this.message = null;
    }
    public ApiResponse(T body, HttpStatus status, String message) {
        super(body, status);
        this.message = message;
    }
}