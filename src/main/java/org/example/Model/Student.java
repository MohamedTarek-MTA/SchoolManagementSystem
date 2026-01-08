package org.example.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Student extends Person{
    private String email;
    private int age;
    public Student(int id,String name,String email,int age){
        super(id,name);
        this.email = email;
        this.age = age;
    }
    public Student(String name,String email,int age){
        super(name);
        this.email = email;
        this.age = age;
    }
}
