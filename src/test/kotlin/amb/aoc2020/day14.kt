package amb.aoc2020

import org.junit.jupiter.api.Test

class Day14 : TestBase() {

    private fun Char.repeat(count: Int): String = this.toString().repeat(count)

    private fun getInstructions(file: String): List<changeMemoryFn> {
        return getTestData(file).filter { it.isNotEmpty() }.map {
            val input = it.split(" = ")
            if (input[0] == "mask")
                { _: Memory, mask: Mask ->
                    logger.info { "newMask: ${input[1]}" }
                    mask.mask = 'X'.repeat(64 - input[1].length) + input[1]
                }
            else
                { mem: Memory, mask: Mask ->
                    val index = input[0].trim('m', 'e', 'm', '[', ']').toInt()
                    var value = input[1].toULong().toString(2)
                    value = '0'.repeat(64 - value.count()) + value
                    logger.info { "value: $value at index $index" }

                    mem[index] = MemoryAddress(operate(value, mask.mask))
                }
        }
    }

    private fun operate(newValue: String, mask: String): String {
        return String(newValue.zip(mask).map {
            if (it.second != 'X') it.second else it.first
        }.toCharArray())
    }


    @Test
    fun task1() {
        val mask = Mask()
        val memory = emptyMap<Int, MemoryAddress>().toMutableMap()
        val instructions = getInstructions("input14")
        instructions.forEach {
            it.invoke(memory, mask)
        }

        logger.info { "Mask: $mask" }
        logger.info { "Memory:" }
        memory.forEach {
            logger.info { it.toString() }
        }

        logger.info { "sum: ${memory.map { it.value.value.toULong(2) }.sum()}" }

    }

    private fun getInstructions2(file: String): List<changeMemoryFn2> {
        return getTestData(file).filter { it.isNotEmpty() }.map {
            logger.info { "processing |||$it||| ..." }
            val input = it.split(" = ")
            if (input[0] == "mask")
                { _: Memory2, mask: Mask ->
                    mask.mask = '0'.repeat(64 - input[1].length) + input[1]
                }
            else
                { mem: Memory2, mask: Mask ->
                    val index = input[0].trim('m', 'e', 'm', '[', ']').toULong().toString(2)
                    val indexAddress = '0'.repeat(64 - index.length) + index
                    val value = input[1].toInt()
                    val floatingAddress = getFloatingMemoryAddress(indexAddress, mask.mask)
                    val addresses = getAllAddresses(floatingAddress)
                    addresses.forEach { address ->
                        mem[address] = value
                    }
                }
        }
    }

    private fun getAllAddresses(floatingAddress: String): List<String> {
        val xAt = floatingAddress.indexOf("X")
        return if (xAt == -1) listOf(floatingAddress)
        else {
            val remainder = if (xAt == floatingAddress.count() - 1) "" else floatingAddress.substring(xAt+1)

            val list = emptyList<String>().toMutableList()
            list.addAll(getAllAddresses(remainder).map { floatingAddress.substring(0, xAt) + "1" + it })
            list.addAll(getAllAddresses(remainder).map { floatingAddress.substring(0, xAt) + "0" + it })
            list
        }
    }

    private fun getFloatingMemoryAddress(address: String, mask: String): String {
        return String(address.zip(mask).map {
            when (it.second) {
                '0' -> it.first
                else -> it.second
            }
        }.toCharArray())
    }


    @Test
    fun task2() {
        val mask = Mask()
        val memory = emptyMap<String, Int>().toMutableMap()
        val instructions = getInstructions2("input14")
        instructions.forEach {
            it.invoke(memory, mask)
        }

        logger.info { "Mask: $mask" }
        logger.info { "Memory:" }
        memory.forEach {
            logger.info { it.toString() }
        }

        logger.info { "sum: ${memory.map { it.value.toULong() }.sum()}" }
    }
}

data class Mask(

    var mask: String = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
)

data class MemoryAddress(
    var value: String = "0000000000000000000000000000000000000000000000000000000000000000"
)

typealias changeMemoryFn = (Memory, Mask) -> Unit

typealias Memory = MutableMap<Int, MemoryAddress>


typealias changeMemoryFn2 = (Memory2, Mask) -> Unit

typealias Memory2 = MutableMap<String, Int>
