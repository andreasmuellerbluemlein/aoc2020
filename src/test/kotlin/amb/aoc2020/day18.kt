package amb.aoc2020

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18 : TestBase() {

    class Number(var number: String) : Operand {
        override fun getValue(): Long {
            return number.toLong()
        }
    }

    interface Operand {
        fun getValue(): Long
    }

    class CalcOperation(
        operand1: Operand,
        operand2: Operand,
        var operation: Char
    ) : Operation(operand1, operand2) {
        override fun getValue(): Long {
            return if (operation == '+') operand1.getValue() + operand2.getValue() else operand1.getValue() * operand2.getValue()
        }

    }

    abstract class Operation(
        var operand1: Operand,
        var operand2: Operand,
    ) : Operand

    fun parse(input: String): Operand {

        if (input.contains('(')) {
            val regex = """\(([0-9\s+*]*)\)""".toRegex()
            val match = regex.find(input)!!
            return parse(input.replace(match.value, parse(match.value.drop(1).dropLast(1)).getValue().toString()))
        }

        if (input.last().isDigit()) {
            val numberString = input.takeLastWhile { it.isDigit() }
            if (input.count() > numberString.count()) {
                val operator = input.dropLast(numberString.count() + 1).last()
                return CalcOperation(parse(input.dropLast(numberString.count() + 3)), Number(numberString), operator)
            } else (
                    return Number(numberString)
                    )
        }
        throw NotImplementedError()
    }

    fun parse2(input: String): Operand {
        //find opening brackets, calc inner result and textreplace result into original
        if (input.contains('(')) {
            val regex = """\(([0-9\s+*]*)\)""".toRegex()
            val match = regex.find(input)!!
            return parse2(input.replace(match.value, parse2(match.value.drop(1).dropLast(1)).getValue().toString()))
        }

        //find simple sum equation, calc result and textreplace into original
        var regex = """([0-9]+\s[+]\s[0-9]+)""".toRegex()
        var match = regex.find(input)
        if(match != null) {
            val operands = match.value.split(" + ")
            val result = operands[0].toLong() + operands[1].toLong()
            return parse2(input.replace(match.value, result.toString()))
        }

        //find simple multiply equation, calc result and textreplace into original
        regex = """([0-9]+\s[*]\s[0-9]+)""".toRegex()
        match = regex.find(input)
        if(match != null) {
            val operands = match.value.split(" * ")
            val result = operands[0].toLong() * operands[1].toLong()
            return parse2(input.replace(match.value, result.toString()))
        }
        return Number(input)
    }

    @Test
    fun task1() {
        var sum = getTestData("input18")
            .filter { it.isNotEmpty() }
            .map { line ->
                 parse(line).getValue()
            }.sum()

        logger.info { "sum is $sum" }
    }

    @Test
    fun task2() {
        var sum = getTestData("input18")
            .filter { it.isNotEmpty() }
            .map { line ->
                val result = parse2(line).getValue()
                logger.info { "$line = $result" }
                result
            }.sum()

        logger.info { "sum is $sum" }
    }

    @Test
    fun subTest(){
        assertEquals(51L, parse2("1 + (2 * 3) + (4 * (5 + 6))").getValue())
        assertEquals(46L, parse2("2 * 3 + (4 * 5)").getValue())
        assertEquals(1445L, parse2("5 + (8 * 3 + 9 + 3 * 4 * 3)").getValue())
        assertEquals(669060L, parse2("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))").getValue())
        assertEquals(23340L, parse2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2").getValue())
    }
}

