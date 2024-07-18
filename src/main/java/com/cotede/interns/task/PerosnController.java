package com.cotede.interns.task;

import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class  PerosnController {
   private List<Person>  personList = new ArrayList<>();


    // GET method that return the object based on the id in  the path as a parameter e.g ?id=1
   @GetMapping
   public ResponseEntity<Object> getPerson(
           @RequestParam(required = false) int id)
          {
       Optional<Person> person = personList.stream().filter(p -> p.getId() == id).findFirst();
    //Check if there is person exit
       if (person.isEmpty()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with ID " + id + " not found");
       }
    //return the person
       return ResponseEntity.ok(person.get());
   }


    @PostMapping
    //   POST Mapping accept the data from the body
   public ResponseEntity<Object> addPerson(@RequestBody Person person) {
    // Check if person is already exists
    for (Person p : personList) {
        if (p.getId() == person.getId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Person with ID " + person.getId() + " already exists");
        }
    }
    // If person does not exist then add the person
    if (person.getId() == 0 || person.getName() == null || person.getAddress() == null || person.getGender() == null || person.getAge() <= 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid person data");
    }

    // add person to the list
    personList.add(person);
    return ResponseEntity.status(HttpStatus.CREATED).build();
}




// UPDATE to new Value
   @PutMapping
   public ResponseEntity<Object> updatePerson(@RequestParam int id, @RequestBody Person updatedPerson) {
       // if it is exists
       Optional<Person> existingPersonOptional = personList.stream()
               .filter(p -> p.getId() == id)
               .findFirst();

       if (!existingPersonOptional.isPresent()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with ID " + id + " not found");
       }

       // Update to new value
       Person existingPerson = existingPersonOptional.get();
       existingPerson.setName(updatedPerson.getName());
       existingPerson.setAge(updatedPerson.getAge());
       existingPerson.setGender(updatedPerson.getGender());
       existingPerson.setAddress(updatedPerson.getAddress());

       // Return Person
       return ResponseEntity.ok(existingPerson);
   }

// DELETE PERSON
   @DeleteMapping
   public ResponseEntity<Object> deletePerson(@RequestParam int id) {
       // Find the person in the list
       Optional<Person> personOpt = personList.stream()
               .filter(p -> p.getId() == id)
               .findFirst();

       if (!personOpt.isPresent()) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person with ID " + id + " not found");
       }

       // Remove the person
       personList.remove(personOpt.get());

       // Return the response that delete success
       return ResponseEntity.ok("Person with ID " + id + " deleted successfully");
   }



}
