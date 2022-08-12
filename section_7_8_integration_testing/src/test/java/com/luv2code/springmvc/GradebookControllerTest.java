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

    //@BeforeAll 애너테이션은 static메서드어야 함.(스태틱 요소들만 다른 요소들보다 빨리 초기화됌)
    @BeforeAll
    public static void setup() {
        // 가짜 서블릿 요청을 만들기위해 (쿼리스트링으로 학생 이름을 보내도록)
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

        // "/" 으로 요청이 올때 상태코드 200이 나오도록함.
        // "/" 요청은 GradebookController의 getStudents()를 호출하게 돼있음.
        // getStudents()가 반환하는 뷰는 index 임!
        // 모델에 collegeStudentList 이터러블 타입을 보유
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView view = mvcResult.getModelAndView();
        
        // 체크
        // getStudents() 메서드가 반환하는 뷰네임이 "index" 를 확인
        assertViewName(view, "index");

    }

    @Test
    public void createStudentHttpRequest() throws Exception{

        CollegeStudent studentOne = new GradebookCollegeStudent("Juhong", "Ahn",
                "test@test.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne));

        when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

        // post 메서드로 "/" 경로에 JSON 타입으로 해당 파라미터들을 보냄.
        // 지금은 405코드 반환됨, Controller에 post매핑이 없음 -> TDD! 이제 만들자!
        MvcResult mvcResult = this.mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstname", request.getParameterValues("firstname"))
                        .param("lastname", request.getParameterValues("lastname"))
                        .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        
        assertViewName(modelAndView, "index");

        // 저장까지 되는가 assert 하기 아직 컨트롤러는 아무것도 안함, 뷰 반환만 할 뿐!
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

        // 삭제했으므로 없어야 테스트 통과!
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
