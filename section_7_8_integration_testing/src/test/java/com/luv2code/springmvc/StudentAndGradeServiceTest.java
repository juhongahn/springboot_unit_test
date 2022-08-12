package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("classpath:application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setupDatabase() {
        jdbcTemplate.execute("INSERT INTO Student(id, firstname, lastname, email_address) " +
                "values (1, 'Juhong', 'An', 'test@test.com')");
    }

    @AfterEach
    public void deleteDatabase() {
        jdbcTemplate.execute("DELETE FROM Student ");
    }

    @Test
    public void createStudentService() {
        studentService.createStudent("Juhong", "An", "wednesday5028@gmail.com");

        CollegeStudent student = studentDao.findByEmailAddress("wednesday5028@gmail.com");
        System.out.println(student);
        assertEquals("wednesday5028@gmail.com", student.getEmailAddress(), "Find by email");
    }

    @Test
    public void deleteStudentService() {

        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);

        assertTrue(deletedCollegeStudent.isPresent(), "Return true");

        studentService.deleteStudent(1);

        deletedCollegeStudent = studentDao.findById(1);

        assertFalse(deletedCollegeStudent.isPresent(), "Return false");

    }

    @Test
    public void isStudentNullCheck() {

        assertTrue(studentService.checkIfStudentIsNullById(1));
        assertFalse(studentService.checkIfStudentIsNullById(0));
    }

    @Sql("classpath:insertData.sql")
    @Test
    public void getGradebookService() {

        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();

        iterableCollegeStudents.forEach((collegeStudents::add));

        assertEquals(5, collegeStudents.size());
    }

}
