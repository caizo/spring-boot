package org.pmv.springboot.student;

import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(Views.Internal.class)
    private Long id;
    @NotNull(message = "{org.pmv.springboot.constraint.name.NotNull.message}")
    @Size(min = 3,max = 30)
    @JsonView(Views.External.class)
    private String name;
    @NotEmpty
    @Email
    @JsonView(Views.External.class)
    private String email;
    @Transient // no column needed in table
    @Positive
    @JsonView(Views.External.class)
    private Integer age;
    @JsonView(Views.External.class)
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
