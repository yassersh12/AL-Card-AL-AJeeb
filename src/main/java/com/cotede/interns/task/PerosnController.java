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



   @GetMapping
    public ResponseEntity<Map<String, Object>> getPerson(@RequestParam(required = false) int id , @RequestHeader HttpHeaders headers){
       Optional<Person> person = personList.stream().filter(p -> p.getId()==id).findFirst();
       if(!person.isPresent()){
           return new ResponseEntity<>(createResponse("Fail","ID NOT FOUND",null),HttpStatus.NOT_FOUND);
       }
       Map<String, Object> data = new HashMap<>();
       data.put("person", person.get());
       return ResponseEntity.ok(data);
   }
   @PostMapping
   public ResponseEntity<Map<String, Object>> addPerson(@RequestBody Person person){
      if(person.getId()==0||person.getName()==null||person.getAddress()==null || person.getGender()==null||person.getAge()<=0){
         return new ResponseEntity<>(createResponse("Fail","ID NOT FOUND",null),HttpStatus.NOT_FOUND);
      }
      personList.add(person);
      return new ResponseEntity<>(createResponse("Sucess",null,person), HttpStatus.CREATED);
   }


   @PutMapping("/{id}")
   public ResponseEntity<Map<String, Object>> updatePerson(
           @PathVariable int id,
           @RequestBody Person updatedPerson
   ) {
      Optional<Person> existingPersonOptional = personList.stream()
              .filter(p -> p.getId() == id)
              .findFirst();

      if (!existingPersonOptional.isPresent()) {
         return new ResponseEntity<>(createResponse("Fail","ID NOT FOUND",null),HttpStatus.NOT_FOUND);
      }

      Person existingPerson = existingPersonOptional.get();

      // Update the fields of the existing person with the values from updatedPerson
      existingPerson.setName(updatedPerson.getName());
      existingPerson.setAge(updatedPerson.getAge());
      existingPerson.setGender(updatedPerson.getGender());
      existingPerson.setAddress(updatedPerson.getAddress());

      // Respond with updated person details
      return ResponseEntity.ok(createResponse("Success", null, existingPerson));
   }


   @DeleteMapping
   public ResponseEntity<Map<String, Object>> deletePerson(@RequestParam int id){
      Optional<Person> personOpt = personList.stream().filter(p -> p.getId() == id).findFirst();
      if(!personOpt.isPresent()){
         return new ResponseEntity<>(createResponse("Fail","ID NOT FOUND",null),HttpStatus.NOT_FOUND);

      }
      personList.remove(personOpt.get());
      return ResponseEntity.ok(createResponse("Success", "Delete Success", personOpt));
   }



   private Map<String,Object> createResponse(String status, String message,Object data) {
      Map<String,Object> response = new HashMap<>();
      response.put("status", status);
      response.put("message", message);
      response.put("data", data);
      return response;

   }

}
