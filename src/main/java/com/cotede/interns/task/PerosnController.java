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
           return ResponseEntity.notFound().build();
       }
       Map<String, Object> data = new HashMap<>();
       data.put("person", person.get());
       return ResponseEntity.ok(data);
   }
   @PostMapping
   public ResponseEntity<Map<String, Object>> addPerson(@RequestBody Person person){
      if(person.getId()==0||person.getName()==null||person.getAddress()==null || person.getGender()==null||person.getAge()<=0){
         return ResponseEntity.badRequest().build();
      }
      personList.add(person);
      return new ResponseEntity<>(createResponse("Sucess",null,person), HttpStatus.CREATED);
   }




























































//
//   @PutMapping
//   public ResponseEntity<Map<String, Object>> updatePerson(@RequestBody Person person){
//      if(person.getId()==0){
//         return new ResponseEntity<>(createResponse("Fail","Missing id",null), HttpStatus.BAD_REQUEST);
//      }
//
//      Optional<Person> personOptional = personList.stream().filter(p -> p.getId()==person.getId()).findFirst();
//      if(!personOptional.isPresent()){
//         return new ResponseEntity<>(createResponse("Fail","Missing id",null), HttpStatus.BAD_REQUEST;
//      }
//
//      personOptional.get().setName(person.getName());
//      personOptional.get().setAddress(person.getAddress());
//      personOptional.get().setGender(person.getGender());
//      personOptional.get().setAge(person.getAge());
//      personList.add(personOptional.get());
//
//   }
   private Map<String,Object> createResponse(String status, String message,Object data) {
      Map<String,Object> response = new HashMap<>();
      response.put("status", status);
      response.put("message", message);
      response.put("data", data);
      return response;

   }

}
