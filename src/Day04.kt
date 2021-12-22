package net.dryft.aoc.day04

import java.io.File

fun readFile(name: String) = File("src", name).readLines()

typealias BingoBoard = List<List<Int>>

data class PuzzleInput(val numberSequence: List<Int>, val boards: List<BingoBoard>)

fun main() {

    fun isWinningBoard(numbers: List<Int>, board: BingoBoard): Boolean {
        val horizWin = board.any { row -> row.all { numbers.contains(it) } }
        val vertWin = IntRange(0,4).any { columnIdx ->
            board.map { it[columnIdx] }
                 .all { numbers.contains(it)}
        }
        return horizWin || vertWin
    }

    fun firstWinningScore(input: PuzzleInput): Int {
        val winningNumbersIdx = IntRange(1, input.numberSequence.size).first { numbersIdx ->
            val partialNumbers = input.numberSequence.take(numbersIdx)
            input.boards.any { isWinningBoard(partialNumbers, it)}
        }
        val winningNumberSequence = input.numberSequence.take(winningNumbersIdx)
        val winningBoard = input.boards.first { isWinningBoard(winningNumberSequence, it) }
        val remainingNumbers = winningBoard.flatten().filterNot { winningNumberSequence.contains(it) }
        val lastCalledNumber = winningNumberSequence.last()
        return remainingNumbers.sum() * lastCalledNumber
    }

    fun lastWinningScore(input: PuzzleInput): Int {
        val winningNumbersIdx = IntRange(1, input.numberSequence.size).first { numbersIdx ->
            val partialNumbers = input.numberSequence.take(numbersIdx)
            input.boards.any { isWinningBoard(partialNumbers, it)}
        }
        val winningNumberSequence = input.numberSequence.take(winningNumbersIdx)
        val winningBoard = input.boards.first { isWinningBoard(winningNumberSequence, it) }
        val remainingBoards = input.boards.filterNot { isWinningBoard(winningNumberSequence, it) }
        val remainingNumbers = winningBoard.flatten().filterNot { winningNumberSequence.contains(it) }
        val lastCalledNumber = winningNumberSequence.last()
        return if (remainingBoards.isEmpty())
            remainingNumbers.sum() * lastCalledNumber
        else
            lastWinningScore(input.copy(boards = remainingBoards))
    }

    fun loadBoards(raw: List<String>): List<BingoBoard> {
        return raw.chunked(6).map { lines ->
            if (lines.size == 6 && lines[5].length > 2) {
                throw Exception("Unexpected characters on line 6 of a board: $lines[5]")
            }
            lines.take(5).map { line ->
                line.trim().split(Regex("\\s+")).map {
                    it.toInt()
                }
            }
        }
    }

    fun loadInput(filename: String): PuzzleInput {
        val raw = readFile(filename)
        val numberSequence = raw[0].split(",").map { it.toInt() }
        val boards = loadBoards(raw.drop(2))
        return PuzzleInput(numberSequence, boards)
    }

    val input = loadInput("day-4-input.txt")
    println("First winning score: " + firstWinningScore(input))
    println("Last winning score: " + lastWinningScore(input))
}
