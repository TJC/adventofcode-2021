package net.dryft.aoc

import java.io.File

class Day25 {
    inner class Coord(val x: Int, val y: Int) {
        override fun hashCode(): Int {
            return "$x,$y".hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return other?.hashCode() == this.hashCode()
        }

        private fun toEast(): Coord {
            return Coord(x + 1, y)
        }

        private fun toSouth(): Coord {
            return Coord(x, y + 1)
        }

        fun wrappingToEast(): Coord {
            return if (seaFloor[this.toEast()] != null) this.toEast()
            else Coord(0, this.y)
        }

        fun wrappingToSouth(): Coord {
            return if (seaFloor[this.toSouth()] != null) this.toSouth()
            else Coord(this.x, 0)
        }
    }

    interface FloorItems {
        fun checkMoveable(): Boolean
        fun move()
    }

    val seaFloor: MutableMap<Coord, FloorItems> = mutableMapOf()
    var iterations: Int = 0

    class EmptyFloor : FloorItems {
        override fun toString(): String {
            return "."
        }

        override fun checkMoveable(): Boolean {
            return false
        }

        override fun move() {
            // Never moves
        }
    }

    abstract inner class Cucumber(var location: Coord) : FloorItems {
        private var eligibleForMove = false
        abstract fun nextLocation(): Coord

        override fun checkMoveable(): Boolean {
            eligibleForMove = seaFloor[nextLocation()] is EmptyFloor
            return eligibleForMove
        }

        override fun move() {
            if (eligibleForMove) {
                seaFloor[nextLocation()] = this
                seaFloor[location] = EmptyFloor()
                location = nextLocation()
            }
        }

    }

    inner class EasternCucumber(loc: Coord) : Cucumber(loc) {
        override fun toString(): String {
            return ">"
        }

        override fun nextLocation(): Coord {
            return location.wrappingToEast()
        }
    }

    inner class SouthernCucumber(loc: Coord) : Cucumber(loc) {
        override fun toString(): String {
            return "v"
        }

        override fun nextLocation(): Coord {
            return location.wrappingToSouth()
        }
    }

    fun initialiseSeafloor(input: List<String>) {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val loc = Coord(x, y)
                val item = when (c) {
                    '.' -> EmptyFloor()
                    '>' -> EasternCucumber(loc)
                    'v' -> SouthernCucumber(loc)
                    else -> throw Exception("Unexpected char '$c' at $x, $y")
                }
                seaFloor[loc] = item
            }
        }
    }

    fun iterate(): Boolean {
        val movedEast = seaFloor.values.filterIsInstance<EasternCucumber>().map { it.checkMoveable() }.count { it }
        seaFloor.values.filterIsInstance<EasternCucumber>().forEach { it.move() }
        val movedSouth = seaFloor.values.filterIsInstance<SouthernCucumber>().map { it.checkMoveable() }.count { it }
        seaFloor.values.filterIsInstance<SouthernCucumber>().forEach { it.move() }
        return (movedEast + movedSouth > 0)
    }

    fun runUntilStopped(limit: Int): Int {
        while (iterate()) {
            iterations += 1
            if (iterations > limit) throw Exception("too many iterations for test input")
        }
        return iterations + 1
    }

    fun outputStatus() {
        IntRange(0, 6).forEach { y ->
            IntRange(0, 6).forEach { x ->
                print(seaFloor[Coord(x, y)].toString())
            }
            println()
        }
        println("Iterations: $iterations\n")
    }
}


fun main() {
    fun readFile(name: String) = File("src", name).readLines()

    fun testInputs(): List<List<String>> {
        val testInputRaw0 = """
            ...>...
            .......
            ......>
            v.....>
            ......>
            .......
            ..vvv..
        """.trimIndent().lines()

        val testInputRaw1 = """
            ..vv>..
            .......
            >......
            v.....>
            >......
            .......
            ....v..
        """.trimIndent().lines()

        val testInputRaw2 = """
            ....v>.
            ..vv...
            .>.....
            ......>
            v>.....
            .......
            .......
        """.trimIndent().lines()

        return listOf(testInputRaw0, testInputRaw1, testInputRaw2)
    }

    val stoppingTestInput = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
    """.trimIndent()

    val testDay25 = Day25()
    testDay25.initialiseSeafloor(stoppingTestInput.lines())
    assert(testDay25.runUntilStopped(60) == 58)
    testDay25.outputStatus()

    val input = readFile("day-25-input.txt")

    val day25 = Day25()
//    day25.initialiseSeafloor(testInputs()[0])
//    day25.outputStatus()
//    day25.iterate()
//    day25.outputStatus()
//    day25.iterate()
//    day25.outputStatus()

    day25.initialiseSeafloor(input)
    println("Part 1: " + day25.runUntilStopped(10000))
}
