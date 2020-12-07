package amb.aoc2020

import org.junit.jupiter.api.Test

class Day5 : TestBase() {
    companion object {
        val startIndices = arrayOf(
            Pair('B', 8 * 64),
            Pair('B', 8 * 32),
            Pair('B', 8 * 16),
            Pair('B', 8 * 8),
            Pair('B', 8 * 4),
            Pair('B', 8 * 2),
            Pair('B', 8 * 1),
            Pair('R', 4),
            Pair('R', 2),
            Pair('R', 1)
        )
    }

    private fun calcSeatId(code: String): Int {
        val offsets = code.mapIndexed { i, char ->
            if (startIndices[i].first == char) startIndices[i].second else 0
        }
        logger.info { offsets.joinToString(",") { it.toString() } }
        return offsets.sum()
    }


    private fun getSeats(): List<Int> {
        return getTestData("input5").map {
            calcSeatId(it)
        }
    }

    @Test
    fun task1() {
        logger.info { "max seat id ${getSeats().max()!!}" }
    }

    @Test
    fun task2() {
        val sortedSeats = getSeats().sorted()
        for (i in 1..sortedSeats.count() - 2) {
            if (sortedSeats[i] + 1 != sortedSeats[i + 1]) {
                logger.info { "your seat id ${sortedSeats[i] + 1}" }

            }
        }

    }


}
