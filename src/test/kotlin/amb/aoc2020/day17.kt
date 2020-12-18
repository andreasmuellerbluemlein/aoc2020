package amb.aoc2020

import org.junit.jupiter.api.Test

class Day17 : TestBase() {

    fun getInput(file: String): HyperCube {
        return getTestData(file).filter { it.isNotEmpty() }.flatMapIndexed { x, line ->
            line.mapIndexed { y, value ->
                Pair(Quadrupel(x, y, 0,0), value == '#')
            }.filter { it.second }
        }.toMap()
    }


    private fun runCycle(input: HyperCube) : HyperCube {
        val xMin = input.keys.minOf { it.first }
        val yMin = input.keys.minOf { it.second }
        val zMin = input.keys.minOf { it.third }
        val wMin = input.keys.minOf { it.fourth }
        val xMax = input.keys.maxOf { it.first }
        val yMax = input.keys.maxOf { it.second }
        val zMax = input.keys.maxOf { it.third }
        val wMax = input.keys.maxOf { it.fourth }

        val newActives = emptyList<Quadrupel<Int>>().toMutableList()

        for (x in xMin - 1..xMax + 1) {
            for (y in yMin - 1..yMax + 1) {
                for (z in zMin - 1..zMax + 1) {
                    for (w in wMin - 1..wMax + 1) {
                        val pos = Quadrupel(x, y, z,w)
                        val oldCubePos = input[pos]
                        val iAmActive = (oldCubePos != null && oldCubePos)

                        val actives = input.filter {
                            (kotlin.math.abs(x - it.key.first) <= 1
                                    && kotlin.math.abs(y - it.key.second) <= 1
                                    && kotlin.math.abs(z - it.key.third) <= 1
                                    && kotlin.math.abs(w - it.key.fourth) <= 1)
                        }.values.sumBy { if (it) 1 else 0 }

                        if (iAmActive && (actives == 3 || actives == 4)) newActives.add(pos)
                        if (!iAmActive && actives == 3) newActives.add(pos)
                    }
                }
            }
        }

        val result = newActives.map { Pair(it, true) }.toMap()
        return result
    }

    @Test
    fun task1() {
        var cube = getInput("input17")

        for (round in 1..6) {
            cube = runCycle(cube)
        }

        val sum = cube.values.sumBy { if (it) 1 else 0 }
        logger.info { "$sum active cells" }

    }

    @Test
    fun task2() {
    }
}

typealias Cube = Map<Triple<Int, Int, Int>, Boolean>
typealias HyperCube = Map<Quadrupel<Int>, Boolean>

data class Quadrupel<out A>(
    val first: A,
    val second: A,
    val third: A,
    val fourth: A
)
