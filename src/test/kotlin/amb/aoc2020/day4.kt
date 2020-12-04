package amb.aoc2020

import org.junit.jupiter.api.Test

class Day4 : TestBase() {

    private fun getInput(): List<String> {
        val passportCandidates = emptyList<String>().toMutableList()
        var input = ""
        getTestData("input4").forEach {
            if (it.isEmpty()) {
                passportCandidates.add(input)
                input = ""
            } else input += " $it"
        }
        return passportCandidates
    }

    @Test
    fun task1() {
        var numberOfValidInputs = 0
        val requiredFields = setOf("byr", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:")
        getInput().forEach { candidate ->
            if (requiredFields.all { candidate.contains(it) }) {
                numberOfValidInputs++
                logger.info { "valid: $candidate" }
                logger.info { "\\n" }
            }
        }
        logger.info { "found $numberOfValidInputs valid passports" }
    }

    @Test
    fun task2() {
        val requiredValidations = mapOf(
            "byr" to ::isYearOfBirthValid,
            "iyr" to ::isYearOfIssueValid,
            "eyr" to ::isYearOfExpirationValid,
            "hgt" to ::isHeightValid,
            "hcl" to ::isHairColorValid,
            "ecl" to ::isEyeColorValid,
            "pid" to ::isPidValid
        )
        var countOfValidPassports = 0
        getInput().forEach {input->
            logger.info{"Input: $input"}

            val validationResults = requiredValidations.map {
                val regex = """${it.key}:([#a-z0-9]*)""".toRegex()
                val match = regex.find(input)
                match != null && it.value(match.value.substring(4))
            }
            val allValid = validationResults.reduce{acc,i -> acc&&i}
            if (allValid) countOfValidPassports++
        }
        logger.info { "found $countOfValidPassports valid passports" }
    }


    private fun isYearOfBirthValid(input: String): Boolean {
        return input.toIntOrNull() ?: 0 in 1921..2001
    }

    private fun isYearOfIssueValid(input: String): Boolean {
        return input.toIntOrNull() ?: 0 in 2010..2020
    }

    private fun isYearOfExpirationValid(input: String): Boolean {
        return input.toIntOrNull() ?: 0 in 2020..2030
    }

    private fun isHeightValid(input:String): Boolean{
        val cm =
            if(input.contains("cm")) true else {
                if (input.contains("in")) false else return false
            }
        val number = input.replace(if(cm) "cm" else "in","").toIntOrNull()

        return if(cm)
            number?:0 in 150..193
        else
            number?:0 in 59..76
    }

    private fun isHairColorValid(input: String): Boolean{
        val validChars = "0123456789abcdef"
        return input.first() == '#' && input.substring(1).all { validChars.contains(it) }
    }

    private fun isEyeColorValid(input: String): Boolean {
        val validValues = setOf("amb","blu","brn","gry","grn","hzl","oth")
        return validValues.contains(input)
    }

    private fun isPidValid(input: String) : Boolean{
        val digits = "0123456789"
        return input.length == 9 && input.all { digits.contains(it) }
    }



}
