package amb.aoc2020

import org.junit.jupiter.api.Test

class Day10 : TestBase() {
    companion object {
        val knownCounts = emptyMap<List<Int>,Long>().toMutableMap()
    }

    fun getInput(file: String): List<Int> =
        getTestData(file).filter{it.isNotEmpty()}.map{it.toInt()}

    @Test
    fun task1() {
        val input = getInput("input10-example-simple") + 0
        val sorted = input.sorted()
        val diffs = mutableMapOf( 1 to 0, 2 to 0, 3 to 1) // +3 from last adapter
        sorted.reduce{acc, value ->
            diffs[value-acc] =  diffs[value-acc]!! + 1
            value
        }
        logger.info { "$diffs" }
        logger.info { "result is ${diffs[1]!! * diffs[3]!!}" }
    }

    private fun getCombinationsCount(list: List<Int>) : Long{
        knownCounts[list].let { if (it != null) return it }
        if(list.count()==1) return 1
        val value = list.first()
        val result = list.take(4).mapIndexed { index, element ->
            val diff = kotlin.math.abs(value - element)
            if(diff in 1..3) getCombinationsCount(list.drop(index))
            else 0
        }.sum()
        knownCounts[list] = result
        return result
    }

    @Test
    fun task2() {


        val input = getInput("input10") + 0
        val sorted = input.sorted()
        val combinations = getCombinationsCount(sorted)

        logger.info { "number of combinations: $combinations" }
    }
}
