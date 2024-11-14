package com.rcoem.devops.interfaces;

import com.rcoem.devops.Student;
import com.rcoem.devops.services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    StudentServices studentService;

    @GetMapping("/test")
    public String testStudent(){
        return "Started Test Server";
    }

    // Register a new student
    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody Student student) {
        if (studentService.existsByNameAndEmail(student.getName(), student.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A student with the same name and email already exists.");
        }
        String studentId = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body("Student registered successfully with ID: " + studentId);
    }

    // Student login
    @PostMapping("/login")
    public ResponseEntity<String> loginStudent(@RequestParam String email, @RequestParam String password) {
        Student student = studentService.getAllStudents().stream()
                .filter(s -> s.getEmail().equals(email) && s.getPassword().equals(password))
                .findFirst()
                .orElse(null);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
        return ResponseEntity.ok("Login successful.");
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(students);
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(student);
    }

    // Update student information
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        boolean isUpdated = studentService.updateStudent(id, updatedStudent);
        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        return ResponseEntity.ok("Student information updated successfully.");
    }

    // Delete student by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
        boolean isDeleted = studentService.deleteStudent(id);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
        return ResponseEntity.ok("Student deleted successfully.");
    }


}
