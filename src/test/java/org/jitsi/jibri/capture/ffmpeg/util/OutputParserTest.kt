package org.jitsi.jibri.capture.ffmpeg.util

import org.testng.annotations.Test

import org.testng.Assert.*
import org.testng.annotations.BeforeTest

class OutputParserTest {
    lateinit var parser: OutputParser

    @BeforeTest
    fun setUp() {
        parser = OutputParser()
    }

    @Test
    fun `test basic parse`() {
        val outputLine = "frame=   95 fps= 31 q=27.0 size=     584kB time=00:00:03.60 bitrate=1329.4kbits/s speed=1.19x"
        val expectedValues = mapOf(
            "frame" to "95",
            "fps" to "31",
            "q" to "27.0",
            "size" to "584kB",
            "time" to "00:00:03.60",
            "bitrate" to "1329.4kbits/s",
            "speed" to "1.19x"
        )

        val result = parser.parse(outputLine)

        assertEquals(result.size, expectedValues.size)
        expectedValues.forEach {(field, value) ->
            assertTrue(result.contains(field))
            assertEquals(result[field], value)
        }
    }

    @Test
    fun `test failed parse`() {
        val outputLine = "wrong line"
        val result = parser.parse(outputLine)

        assertEquals(0, result.size)
    }
}