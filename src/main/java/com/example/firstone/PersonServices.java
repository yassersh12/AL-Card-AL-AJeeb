package com.example.firstone;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PersonServices {

    public List<Person> getPerson(){
        return  List.of(
                new Person("Ahmed",19,"Dura")
        );
    }
    public Person addPerson(Person person) {
        return new Person("Ahmed", 19, "Dura");

    }
}
