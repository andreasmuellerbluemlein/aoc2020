package amb.aoc2020

import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class Day8 : TestBase() {
    companion object {
    }

    class Instruction(
         var type: String,
        var num: Int
    ){
        fun toggle(){
            when(type){
                "nop" -> type = "jmp"
                "jmp" -> type = "nop"
            }
        }
    }

    fun getInput(file: String): List<Instruction> =
        getTestData(file).filter{it.isNotEmpty()}.map {

            val sign = if (it[4] == '+' ) 1 else -1
            Instruction(
                type = it.substring(0, 3),
                num = it.substring(5)
                    .toInt() * sign
            )
        }

    @Test
    fun task1() {
        val steps = getInput("input8")
        val pair = tryFinishProgramm( steps)
        var index = pair.first
        var value = pair.second

        logger.info { "Your Value is $value after reaching index $index a second time" }

    }

    private fun tryFinishProgramm(
        steps: List<Instruction>
    ): Pair<Int, Int> {
        var index1 = 0
        var value1 = 0
        val coveredIndices = emptyList<Int>().toMutableList()
        while (index1 < steps.count()) {
            if(coveredIndices.contains(index1)) {
                logger.info { "Your Value is $value1 after reaching index $index1 a second time" }
                throw AOCException()
            }
            coveredIndices.add(index1)
            val ins = steps[index1]
            when (ins.type) {
                "nop" -> index1++
                "acc" -> {
                    index1++; value1 += ins.num
                }
                "jmp" -> index1 += ins.num
            }
        }
        return Pair(index1, value1)
    }

    class AOCException : RuntimeException()

    @Test
    fun task2() {
        val steps = getInput("input8")
        var changeIndex = 0
        while (true)
        {
            steps[changeIndex].toggle()
            try {
                val pair = tryFinishProgramm(steps)
                var value = pair.second
                logger.info { "Your Value is $value after changing index $changeIndex" }
                return
            }
            catch(ex: AOCException){
                logger.info { "Modifying index $changeIndex to ${steps[changeIndex].type} did not make the the program finish" }
                steps[changeIndex].toggle()
                changeIndex++
            }

        }

    }
}
