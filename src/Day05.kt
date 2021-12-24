package net.dryft.aoc.day05

import java.io.File
import kotlin.math.*

fun readFile(name: String) = File("src", name).readLines()

data class Coord(val x: Int, val y: Int) {
    fun add(c: Coord): Coord {
        return Coord(c.x + x, c.y + y)
    }
}
data class LineDefn(val start: Coord, val end: Coord) {
    // Limit to plus or minus 1
    private fun limitToOne(n: Int): Int {
        return if (n >= 1) 1
        else if (n <= -1) -1
        else 0
    }

    fun step(): Coord {
        val xdelta = end.x - start.x
        val ydelta = end.y - start.y
        val xstep = limitToOne(xdelta)
        val ystep = limitToOne(ydelta)
        return Coord(xstep, ystep)
    }

    fun allPointsOnLine(): List<Coord> {
        val points = mutableListOf<Coord>()
        points += start
        var current = start
        while (current != end) {
            current = current.add(step())
            points += current
        }
        return points
    }
}

typealias PuzzleInput = List<LineDefn>

fun main() {
    /*
    Example line: 781,721 -> 781,611
     */
    fun parseInput(input: List<String>): PuzzleInput {
        val r = Regex("""^(\d+),(\d+) -> (\d+),(\d+)$""")
        return input.map { line ->
            val (x1,y1,x2,y2) = r.matchEntire(line)!!.destructured
            LineDefn(Coord(x1.toInt(), y1.toInt()), Coord(x2.toInt(), y2.toInt()))
        }
    }

    fun linesToIntersectCount(lines: PuzzleInput): Int {
        val allPoints = mutableMapOf<Coord, Int>()

        lines.forEach { line ->
            val points = line.allPointsOnLine()
            points.forEach { point ->
                val n = allPoints[point] ?: 0
                allPoints[point] = n+1
            }
        }

        return allPoints.filterValues { it > 1 }.size
    }

    fun part1(input: PuzzleInput): Int {
        // Only take vertical or horizontal lines:
        val lines = input.filter {
            it.start.x == it.end.x || it.start.y == it.end.y
        }
        return linesToIntersectCount(lines)
    }

    fun part2(input: PuzzleInput): Int {
        return linesToIntersectCount(input)
    }

    val t = LineDefn(Coord(6,6), Coord(1,1))
    assert(t.allPointsOnLine().size == 6)

    val input = parseInput(readFile("day-5-input.txt"))
    println("Part 1:" + part1(input))
    println("Part 2:" + part2(input))
}
