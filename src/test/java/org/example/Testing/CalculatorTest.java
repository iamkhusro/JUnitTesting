package org.example.Testing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


@TestInstance(TestInstance.Lifecycle.PER_METHOD) //which is usually the default, new instance of CalculatorTest gets created for every @Test
class CalculatorTest {

    Calculator calculator;
    TestInfo testInfo; //get info about the running test and use it
    TestReporter testReporter; //use to print on the JUnit console

    @BeforeAll
    @DisplayName("Calculator Testing")
    static void start() {
        System.out.println("Testing calculator");
    }

    @BeforeEach
    void init(TestInfo testInfo, TestReporter testReporter) {
        //dependency injecting
        this.testInfo = testInfo;
        this.testReporter = testReporter;


        calculator = new Calculator();
        testReporter.publishEntry("Running test " + testInfo.getDisplayName() + " with tag " + testInfo.getTags());

    }

    @RepeatedTest(3) //can be used to run the same test multiple times
    @DisplayName("Add numbers")
    @Tag("Ungrouped") //we can use tags and run only specific tests based on their tags by adding configuration (in Intellij idea or eclipse)
    void addNumbers(RepetitionInfo info) {
//        testReporter.publishEntry("Running test " + testInfo.getDisplayName() + " with tag " + testInfo.getTags());
        assumeTrue(true); //some condition, proceed only when condition is true
        int expected = 3;
        int actual = calculator.add(1,2);
        if (info.getCurrentRepetition() == 2) {
            assertEquals(3, calculator.add(2,1), "Adding numbers");
        }
        else {
            assertEquals(expected, actual, () -> "Should add the numbers and return " + expected + " but returned " + actual + "."); // there can be some expensive strings to calculate
        }                                                                                                                         // we can optimise by using lambdas to tell JUnit to generate the string only when test fails
    }

    @Test
    @DisplayName("Subtract numbers")
//    @Disabled
//    @EnabledOnOs(OS.LINUX)
    @Tag("Ungrouped")
    void subtractNumbers()
    {
//        assertEquals(-1, calculator.subtract(1, 2), "Should subtract the numbers.");
        assertAll(
                () -> assertEquals(-1, calculator.subtract(1, 2)),
                () -> assertEquals(-6, calculator.subtract(-1, 5))
        );
    }

    @Test
    @DisplayName("Divide numbers and get int value")
    @Tag("Ungrouped")
    void divide() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1,0), "Division by zero");
    }



    @Nested
    @DisplayName("Multiply numbers") //if any of the tests fail, multiply numbers will fail
    @Tag("Grouped")
    class Multiply {
        @Test
        @DisplayName("Multiply +ve numbers and get int value")
        void multiplyPos() {
            assertEquals(20, calculator.multiply(4, 5), "Multiply +ve numbers");
        }

        @Test
        @DisplayName("Multiply -ve numbers and get int value")
        void multiplyNeg() {
            assertEquals(20, calculator.multiply(-4, -5), "Multiply -ve numbers");
        }
    }



}