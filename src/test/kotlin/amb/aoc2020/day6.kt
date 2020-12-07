package amb.aoc2020

import org.junit.jupiter.api.Test

class Day6 : TestBase() {
    companion object {

    }

    fun getInput(): List<String> {
        val list = emptyList<String>().toMutableList()
        var value = ""
        getTestData("input6").forEach {
            if (it.isEmpty()) {
                list.add(value)
                value = ""
            } else value += it
        }
        return list
    }

    fun getInput2(): List<String> {
        val list = emptyList<String>().toMutableList()
        var justAdded = true
        var value = ""
        getTestData("input6").forEach {
            if (it.isEmpty()) {
                list.add(value)
                value = ""
                justAdded = true
            } else {
                if (justAdded) value = it
                else {
                    value.forEach { presentChar ->
                        if (!it.contains(presentChar)) {
                            value = value.replace(presentChar.toString(), "")
                        }
                    }
                }
                justAdded = false
            }
        }
        return list
    }

    @Test
    fun task1() {
        val distinct = getInput().map { element ->
            element.map { it }.distinct()
        }
        val value = distinct.sumOf { it.count() }

        logger.info { distinct.map { it }.joinToString(",") }
        logger.info { "sum is $value" }
    }

    @Test
    fun task2() {
        val distinct = getInput2().map { element ->
            element.map { it }.distinct()
        }
        val value = distinct.sumOf { it.count() }

        logger.info { distinct.map { it }.joinToString(",") }
        logger.info { "sum is $value" }
    }
}
