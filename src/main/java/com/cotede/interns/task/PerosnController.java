package com.cotede.interns.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class  PerosnController {
    // ArrayList to store persons temporary
    private List<Person> personList = new ArrayList<>();


    // GET method that returns the object Based on the value of the id parameter
    @GetMapping
    public APIResponse<?>getPerson(@RequestHeader(value = "id", required = false) int id) {
        Optional<Person> person = personList.stream().filter(p -> p.getId() == id).findFirst();
        if (person.isEmpty()) {
            return new APIResponse<>( "Person not found", null, HttpStatus.NOT_FOUND);
        }
        return new APIResponse<>("Person found", person.get(),HttpStatus.OK);
    }


    // POST method to add a new person
    @PostMapping
    public APIResponse<?> addPerson(@RequestBody Person person) {
        for (Person p : personList) {
            if (p.getId() == person.getId()) {
                return new APIResponse<>("Person with ID " + person.getId() + " already exists", null, HttpStatus.CONFLICT);
            }
        }
        if (person.getId() == 0 || person.getName() == null || person.getAddress() == null || person.getGender() == null || person.getAge() <= 0) {
            return new APIResponse<>("Invalid person data", null, HttpStatus.BAD_REQUEST);
        }
        personList.add(person);
        return new APIResponse<>("Person created", null, HttpStatus.CREATED);
    }


    // PUT Mapping to update existing person
    @PutMapping
    public APIResponse<?> updatePerson(@RequestParam int id, @RequestBody Person updatedPerson) {
        Optional<Person> existingPersonOptional = personList.stream().filter(p -> p.getId() == id).findFirst();
        if (!existingPersonOptional.isPresent()) {
            return new APIResponse<>( "Person with ID " + id + " not found", null,HttpStatus.NOT_FOUND);
        }
        Person existingPerson = existingPersonOptional.get();
        existingPerson.setName(updatedPerson.getName());
        existingPerson.setAge(updatedPerson.getAge());
        existingPerson.setGender(updatedPerson.getGender());
        existingPerson.setAddress(updatedPerson.getAddress());
        return new APIResponse<>( "Person updated", existingPerson,HttpStatus.OK);
    }


    // DELETE Mapping to delete a person
    @DeleteMapping
    public APIResponse<?> deletePerson(@RequestParam int id) {
        Optional<Person> personOpt = personList.stream().filter(p -> p.getId() == id).findFirst();
        if (!personOpt.isPresent()) {
            return new APIResponse<>("Person with ID " + id + " not found", null,HttpStatus.NOT_FOUND);
        }
        personList.remove(personOpt.get());
        return new APIResponse<>( "Person deleted", null,HttpStatus.OK);
    }
}
