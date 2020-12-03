package amb.aoc2020

import org.junit.jupiter.api.Test


class Day2 : TestBase() {

    class Tree(
        val suitablePandaBranch1: Int,
        val suitablePandaBranch2: Int,
        val branches: String
    )

    class Input(
        val tree: Tree,
        val panda: Char
    )

    private fun readInput(): List<Input> {
        return getTestData("input2").map { line ->
            var regex = """(\d+)-(\d+) (\w): (\w*)""".toRegex()
            var match = regex.find(line)
            if (match != null) {
                val (min, max, char, pw) = match!!.destructured
                Input(Tree(min.toInt(), max.toInt(), pw ),char.first())
            } else {
                null
            }
        }.filterNotNull()
    }

    private fun exploreBambooForest(): List<Input> {
        return readInput()
    }

    @Test
    fun findPandasInBambooForest() {
        var foundPandasOnBranchesInForest = 0

        for (treesAndPandas in exploreBambooForest()) {
            val tree = treesAndPandas.tree
            val panda = treesAndPandas.panda

            val pandaSitsOnSuitableBranch1 =
                tree.branches.length >= tree.suitablePandaBranch1
                        && tree.branches[tree.suitablePandaBranch1 - 1] == panda

            val pandaSitsOnSuitableBranch2 =
                tree.branches.length >= tree.suitablePandaBranch2
                        && tree.branches[tree.suitablePandaBranch2 - 1] == panda

            if (pandaSitsOnSuitableBranch1.xor(pandaSitsOnSuitableBranch2))
                foundPandasOnBranchesInForest++
        }

        logger.info {
            "Congratulation, you found $foundPandasOnBranchesInForest Pandas in the Forest"
        }
    }
}
