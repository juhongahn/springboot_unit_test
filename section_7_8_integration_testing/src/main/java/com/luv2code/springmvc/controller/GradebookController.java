package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService studentAndGradeService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
	}

	// JSON 형태로 firstname, lastnamem, emaill 이 날라올거고 잭슨이 알아서 매핑해줄꺼임
	@PostMapping("/")
	public String createStudent(@ModelAttribute("student") CollegeStudent student, Model model) {
		studentAndGradeService.createStudent(student.getFirstname(), student.getLastname(),
				student.getEmailAddress());
		Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
		model.addAttribute("students", collegeStudents);
		return "index";
	}


	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
	}

	@GetMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable int id, Model m) {

		if (!studentAndGradeService.checkIfStudentIsNullById(id)) {
			return "error";
		}

		studentAndGradeService.deleteStudent(id);
		Iterable<CollegeStudent> collegeStudents = studentAndGradeService.getGradebook();
		m.addAttribute("students", collegeStudents);
		return "index";
	}



}
