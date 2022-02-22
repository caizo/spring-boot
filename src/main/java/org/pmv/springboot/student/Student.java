package org.pmv.springboot.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "{org.pmv.springboot.constraint.name.NotNull.message}")
    @Size(min = 3,max = 30)
    private String name;
    @NotEmpty
    @Email
    private String email;
    @Transient // no column needed in table
    @Positive
    private Integer age;
    private LocalDate dob;

    public Student(String name, String email, LocalDate dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }


    public Integer getAge() {
        return Period.between(this.dob,LocalDate.now()).getYears();
    }
}
