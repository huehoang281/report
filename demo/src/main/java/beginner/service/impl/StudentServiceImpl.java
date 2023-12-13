package beginner.service.impl;

import beginner.exception.StudentNotFoundException;
import beginner.model.Student;
import beginner.repository.StudentRepository;
import beginner.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    StudentRepository studentRepository;
    public StudentServiceImpl(StudentRepository studentRepository) {
                this.studentRepository = studentRepository;
    }
    @Override
    public String createStudent(Student student) {
        studentRepository.save(student);
        return "Success";
    }
    @Override
    public String updateStudent(Student student){
        studentRepository.save(student);
        return "Success";
    }
    public Student getStudent(String id){

        if(studentRepository.findById(id).isEmpty())
            throw new StudentNotFoundException("Requested student does not exits");

        return studentRepository.findById(id).get();
    }
    @Override
    public String deleteStudent(String id){
        studentRepository.deleteById(id);
        return "Success";
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

}
