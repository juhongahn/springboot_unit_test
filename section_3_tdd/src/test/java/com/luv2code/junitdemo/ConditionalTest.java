package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

class ConditionalTest {

    @Test
    @Disabled("Don't run until JIRA #123 is resolved")
    void basicTest() {

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindowOnly() {

    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testForMacOnly() {

    }

    @Test
    @EnabledOnOs({OS.LINUX})
    void testForLinuxOnly() {

    }

    @Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void testForMacAndWindowOnly() {
        // OR 연산.
    }

    @Test
    @EnabledOnJre(JRE.JAVA_18)
    void testForOnlyForJava18() {

    }

    @Test
    @EnabledOnJre(JRE.JAVA_13)
    void testForOnlyForJava13() {

    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_13, max=JRE.JAVA_18)
    void testForJavaRange() {

    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_11)
    void testForJavaRangeMin() {

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "LUV2CODE_ENV", matches = "DEV")
    void testOnlyForDevEnvironment() {

    }

    @Test
    @EnabledIfSystemProperty(named = "LUV2CODE_SYS_PROP", matches = "CI_CD_DEPLOY")
    void testOnlyForSystemProperty() {

    }
}
