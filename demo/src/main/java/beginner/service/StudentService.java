package beginner.service;

import beginner.model.Student;

import java.util.List;

public interface StudentService {
    public String createStudent(Student student);
    public String updateStudent(Student student);
    public String deleteStudent(String id);
    public Student getStudent(String id);

    public List<Student> getAllStudent();
}
