package org.example.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private int id;
    private String name;

    public Person(String name){
        this.name = name;
    }
}
