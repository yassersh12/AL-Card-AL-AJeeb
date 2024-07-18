package com.cotede.interns.task;
// Person Class

public class Person {
  private int id ;
  private String name ;
  private int age ;
  private String gender;
  private String Address;

    public Person(int id, String address, String gender, int age, String name) {
        this.id = id;
        Address = address;
        this.gender = gender;
        this.age = age;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
