package amb.aoc2020

import org.junit.jupiter.api.Test
import java.lang.Math.abs
import java.lang.RuntimeException

class Day12 : TestBase() {
    companion object {
        val lTurns = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1), Pair(1, 0))
        val rTurns = lTurns.reversed()
    }

    class Instruction(
        var type: Char,
        var num: Int
    ) {
        companion object {
            final fun parse(line: String): Instruction {
                val newLine = line
                    .replace("L180", "T0")
                    .replace("R180", "T0")
                    .replace("L270", "R90")
                    .replace("R270", "L90")
                return Instruction(
                    newLine[0],
                    newLine.drop(1).toInt()
                )
            }
        }

    }


    class Ship(var x: Int, var y: Int, var direction: Pair<Int, Int>) {
        fun followInstruction(ins: Instruction) {
            when (ins.type) {
                'N' -> direction = Pair(direction.first,direction.second + ins.num)
                'E' -> direction = Pair(direction.first + ins.num,direction.second)
                'S' -> direction = Pair(direction.first,direction.second - ins.num)
                'W' -> direction = Pair(direction.first - ins.num,direction.second)
                'T' ->
                    direction = Pair(-direction.first, -direction.second)
                'L' -> {
                    direction = Pair(-direction.second, direction.first)
                }
                'R' -> {
                    direction = Pair(direction.second,-direction.first)
                }
                'F' -> {
                    x += (ins.num * direction.first)
                    y += (ins.num * direction.second)
                }

            }
        }


    }

    fun getInput(file: String): List<Instruction> =
        getTestData(file).filter { it.isNotEmpty() }.map {
            Instruction.parse(it)
        }

    @Test
    fun task1() {
        val ship = Ship(0, 0, Pair(10, 1))
        getInput("input12").forEach {
            ship.followInstruction(it)
            logger.info { "ship is at ${ship.x} and ${ship.y}" }
        }
        logger.info {
            "ship is at ${ship.x} and ${ship.y} and manhattan distance ${
                kotlin.math.abs(ship.x) + kotlin.math.abs(
                    ship.y
                )
            }"
        }

    }


    @Test
    fun task2() {


    }
}
