package amb.aoc2020

import org.junit.jupiter.api.Test

class Day3 : TestBase(){

    fun getInput() : List<List<Boolean>>{
        val input = getTestData("input3")
        return input.map { line ->
            line.map { char -> char == '#' }
        }.filter{ it.isNotEmpty() }
    }

    @Test
    fun task1(){
        logger.info{"you hit trees ${numberOfTreesHit(3,1)} times"}
    }

    @Test
    fun task2(){
        val inputOffsets = listOf(Pair(1,1),Pair(3,1),Pair(5,1),Pair(7,1),Pair(1,2))
        val counts = inputOffsets.map {
            val count = numberOfTreesHit(it.first,it.second).toLong()
            logger.info{"sub - you hit trees $count times"}
            count
        }
        val mult = counts.reduce { acc, i -> acc * i }
        logger.info{"you hit trees $mult times"}

    }

    private fun numberOfTreesHit(rightOffset: Int, topOffset: Int): Int{
        val map = getInput()
        var currentRight = 0
        var currentTop = 0
        var countOfTrees = 0
        while (currentTop < map.count())
        {
            if(map[currentTop][currentRight]) countOfTrees ++
            currentRight += rightOffset
            if(currentRight >= map[currentTop].count()) currentRight -= map[currentTop].count()
            currentTop += topOffset
        }
        return countOfTrees
    }

}
