package com.example.firstone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SpringBootApplication
public class FirstOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstOneApplication.class, args);

    }

}
