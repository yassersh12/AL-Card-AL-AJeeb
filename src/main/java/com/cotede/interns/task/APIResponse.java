package com.cotede.interns.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIResponse<T> extends ResponseEntity<APIResponse.Body<T>> {
    private String message;
    private T data;

    public APIResponse(String message, T data, HttpStatus httpStatus) {
        super(new Body<>(message, data, httpStatus), httpStatus);
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // Inner class to represent the response body structure
    public static class Body<T> {
        private String message;
        private T data;
        private HttpStatus httpStatus;

        public Body(String message, T data, HttpStatus httpStatus) {
            this.message = message;
            this.data = data;
            this.httpStatus = httpStatus;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public void setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }
    }
}
