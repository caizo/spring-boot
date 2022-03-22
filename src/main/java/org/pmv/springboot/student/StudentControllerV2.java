package org.pmv.springboot.student;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v2/student")
public class StudentControllerV2 {

    private final StudentService studentService;

    /**
     *
     * @return
     */

    @JsonView(Views.External.class)
    @GetMapping("/external")
    public List<Student> getStudentsExternal(){
        //throw new ApiRequestException("Opps cannot get all students");
        return studentService.getStudents();
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/internal")
    public List<Student> getStudentsInternal(){
        //throw new ApiRequestException("Opps cannot get all students");
        return studentService.getStudents();
    }

    /**
     *
     * @param student
     */
    @PostMapping
    public void saveStudent(@Valid @RequestBody Student student){
        log.info("Saving student {},",student);
        studentService.save(student);
    }


    /**
     *
     * @param studentId
     */
    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId){
        studentService.deleteStudent(studentId);
    }


    /**
     *
     * @param studentId
     * @param name
     * @param email
     */
//    @PutMapping(path = "{studentId}")
//    public void updateStudent(@PathVariable("studentId") Long studentId,
//                              @RequestParam(required = false) String name,
//                              @RequestParam(required = false) String email){
//        studentService.updateStudent(studentId,name,email);
//    }

    @PutMapping()
    public void updateStudent(@Valid @RequestBody Student student){
        studentService.updateStudent(student);
    }


    /**
     *
     * @param studentId
     * @return
     */
    @GetMapping(path = "{studentId}")
    public Student getStudentById(@PathVariable("studentId") Long studentId){
        return studentService.getStudentById(studentId);
    }

}
