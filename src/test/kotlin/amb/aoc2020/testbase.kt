package amb.aoc2020

import org.junit.platform.commons.logging.LoggerFactory

open class TestBase{
    val logger = LoggerFactory.getLogger(Day1::class.java)

    fun getTestData(filename: String) : List<String>{
        return javaClass.getResource("/$filename").readText().lines()
    }
}
