package amb.aoc2020

import org.junit.jupiter.api.Test
import java.util.stream.IntStream.range
import kotlin.streams.toList

class Spot(val isSeat: Boolean, var isOccupied: Boolean) {
    override fun toString(): String {
        return if (!isSeat) "." else if (isOccupied) "#" else "L"
    }
}

typealias SpotMap = List<List<Spot>>

class Day11 : TestBase() {

    private fun getMap(file: String): SpotMap {
        val input = getTestData(file)
        return input.map { line ->
            line.map { char ->
                Spot(char != '.', char == '#')
            }
        }.filter { it.isNotEmpty() }
    }

    private fun printMap(map: SpotMap) {
        println()
        map.forEach { line ->
            line.forEach { spot ->
                print(spot.toString())
            }
            println()
        }
    }

    private fun getMapDifference(map1: SpotMap, map2: SpotMap): Int {
        return map1.mapIndexed { x, row ->
            row.mapIndexed { y, spot ->
                if (spot.isOccupied != map2[x][y].isOccupied) 1 else 0
            }.sum()
        }.sum()
    }

    private fun getOccupiedSeats(map: SpotMap): Int {
        return map.map { row ->
            row.map { spot ->
                if (spot.isOccupied) 1 else 0
            }.sum()
        }.sum()
    }

    private fun getNeighbours(x: Int, y: Int, xSize: Int, ySize: Int): Sequence<Pair<Int, Int>> {
        return sequence {
            if (x > 0) yield(Pair(x - 1, y))
            if (x > 0 && y > 0) yield(Pair(x - 1, y - 1))
            if (y > 0) yield(Pair(x, y - 1))
            if (y > 0 && x < xSize - 1) yield(Pair(x + 1, y - 1))
            if (x < xSize - 1) yield(Pair(x + 1, y))
            if (x < xSize - 1 && y < ySize - 1) yield(Pair(x + 1, y + 1))
            if (y < ySize - 1) yield(Pair(x, y + 1))
            if (x > 0 && y < ySize - 1) yield(Pair(x - 1, y + 1))
        }
    }

    private fun getNeighbours2(x: Int, y: Int, xSize: Int, ySize: Int): Sequence<List<Pair<Int, Int>>> {
        return sequence {
            val minusXRange = range(0,x).toList().reversed()
            val minusYRange = range(0,y).toList().reversed()
            val xRange = range(x,xSize).toList() - x
            val yRange = range(y,ySize).toList() - y

            yield(minusXRange.map { Pair(it,y) })
            yield(minusXRange.zip(minusYRange).map { Pair(it.first,it.second) })
            yield(minusYRange.map{Pair(x,it)})
            yield(xRange.zip(minusYRange).map { Pair(it.first,it.second) })
            yield(xRange.map{Pair(it,y)})
            yield(xRange.zip(yRange).map { Pair(it.first,it.second) })
            yield(yRange.map{Pair(x,it)})
            yield(minusXRange.zip(yRange).map{Pair(it.first,it.second)})
        }
    }

    private fun getOccupiedInSight(spots: List<Spot>):Int{
        val firstOccupied  = spots.indexOfFirst { it.isSeat && it.isOccupied }
        var firstFree = spots.indexOfFirst { it.isSeat && !it.isOccupied }
        if(firstFree == -1) firstFree = Int.MAX_VALUE

        return if(firstOccupied in 0 until firstFree) 1 else 0
    }

    private fun getOccupiedInSight(map: SpotMap, x: Int, y: Int) : Int{
        val xSize = map.count()
        val ySize = map[x].count()
        val neighbours = getNeighbours2(x,y,xSize,ySize)
        return neighbours.map { lane ->
            getOccupiedInSight(lane.map { map[it.first][it.second] })
        }.sum()
    }

    private fun playRound(map: SpotMap): SpotMap {
        return map.mapIndexed { x, row ->
            row.mapIndexed { y, spot ->
                var newSpot = Spot(spot.isSeat, spot.isOccupied)
                if (spot.isSeat) {
                    val neighbours = getNeighbours(x, y, map.count(), row.count())
                    val numOccupied = neighbours.count { map[it.first][it.second].isOccupied }
                    if (spot.isOccupied && (numOccupied >= 4))
                        newSpot.isOccupied = false
                    if (!spot.isOccupied && (numOccupied == 0))
                        newSpot.isOccupied = true
                }
                newSpot
            }
        }
    }

    private fun playRound2(map: SpotMap): SpotMap {
        return map.mapIndexed { x, row ->
            row.mapIndexed { y, spot ->
                var newSpot = Spot(spot.isSeat, spot.isOccupied)
                if (spot.isSeat) {
                    val numOccupied = getOccupiedInSight(map,x,y)
                    if (spot.isOccupied && (numOccupied >= 5))
                        newSpot.isOccupied = false
                    if (!spot.isOccupied && (numOccupied == 0))
                        newSpot.isOccupied = true
                }
                newSpot
            }
        }
    }

    @Test
    fun task1() {
        var map = getMap("input11")
        var difference = 1
        var round = 0


        while (difference > 0) {
            round++
            printMap(map)
            var newMap = playRound(map)
            difference = getMapDifference(map, newMap)
            map = newMap
        }

        logger.info { "game ended after round $round with ${getOccupiedSeats(map)} occupied seats" }
    }

    @Test
    fun task2() {

        var map = getMap("input11")
        logger.info { getOccupiedInSight(listOf(Spot(false,false),Spot(true,true))).toString()}

        var difference = 1
        var round = 0
        while(difference > 0){
            round++
            //printMap(map)
            var newMap = playRound2(map)
            difference = getMapDifference(map,newMap)
            map = newMap
        }
        logger.info { "game ended after round $round with ${getOccupiedSeats(map)} occupied seats" }

    }


}
