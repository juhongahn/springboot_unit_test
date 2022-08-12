package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.lang.reflect.Executable;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach() {
        this.demoUtils =  new DemoUtils();
        System.out.println("@BeforeEach executes before the execution of each test method");
    }

    @Test
    @DisplayName("Equals And NotEquals")
    void testEqualsAndNotEquals() {

        assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9), "1+9 must be 10");
    }

    @Test
    @DisplayName("Multiply")
    void testMultiply() {

        assertEquals(8, demoUtils.multiply(2, 4), "2*4 must be 8");
        assertNotEquals(2, demoUtils.add(1, 9), "1*9 must not be 2");
    }

    @Test
    @DisplayName("Null And NotNull")
    void testNullAndNotNull() {
        
        String str1 = null;
        String str2 = "luv2code";

        assertNull(demoUtils.checkNull(str1), "Object should be null");
        assertNotNull(demoUtils.checkNull(str2), "Object should not be null");

    }

    @Test
    @DisplayName("Same and Not same")
    void testSameNotSame() {

        String str = "Luv2Code";

        assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(),
                "Object should refer to same object");

        // String str = new String("Luv2Code Academy") 로 다른 주소를 가르키고
        // value는 동일한 두 객체의 NotSame 테스트가 통과한다 -> 값을 비교하는(동등성) 연산이다.
        assertNotSame(str, demoUtils.getAcademy(),
                "Object should not refer to same object");

    }

    @DisplayName("True and False")
    @Test
    void testTrueFalse() {
        int gradeOne = 10;
        int gradeTwo = 5;

        assertTrue(demoUtils.isGreater(gradeOne, gradeTwo), "This should return true");
        assertFalse(demoUtils.isGreater(gradeTwo, gradeOne), "This should return false");
    }


    @DisplayName("Array equals")
    @Test
    void testArrayEquals() {
        String[] stringArray = {"A", "B", "C"};

        // 기본적으로 assert... 들은 동등성 검사를 한다.
        assertArrayEquals(stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Array should be the same");
    }

    @DisplayName("Iterable equals")
    @Test
    void testIterableEquals() {
        List<String> theList = List.of("luv", "2", "code");
        // List는 Collection 을 상속받는데, Collection은 Iterable 인터페이스를 상속한다. 때문에 아래처럼
        // List를 assertIterableEquals 로 비교할 수 있다.

        assertIterableEquals(theList, demoUtils.getAcademyInList(), "Expected list should be same as" +
                "actual list");
    }

    @DisplayName("Line match")
    @Test
    void testLineMatch() {
        List<String> theList = List.of("luv", "2", "code");
        // List는 Collection 을 상속받는데, Collection은 Iterable 인터페이스를 상속한다. 때문에 아래처럼
        // List를 assertIterableEquals 로 비교할 수 있다.


        // 정규식도 가능하네 \d+ => 하나 혹은 그 이상 연결된 숫자
        // 11은 \d+ 와 일치하므로 라인테스트 통과
        List<String> expected = asList("Java", "\\d+", "JUnit");
        List<String> actual = asList("Java", "11", "JUnit");
        
        assertLinesMatch(expected, actual, "Lines should match");
        assertLinesMatch(theList, demoUtils.getAcademyInList(), "Lines should match");
    }

    @DisplayName("Throws and Does Not Throw")
    @Test
    void testThrowsAndDoesNotThrow() {
        assertThrows(InvalidParameterException.class, () -> demoUtils.throwException(-1),
                "Should throw exception");
        assertDoesNotThrow(() -> demoUtils.throwException(6), "Should not throw exception");
    }

    @DisplayName("Timeout")
    @Test
    void testTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), ()->demoUtils.checkTimeout(),
                "Method should execute in 3 sec");
    }

    /*
        @AfterEach
        void tearDownAfterEach(){
            System.out.println("Running @AfterEach");
            System.out.println();
        }

        @BeforeAll
        static void setupBeforeEachClass() {
            System.out.println("@BeforeAll executes only once before all test methods execution in the class");
        }

        @AfterAll
        static void tearDownAfterEachClass() {
            System.out.println("@AfterAll executes only once after all test methods execution in the class");
        }
    */

}
