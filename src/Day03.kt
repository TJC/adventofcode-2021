package net.dryft.aoc.day03

import java.io.File
import kotlin.math.pow

fun readInput(name: String) = File("src", "$name").readLines()
/*
Input has 12 "bits" per line.

TODO: https://adventofcode.com/2021/day/3#part2
 */
fun main() {
    val bits = listOf(0,1,2,3,4,5,6,7,8,9,10,11)
    val bitPowers = bits.map {
        2.toDouble().pow(11 - it).toLong()
    }

    // Index starts at 0
    fun valueOfIndex(input: List<String>, index: Int): Int {
        val ourBits = input.map { it[index] }
        val zeroCount = ourBits.count { it == '0' }
        val oneCount = ourBits.count { it == '1' }
        return if (oneCount > zeroCount) 1
        else 0
    }

    fun flip(input: List<Int>): List<Int> {
        return input.map {
            when(it) {
                0 -> 1
                1 -> 0
                else -> throw Exception("Invalid bit: $it")
            }
        }
    }

    fun bitsToDecimal(input: List<Int>): Long {
        return input.zip(bits).sumOf { (bit, position) ->
            bitPowers[position] * bit
        }
    }

    fun part1(input: List<String>): Long {
        val gammaBits = bits.map { valueOfIndex(input, it) }
        val epsilonBits = flip(gammaBits)


        val gamma = bitsToDecimal(gammaBits)
        val epsilon = bitsToDecimal(epsilonBits)
        println("gamma: $gamma")
        println("epsilon: $epsilon")
        return gamma * epsilon
    }

    val testInput = readInput("puzzle-3-input.txt")
    println(part1(testInput))
}
