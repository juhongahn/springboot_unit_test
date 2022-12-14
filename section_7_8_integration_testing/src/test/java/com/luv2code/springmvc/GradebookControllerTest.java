package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.*;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentCreateServiceMock;

    @Autowired
    private StudentDao studentDao;

    //@BeforeAll ?????????????????? static??????????????? ???.(????????? ???????????? ?????? ??????????????? ?????? ????????????)
    @BeforeAll
    public static void setup() {
        // ?????? ????????? ????????? ??????????????? (????????????????????? ?????? ????????? ????????????)
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Juhong");
        request.setParameter("lastname", "Ahn");
        request.setParameter("emailAddress", "k1ng@test.com");
    }

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.execute("INSERT INTO Student(id, firstname, lastname, email_address) " +
                "values (1, 'Juhong', 'An', 'test@test.com')");
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM Student ");
    }

    @Test
    public void getStudentsHttpRequest() throws Exception {
        CollegeStudent studentOne = new GradebookCollegeStudent("Juhong", "Ahn",
                "test@test.com");

        CollegeStudent studentTwo = new GradebookCollegeStudent("King", "Ahn",
                "king@test.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

        // "/" ?????? ????????? ?????? ???????????? 200??? ???????????????.
        // "/" ????????? GradebookController??? getStudents()??? ???????????? ?????????.
        // getStudents()??? ???????????? ?????? index ???!
        // ????????? collegeStudentList ???????????? ????????? ??????
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView view = mvcResult.getModelAndView();
        
        // ??????
        // getStudents() ???????????? ???????????? ???????????? "index" ??? ??????
        assertViewName(view, "index");

    }

    @Test
    public void createStudentHttpRequest() throws Exception{

        CollegeStudent studentOne = new GradebookCollegeStudent("Juhong", "Ahn",
                "test@test.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

        // post ???????????? "/" ????????? JSON ???????????? ?????? ?????????????????? ??????.
        // ????????? 405?????? ?????????, Controller??? post????????? ?????? -> TDD! ?????? ?????????!
        MvcResult mvcResult = this.mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstname", request.getParameterValues("firstname"))
                        .param("lastname", request.getParameterValues("lastname"))
                        .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        
        assertViewName(modelAndView, "index");

        // ???????????? ????????? assert ?????? ?????? ??????????????? ???????????? ??????, ??? ????????? ??? ???!
        CollegeStudent verifyStudent = studentDao.findByEmailAddress("k1ng@test.com");
        assertNotNull(verifyStudent, "Student should be found!!");
    }

    @Test
    public void deleteStudentHttpRequest() throws Exception{
        assertTrue(studentDao.findById(1).isPresent());

        MvcResult mvcResult = mockMvc.perform(get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertViewName(modelAndView, "index");

        // ?????????????????? ????????? ????????? ??????!
        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    public void deleteStudentHttpRequestErrorPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        assertViewName(modelAndView, "error");
    }
}
