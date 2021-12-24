package net.dryft.aoc.day06

import java.io.File

fun readFile(name: String) = File("src", name).readLines()

/*
Initial state: 3,4,3,1,2
After  1 day:  2,3,2,0,1
After  2 days: 1,2,1,6,0,8
After  3 days: 0,1,0,5,6,7,8
After  4 days: 6,0,6,4,5,6,7,8,8
After  5 days: 5,6,5,3,4,5,6,7,7,8
After  6 days: 4,5,4,2,3,4,5,6,6,7
After  7 days: 3,4,3,1,2,3,4,5,5,6
After  8 days: 2,3,2,0,1,2,3,4,4,5
After  9 days: 1,2,1,6,0,1,2,3,3,4,8
After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8
Each day, a 0 becomes a 6 and adds a new 8 to the end of the list, while each other number decreases by 1 if it was present at the start of the day.

In this example, after 18 days, there are a total of 26 fish. After 80 days, there would be a total of 5934.
 */
fun main() {
    fun parseInput(input: List<String>): List<Int> {
        return input[0].split(",").map { it.toInt() }
    }

    tailrec fun part1(input: List<Int>, days: Int): Int {
        if (days == 0) return input.size

        val newFish = input.flatMap {
            when (it) {
                0 -> listOf(6, 8)
                else -> listOf(it - 1)
            }
        }
        return part1(newFish, days - 1)
    }

    fun part2(input: List<Int>, days: Int): Long {
        val buckets = mutableMapOf<Int,Long>()
        IntRange(0, 8).forEach { i ->
            buckets[i] = input.count { it == i }.toLong()
        }

        var dayCount = days
        while (dayCount > 0) {
            val newFish = buckets[0] ?: 0
            IntRange(0, 7).forEach { i ->
                buckets[i] = buckets[i+1] ?: 0
            }
            buckets[6] = (buckets[6] ?: 0) + newFish
            buckets[8] = newFish
            dayCount -= 1
        }

        return IntRange(0, 8).sumOf { buckets[it] ?: 0 }
    }

    val input = parseInput(readFile("day-6-input.txt"))

    assert(part1(listOf(3,4,3,1,2), 80) == 5934)
    assert(part1(input, 80).toLong() == part2(input, 80))

    println("part 1: " + part1(input, 80))
    println("part 2: " + part2(input, 256))
}
