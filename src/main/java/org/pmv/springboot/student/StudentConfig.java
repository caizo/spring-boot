package org.pmv.springboot.student;

import org.pmv.springboot.infoapp.InfoApp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean("studentRunner")
    CommandLineRunner commandLineRunner(StudentRepository repository, InfoApp infoApp){
        return args -> {
            System.out.println(infoApp);
            Student pablo = new Student("Pablo", "pmv@gmail.com", LocalDate.of(1983, Month.APRIL, 9));
            Student toni = new Student("Antonio", "ant@gmail.com", LocalDate.of(1982, Month.FEBRUARY, 23));
            Student jose = new Student("José", "juju@gmail.com", LocalDate.of(1982, Month.NOVEMBER, 7));
            Student evaristo = new Student("Evaristo", "varis@gmail.com", LocalDate.of(1982, Month.MAY, 27));
            Student juan = new Student("Juan Manuel", "chupis@gmail.com", LocalDate.of(1982, Month.MAY, 2));
            Student guillo = new Student("Guillermo", "guillo@gmail.com", LocalDate.of(1981, Month.OCTOBER, 16));
            Student alejandro = new Student("Alejandro", "negro@gmail.com", LocalDate.of(1978, Month.OCTOBER, 21));
            Student costan = new Student("Constantino", "buba@gmail.com", LocalDate.of(1982, Month.MAY, 26));
            repository.saveAll(List.of(pablo,toni,jose,evaristo,juan,guillo,alejandro,costan));
        };
    }
}
