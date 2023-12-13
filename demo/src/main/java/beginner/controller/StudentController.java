package beginner.controller;

import beginner.model.Student;
import beginner.repository.StudentRepository;
import beginner.response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student") //xac dinh URL
public class StudentController {


    @Autowired
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @GetMapping("/")
    public List<Student> getAllStudentDetails() {

        return studentRepository.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentDetails(@PathVariable("id") String id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student != null){
            return ResponseHandler.responseBuilder("Requested student Details are given here",
                    HttpStatus.OK, student);
        } else {
            return ResponseHandler.responseBuilder("",
                    HttpStatus.NOT_FOUND, null);
        }
    }


    @PostMapping("/")
    public String createStudentDetails(@RequestBody Student student) {
        Student student1 = studentRepository.save(student);
        return "Create student is successfully";
    }
    @PutMapping("/{id}")
    public String updateStudentDetails(@PathVariable String id ,@RequestBody Student student) {
        Optional<Student> updateStudent = studentRepository.findById(id);
        if (updateStudent.isPresent()) {
            student.getId();
            Student update = studentRepository.save(student);
            return "Student is updated successfully";
        } else
        {
            return  "Student is updated failed ";
        }

    }


    @DeleteMapping("/{id}")
    public String deleteStudentDetails(@PathVariable("id") String id) {
        Optional<Student> deleteStudent = studentRepository.findById(id);
        if (deleteStudent.isPresent()) {
            studentRepository.deleteById(id);
            return "Student is deleted successfully";
        } else {
            return "Student is deleted failed";
        }

    }
}
