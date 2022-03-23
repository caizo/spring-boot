package org.pmv.springboot.student;

import lombok.AllArgsConstructor;
import org.pmv.springboot.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public List<Student> getStudents(){
        LOGGER.info("getStudents was called");
        return studentRepository.findAll();
    }


    /**
     *
     * @param student
     */
    public void save(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Wrong email");
        }
        studentRepository.save(student);
    }

    /**
     *
     * @param studentId
     */
    public void deleteStudent(Long studentId) {
        boolean userExists = studentRepository.existsById(studentId);
        if(userExists){
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalStateException("Sudent with id " + studentId+ " does not exists");
        }

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student to be updated not found"));

        Optional<String> optionalName = Optional.ofNullable(name);
        if(optionalName.isPresent() && !optionalName.isEmpty()){
            student.setName(name);
        }

        Optional<String> optionalEmail = Optional.ofNullable(email);
        if(optionalEmail.isPresent() && !optionalEmail.isEmpty() &&
                !Objects.equals(student.getEmail(),email)){
            Optional<Student> optionalStudent = studentRepository.findStudentByEmail(email);
            if(optionalStudent.isPresent()){
                throw new IllegalStateException("Student already present");
            } else {
                student.setEmail(email);
            }
        }
    }

    /**
     *
     * @param studentId
     * @return
     */
    public Student getStudentById(Long studentId) {
//        return studentRepository.findById(studentId).orElseThrow(
//                () -> new NotFoundException("Sudent with ID: " + studentId + " NOT FOUND."));
        return studentRepository.findById(studentId).orElseThrow(
                () -> {
                    NoSuchElementException exception =
                            new NoSuchElementException("Sudent with ID: " + studentId + " NOT FOUND.");
                    LOGGER.error("Error in getStudentById: {}",studentId, exception);

                    return exception;
                });
                
    }


    /**
     *
     * @param student
     */
    public void updateStudent(Student student) {
        studentRepository.save(student);
    }
}
