package net.dryft.aoc.day09

import java.io.File

fun readFile(name: String) = File("src", name).readLines()

/*
Part 1:
Given a 2D heightmap, find the lowest points.
For each lowest point, take its value and add one.
Sum these.
*/

val testInputRaw = """
    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
""".trimIndent()

fun main() {
    // Note: Outer array is "y", inner array is "x"
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.map { it.toString().toInt() }}
    }

    fun neighbours(input: List<List<Int>>, x: Int, y: Int): List<Int> {
        val up = if (y == 0) null else input[y-1][x]
        val down = if (y == input.size - 1) null else input[y+1][x]
        val left = if (x == 0) null else input[y][x-1]
        val right = if (x == input[y].size - 1) null else input[y][x+1]
        return listOfNotNull(up, down, left, right)
    }

    fun part1(input: List<List<Int>>): Int {
        var score = 0
        for (y in input.indices) {
            for (x in input[y].indices) {
                val v = input[y][x]
                if (neighbours(input, x, y).all { v < it }) {
                    score += v + 1
                }
            }
        }

        return score
    }

    val testInput = parseInput(testInputRaw.lines())
    assert(part1(testInput) == 15)

    val input = parseInput(readFile("day-9-input.txt"))
    println("Part 1:" + part1(input))
}
