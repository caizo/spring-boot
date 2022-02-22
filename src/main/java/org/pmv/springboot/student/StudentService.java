package org.pmv.springboot.student;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getStudents(){
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
        return studentRepository.findById(studentId).get();
    }
}
