package beginner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice // xu li exception toàn cau cho all controller
public class StudentExceptionHandler {

    @ExceptionHandler(value = { StudentNotFoundException.class}) // xac dinh phuong thuc xu ly exception
    public ResponseEntity<Object> handlerStudentNotFoundException
            (StudentNotFoundException studentNotFoundException)
    {
        StudentException studentException = new StudentException(
                studentNotFoundException.getMessage(),
                studentNotFoundException.getCause(),
                HttpStatus.NOT_FOUND

        );
        return new ResponseEntity<>(studentException,HttpStatus.NOT_FOUND);
    }
}
