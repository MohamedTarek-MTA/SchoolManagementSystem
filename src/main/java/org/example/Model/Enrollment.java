package org.example.Model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    private int id;
    private int studentId;
    private int subjectId;

    public Enrollment(int studentId,int subjectId){
        this.studentId = studentId;
        this.subjectId = subjectId;
    }
}
