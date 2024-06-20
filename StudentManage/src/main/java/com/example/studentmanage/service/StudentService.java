package com.example.studentmanage.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.example.studentmanage.model.Student;
import com.example.studentmanage.repository.StudentRepository;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=students_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Student> listStudents = studentRepository.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Student Number", "Name", "Age", "Gender", "Class", "Address", "Meal Place", "Sleep Place"};
        String[] nameMapping = {"id", "studentNumber", "name", "age", "gender", "className", "address", "mealPlace", "sleepPlace"};

        csvWriter.writeHeader(csvHeader);

        for (Student student : listStudents) {
            csvWriter.write(student, nameMapping);
        }

        csvWriter.close();
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("StudentService bean is being destroyed");
    }
}
