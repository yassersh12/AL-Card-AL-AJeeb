package com.cotede.interns.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class  PerosnController {
    private List<Person> personList = new ArrayList<>();
    // GET method that returns the object based on the id in the path as a parameter e.g ?id=1
    @GetMapping
    public ResponseEntity<APIResponse<?>> getPerson(@RequestParam(required = false) int id) {
        Optional<Person> person = personList.stream().filter(p -> p.getId() == id).findFirst();

        if (person.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Person not found", null));
        }

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Person found", person.get()));
    }

    // POST Mapping accepts the data from the request body
    @PostMapping
    public ResponseEntity<APIResponse<?>> addPerson(@RequestBody Person person,
                                                    @RequestHeader(value = "Content-Length", required = false) String contentLength) {
        System.out.println("Content-Length : " + contentLength);

        for (Person p : personList) {
            if (p.getId() == person.getId()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new APIResponse<>(HttpStatus.CONFLICT.value(), "Person with ID " + person.getId() + " already exists", null));
            }
        }

        if (person.getId() == 0 || person.getName() == null || person.getAddress() == null || person.getGender() == null || person.getAge() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid person data", null));
        }

        personList.add(person);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Person created", null));
    }

    // PUT Mapping to update existing person
    @PutMapping
    public ResponseEntity<APIResponse<?>> updatePerson(@RequestParam int id, @RequestBody Person updatedPerson) {
        Optional<Person> existingPersonOptional = personList.stream().filter(p -> p.getId() == id).findFirst();

        if (!existingPersonOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Person with ID " + id + " not found", null));
        }

        Person existingPerson = existingPersonOptional.get();
        existingPerson.setName(updatedPerson.getName());
        existingPerson.setAge(updatedPerson.getAge());
        existingPerson.setGender(updatedPerson.getGender());
        existingPerson.setAddress(updatedPerson.getAddress());

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Person updated", existingPerson));
    }

    // DELETE Mapping to delete a person
    @DeleteMapping
    public ResponseEntity<APIResponse<?>> deletePerson(@RequestParam int id) {
        Optional<Person> personOpt = personList.stream().filter(p -> p.getId() == id).findFirst();

        if (!personOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Person with ID " + id + " not found", null));
        }

        personList.remove(personOpt.get());

        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Person deleted", null));
    }


}
