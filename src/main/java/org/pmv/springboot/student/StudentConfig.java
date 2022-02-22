package org.pmv.springboot.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args -> {
            Student pablo = new Student("Pablo", "pmv@gmail.com", LocalDate.of(1983, Month.APRIL, 9));
            Student toni = new Student("Antonio", "ant@gmail.com", LocalDate.of(1982, Month.FEBRUARY, 23));
            repository.saveAll(List.of(pablo,toni));
        };
    }
}