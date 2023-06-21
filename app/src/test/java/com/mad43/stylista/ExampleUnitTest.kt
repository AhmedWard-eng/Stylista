package com.mad43.stylista

import com.mad43.stylista.domain.remote.isValid
import org.junit.Test

import org.junit.Assert.*
import java.util.Calendar

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkTime()
    {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH,20)
        assertTrue(isValid("2023-06-22T20:00:00-04:00"))
    }
}