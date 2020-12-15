package amb.aoc2020

import org.junit.jupiter.api.Test

class Day15 : TestBase() {

    private fun getNextNumber(oldNumbers: MutableMap<Int,Int>, lastNumber: Int, round: Int): Int {
        val oldRound = oldNumbers[lastNumber]
        try{
        return if (oldRound == null) 0
        else {
            return round - oldRound -1
        }
        }finally {
            oldNumbers[lastNumber] = round - 1
        }
    }

    private fun playMemory(vararg numbers: Int) {
        val map = numbers.dropLast(1).mapIndexed { index, i -> Pair(i, index+1) }.toMap().toMutableMap()
        var round = numbers.count()+1
        var lastNumber = numbers.last()
        while (round < 30000001) {
            lastNumber = getNextNumber(map,lastNumber,round)
            if (round % 10000 == 0) {
                logger.info { "${round-1}. element = $lastNumber" }
                logger.info { "map is of size ${map.count()}" }
            }
            round++
        }
        logger.info { "${round-1}. element = $lastNumber" }
        logger.info { "map is of size ${map.count()}" }
    }



    @Test
    fun task1() {
        playMemory(13,0,10,12,1,5,8)
    }

    @Test
    fun task2() {

    }
}
