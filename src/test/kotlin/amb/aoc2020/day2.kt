package amb.aoc2020

import org.junit.jupiter.api.Test


class Day2 : TestBase() {

    class Tree(
        val branches: String
    )

    class Panda(
        val name: Char,
        val favouriteBranch1: Int,
        val favouriteBranch2: Int,
    )

    class Input(
        val tree: Tree,
        val panda: Panda
    )

    @Test
    fun findPandasInBambooForest() {
        var foundPandasOnTheirFavouriteBranchesInForest = 0

        for (treesAndPandas in exploreBambooForest()) {
            val tree = treesAndPandas.tree
            val panda = treesAndPandas.panda

            val pandaSitsOnFavouriteBranch1 =
                tree.branches.length >= panda.favouriteBranch1
                        && tree.branches[panda.favouriteBranch1 - 1] == panda.name

            val pandaSitsOnFavouriteBranch2 =
                tree.branches.length >= panda.favouriteBranch2
                        && tree.branches[panda.favouriteBranch2 - 1] == panda.name

            if (pandaSitsOnFavouriteBranch1.xor(pandaSitsOnFavouriteBranch2))
                foundPandasOnTheirFavouriteBranchesInForest++
        }

        logger.info {
            "Congratulation, you found $foundPandasOnTheirFavouriteBranchesInForest Pandas in the Forest"
        }
    }

    private fun readInput(): List<Input> {
        return getTestData("input2").map { line ->
            var regex = """(\d+)-(\d+) (\w): (\w*)""".toRegex()
            var match = regex.find(line)
            if (match != null) {
                val (min, max, char, pw) = match!!.destructured
                Input(Tree( pw ),Panda(char.first(),min.toInt(),max.toInt()))
            } else {
                null
            }
        }.filterNotNull()
    }

    private fun exploreBambooForest(): List<Input> {
        return readInput()
    }

}
