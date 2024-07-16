package com.example.firstone;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class  PerosnController {
    private final PersonServices personServices;
    @Autowired
    public PerosnController(PersonServices personServices) {
        this.personServices = personServices;
    }

    @GetMapping("/person")
    public List<Person> getPerson(){
        return  personServices.getPerson();
    }

    @PostMapping("/person") // Endpoint for adding a new person
    public void  addPerson(@RequestBody Person person) {
        personServices.addPerson(person);
        System.out.println(person.toString());
    }

}
