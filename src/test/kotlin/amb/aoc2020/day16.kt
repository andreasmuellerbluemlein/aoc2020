package amb.aoc2020

import org.junit.jupiter.api.Test

class Day16 : TestBase() {


    data class Input(
        var validValues: Map<String, List<Int>>,
        var myTicket: List<Int>,
        var nearbyTickets: List<List<Int>>
    )

    fun getInput(file: String): Input {
        val validValues = emptyMap<String, List<Int>>().toMutableMap()
        val myTicket = emptyList<Int>().toMutableList()
        val nearbyTickets = emptyList<List<Int>>().toMutableList()

        var section = "rules:"
        getTestData(file).filter { it.isNotEmpty() }.forEach { line ->
            if (line == "your ticket:" || line == "nearby tickets:") section = line
            else
                when (section) {
                    "rules:" -> {
                        val parts = line.split(": ", " or ")
                        validValues[parts[0]] = parts.drop(1).flatMap {
                            val numberStrings = it.split("-")
                            numberStrings[0].toInt()..numberStrings[1].toInt()
                        }
                    }
                    "your ticket:" -> {
                        myTicket.addAll(line.split(",").map { it.toInt() })
                    }
                    "nearby tickets:" -> {
                        nearbyTickets.add(line.split(",").map { it.toInt() })
                    }
                    else -> throw NotImplementedError()
                }
        }
        return Input(validValues, myTicket, nearbyTickets)
    }

    fun findInvalidTicketValues(validValues: Map<String, List<Int>>, ticket: List<Int>): List<Int> {
        return ticket.filter { ticketValue ->
            !validValues.any { rule ->
                rule.value.contains(ticketValue)
            }
        }
    }

    fun isTicketValid(validValues: Map<String, List<Int>>, ticket: List<Int>): Boolean {
        return ticket.all { ticketValue ->
            validValues.any { rule ->
                rule.value.contains(ticketValue)
            }
        }
    }

    @Test
    fun task1() {
        val input = getInput("input16")
        logger.info { input.toString() }

        val sumOfInvalidTicketValues = input.nearbyTickets.flatMap { ticket ->
            findInvalidTicketValues(input.validValues, ticket)
        }.sum()

        logger.info { "sum of invalid ticket values: $sumOfInvalidTicketValues" }

    }

    fun findRuleIndexCandidates(
        validValues: List<Int>,
        validTickets: List<List<Int>>,
        assignedIndices: List<Int>
    ): List<Int> {
        return (0 until validTickets.first().count()).minus(assignedIndices).filter { ticketValueIndex ->
            validTickets.all { ticket -> validValues.contains(ticket[ticketValueIndex]) }
        }
    }

    @Test
    fun task2() {
        val input = getInput("input16")
        logger.info { input.toString() }
        logger.info { "count of nearbyTickets: ${input.nearbyTickets.count()}" }


        val invalidTickets = input.nearbyTickets.filter { !isTicketValid(input.validValues, it) }
        val validTickets = input.nearbyTickets.union(listOf(input.myTicket)).minus(invalidTickets).toList()

        logger.info { "count of validTickets: ${validTickets.count()}" }
        val unassignedRules = input.validValues.keys.toMutableList()
        val ruleIndexMap = emptyMap<String, Int>().toMutableMap()
        while (unassignedRules.any()) {
            unassignedRules.toList().forEach { rule ->
                val indexCandidates =
                    findRuleIndexCandidates(input.validValues[rule]!!, validTickets, ruleIndexMap.values.toList())
                if (indexCandidates.count() == 1) {
                    unassignedRules.remove(rule)
                    ruleIndexMap[rule] = indexCandidates.first()
                }
            }
        }

        logger.info{"ruleIndexMap: $ruleIndexMap"}

        val multiplyDeparture =  ruleIndexMap.filter { it.key.startsWith("departure") }.map{input.myTicket[it.value].toLong()}.reduce{
            acc, element -> acc * element
        }

        logger.info { "multiplied departure values : $multiplyDeparture" }
    }
}
