package com.marcnf

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    @DisplayName("Dummy test")
    fun dummyTest() {
        // GIVEN
        val a = 2
        val b = 2
        val expectedResult = 4

        // WHEN
        val result = a + b

        // THEN
        assertEquals(expectedResult, result)
    }
}