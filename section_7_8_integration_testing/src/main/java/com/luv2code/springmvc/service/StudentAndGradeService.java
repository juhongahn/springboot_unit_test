package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    private StudentDao studentDao;

    @Autowired
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void createStudent(String firstName, String lastName, String emailAddress){
        CollegeStudent collegeStudent = new CollegeStudent(firstName,lastName,emailAddress);

        studentDao.save(collegeStudent);
    }

    public boolean checkIfStudentIsNullById(int id) {

        Optional<CollegeStudent> student = studentDao.findById(id);
        return student.isPresent();
    }

    public void deleteStudent(int id) {
        if (checkIfStudentIsNullById(id)){
            studentDao.deleteById(id);
        }
    }

    public Iterable<CollegeStudent> getGradebook() {
        return studentDao.findAll();
    }
}
