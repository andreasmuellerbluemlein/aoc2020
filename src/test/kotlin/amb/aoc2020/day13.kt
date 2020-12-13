package amb.aoc2020

import org.junit.jupiter.api.Test

class Day13 : TestBase() {
    companion object {
    }

    fun getInput(file: String): Pair<Int, List<Int>> {
        val time = getTestData(file).first().toInt()
        val busses = getTestData(file).drop(1).first().split(",").filter { it != "x" }.map { it.toInt() }
        return Pair(time, busses)
    }

    fun getEarliestDeparture(time: Int, bus: Int): Int {
        val t = bus * (time / bus)
        return if (time == t) t else t + bus
    }

    @Test
    fun task1() {
        val input = getInput("input13")
        val time = input.first
        val busses = input.second

        val earliest = busses
            .map { getEarliestDeparture(time, it) }
            .withIndex()
            .minByOrNull { (_, f) -> f }!!
        val busLine = busses[earliest.index]
        val result = (earliest.value - time) * busLine

        logger.info { "earliest line $busLine at ${earliest.value} -> $result" }

    }


    private fun getMatch(vararg busses: Pair<Long,Long>) : Pair<Long,Long>{
        val offset = busses.maxByOrNull { it.first }!!
        var start = -offset.second

        var first : Long? = null
        while (true) {
            val t = busses.map { (start + it.second) % it.first}
            if (t.all { it == 0L }) {
                if(first == null) first = start
                else return Pair(start-first, -first)
            }
            start += offset.first
        }
    }

    @Test
    fun subTask(){
        val result = getMatch(Pair(67,0),Pair(7,1),Pair(59,2),Pair(61,3))

        logger.info { "Match: ${result.first} and ${result.second}" }
    }

    @Test
    fun task2() {
        var busses = getTestData("input13")[1]
            .split(",")
            .withIndex().mapNotNull {
                if (it.value == "x") null
                else Pair(it.value.toLong(), it.index.toLong())
            }

        logger.info { busses.toString() }

        while (busses.count()>1) {
            val result = getMatch(busses[0],busses[1])
            busses = busses.drop(2) + result // replace first two element with its result
        }
        logger.info { busses.toString() }
    }
}
