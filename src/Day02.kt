package net.dryft.aoc.day02

import java.io.File

enum class SubDirection {
    forward, down, up
}

data class Move(val action: SubDirection, val amount: Int)

fun readInput(name: String) = File("src", "$name").readLines()

/*
down X increases your aim by X units.
up X decreases your aim by X units.
forward X does two things:
  It increases your horizontal position by X units.
  It increases your depth by your aim multiplied by X.
 */
fun main() {
    fun part1(input: List<Move>): Int {
        val forwards = input.filter { it.action == SubDirection.forward }.sumOf { it.amount }
        val downwards = input.filter { it.action == SubDirection.down }.sumOf { it.amount }
        val upwards = input.filter { it.action == SubDirection.up }.sumOf { it.amount }
        val depth = downwards - upwards
        return forwards * depth
    }

    fun part2(input: List<Move>): Long {
        var aim = 0L
        var depth = 0L
        var horizontal = 0L
        input.forEach {
            when (it.action) {
                SubDirection.up -> aim -= it.amount
                SubDirection.down -> aim += it.amount
                SubDirection.forward -> {
                    horizontal += it.amount
                    depth += aim * it.amount
                }
            }
        }
        println("Final aim: $aim")
        println("Final depth: $depth")
        println("Final horizontal: $horizontal")
        return horizontal * depth
    }

    fun parser(input: List<String>): List<Move> {
        val regex = """(\w+)\s+(\d+)""".toRegex()
        return input.map { i ->
            val matchResult = regex.find(i)
            val (command, distance) = matchResult!!.destructured
            val action = SubDirection.valueOf(command)
            Move(action, distance.toInt())
        }
    }

    val testInput = readInput("puzzle-2-input.txt")
    val commands = parser(testInput)
    println(part1(commands))
    println(part2(commands))
}
