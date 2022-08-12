package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

//    @MockBean 대체
//    @Mock
//    private ApplicationDao applicationDao;
//
//    @InjectMocks
//    private ApplicationService applicationService;

    @MockBean
    private ApplicationDao applicationDao;

    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        studentOne.setFirstname("Juhong");
        studentOne.setLastname("An");
        studentOne.setEmailAddress("test@test.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When & Verify")
    @Test
    public void assertEqualsTestAddGrades(){
        when(applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()))
                .thenReturn(100.0);

        // 셋업해둔 applicationDao의 addGradeResultsForSingleClass() 메서드가 100.0을 반환한다.
        assertEquals(100, applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()));

        // applicationDao의 addGradeResultsForSingleClass() 메서드가 호출됐는지 확인.
        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

        // applicationDao의 addGradeResultsForSingleClass() 메서드가 몇번 호출됐는지 확인
        // Mock객체는 메서드 호출 횟수를 감지하고 있다.
        verify(applicationDao, times(1)).addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults());
    }

    @DisplayName("Find Gpa")
    @Test
    public void assertEqualsTestFindGpa() {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);

        assertEquals(88.31, applicationService.findGradePointAverage(
                studentOne.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Not Null")
    @Test
    public void testAssertNotNull() {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);

        assertNotNull(applicationService.checkNull(studentOne.getStudentGrades().getMathGradeResults())
        ,"Object should not be null");
    }

    @DisplayName("Throw runtime exception")
    @Test
    public void throwRuntimeException() {
        CollegeStudent nullStudent = (CollegeStudent) applicationContext.getBean("collegeStudent");

        // 테스트 하고자하는 메서드의 파라미터도 셋업해둔 목객체의 메서드 파라미터와 같아야하는구나!
        // 단발적인 예외를 던지려면 doThrow로 셋업한다.
        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class, ()-> applicationService.checkNull(nullStudent));

        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @DisplayName("Multiple Stubbing")
    @Test
    public void stubbingConsecutiveCalls() {
        CollegeStudent nullStudent = (CollegeStudent) applicationContext.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                        .thenReturn("Do not throw exception second time");

        assertThrows(RuntimeException.class, ()-> applicationService.checkNull(nullStudent));
        assertEquals("Do not throw exception second time", applicationService.checkNull(nullStudent));

        // 메서드는 두번 호출된다.
        verify(applicationDao, times(2)).checkNull(nullStudent);
    }

}
