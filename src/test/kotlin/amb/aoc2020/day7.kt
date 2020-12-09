package amb.aoc2020

import org.junit.jupiter.api.Test

class Day7 : TestBase() {
    companion object {

    }

    fun loadRegulations(inputFile: String) : Map<String,List<Pair<String,Int>>>{
        val data = getTestData(inputFile).map { line ->
            line.split(" bags contain ", " bags, ", " bag, "," bags."," bag.","no other")
        }
        return data.map{ regulation ->
            val innerBags = regulation.filter { it.isNotEmpty() }.drop(1).map {
                Pair(it.drop(2),Character.getNumericValue(it.first()))
            }
            Pair(regulation[0], innerBags)
        }.toMap()
    }

    fun findOuterBags(regulations: Map<String,List<Pair<String,Int>>>, bag: String) : List<String>
    {
        return regulations.filter { regulation ->  regulation.value.any{it.first == bag} }.map { it.key }
    }



    fun getCountOfReferencingRegulations(regulations: Map<String,List<Pair<String,Int>>>, bag: String) : Int{
        if (skipThose.contains(bag)) return 0
        val outerBags = findOuterBags(regulations,bag)
        logger.info { "$bag - ${outerBags.joinToString(", ")}"}
        skipThose.add(bag)
        return 1 + outerBags.map { getCountOfReferencingRegulations(regulations,it) }.sum()
    }

    private val skipThose = emptyList<String>().toMutableList()
    @Test
    fun task1() {
        val regulations = loadRegulations("input7")

        val countOfReferencingRegulations = getCountOfReferencingRegulations(regulations,"shiny gold")

        logger.info { "${countOfReferencingRegulations-1} are referencing shiny gold bag" }
    }

    fun getCountOfIncludingPackages(regulations: Map<String,List<Pair<String,Int>>>, bag: String) : Int {

        return 1 + regulations[bag]!!.map { getCountOfIncludingPackages(regulations,it.first) * it.second }.sum()

    }

    @Test
    fun task2() {
        val regulations = loadRegulations("input7")

        var count = getCountOfIncludingPackages(regulations,"shiny gold")

        logger.info { "${count - 1} are referencing shiny gold bag" }
    }
}
