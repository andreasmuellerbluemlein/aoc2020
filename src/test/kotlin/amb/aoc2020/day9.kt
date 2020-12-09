package amb.aoc2020

import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class Day9 : TestBase() {
    companion object {
        const val preambleSize = 25
    }

    fun getInput(file: String): List<Long> =
        getTestData(file).filter{it.isNotEmpty()}.map{it.toLong()}

    fun isValid(list: List<Long>, index : Int) : Boolean{
        val value = list[index]
        val sublist = list.subList(index - preambleSize,index)
        sublist.forEach { x ->
            sublist.forEach{ y ->
                if(value == x + y) return true
            }
        }
        return false
    }

    @Test
    fun task1() {
        val input = getInput("input9")

        for (index in preambleSize..input.count()){
            if(!isValid(input, index)){
                logger.info { "input index $index with value ${input[index]} is invalid" }
                return
            }
        }
    }

    fun findSumSet(list: List<Long>, startIndex : Int, targetValue: Long) : List<Long>?{
        var sum = 0L
        for (index in startIndex until list.count()){
            sum += list[index]
            if(sum == targetValue){
                logger.info { "found sublist from $startIndex to $index summing up to $targetValue" }
                return list.subList(startIndex,index)}
            else if(sum > targetValue) {
                logger.info { "no sublist from $startIndex found summing up to $targetValue" }

                return null}
        }
        return null
    }

    @Test
    fun task2() {
        val input = getInput("input9")

        var invalidValue = 104054607L
        var invalidValue2 = 127L
        input.forEachIndexed{ index, _ ->
            val sumSetCandidate = findSumSet(input, index,invalidValue)
            if(sumSetCandidate != null)
            {
                val min = sumSetCandidate.minOrNull()
                val max = sumSetCandidate.maxOrNull()
                logger.info { "min $min and max $max sum up to ${min!!.plus(max!!)}" }
                return
            }
        }

    }
}
