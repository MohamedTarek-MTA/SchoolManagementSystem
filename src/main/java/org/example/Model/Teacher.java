package org.example.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Teacher extends Person{
    private String specialization;

    public Teacher(int id,String name,String specialization){
        super(id,name);
        this.specialization = specialization;
    }
    public Teacher(String name,String specialization){
        super(name);
        this.specialization = specialization;
    }
}
