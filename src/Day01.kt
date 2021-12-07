fun main() {
    fun part1(input: List<Int>): Int {
        val offset1 = input.subList(1, input.size)
        val pairs = input.zip(offset1)
        return pairs.filter { (i, j) -> j > i }.size
    }

    fun part2(input: List<Int>): Int {
        val offset1 = input.subList(1, input.size)
        val offset2 = input.subList(2, input.size)
        val trips = input.zip(offset1).zip(offset2)
        val sums = trips.map {
            val i = it.first.first
            val j = it.first.second
            val k = it.second
            i+j+k
        }
        val combined = sums.zip(sums.subList(1, sums.size))
        return combined.filter { (a,b) -> b > a }.size
    }

    val testInput = readInput("puzzle-1-input.txt")
    val intList = testInput.map { it.toInt() }
    println(part1(intList))

    println(part2(intList))

}
