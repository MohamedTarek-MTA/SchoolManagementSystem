package org.example.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Grade {
    private int id;
    private int enrollmentId;
    private String grade;

    public Grade(String grade){
        this.grade = grade;
    }
    public Grade(int enrollmentId,String grade){
        this.enrollmentId = enrollmentId;
        this.grade = grade;
    }
}
