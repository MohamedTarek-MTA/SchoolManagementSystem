package org.example.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject {
    private int id;
    private String name;
    private int teacherId;

    public Subject(String name,int teacherId){
        this.name = name;
        this.teacherId = teacherId;
    }
    public Subject(String name){
        this.name = name;
    }
}
