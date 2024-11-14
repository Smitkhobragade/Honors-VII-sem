package com.rcoem.devops.services;

import com.rcoem.devops.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServices {

    private HashMap<String, Student> students;

    @PostConstruct
    public void setup() {
        students = new HashMap<>();
    }

    // Create a new student
    public String createStudent(Student student) {
        student.setStudentId(UUID.randomUUID().toString());
        students.put(student.getStudentId(), student);
        return student.getStudentId();
    }

    // Retrieve all students
    public List<Student> getAllStudents() {
        return students.values().stream().collect(Collectors.toList());
    }

    // Retrieve a student by ID
    public Student getStudent(String id) {
        return students.getOrDefault(id, null);
    }

    // Update an existing student
    public boolean updateStudent(String id, Student updatedStudent) {
        if (!students.containsKey(id)) {
            return false; // Student not found
        }
        updatedStudent.setStudentId(id);
        students.put(id, updatedStudent);
        return true;
    }

    // Delete a student
    public boolean deleteStudent(String id) {
        if (!students.containsKey(id)) {
            return false; // Student not found
        }
        students.remove(id);
        return true;
    }

    // Check if a student exists by name and email
    public boolean existsByNameAndEmail(String name, String email) {
        return students.values().stream()
                .anyMatch(student -> student.getName().equalsIgnoreCase(name) && student.getEmail().equalsIgnoreCase(email));
    }

}