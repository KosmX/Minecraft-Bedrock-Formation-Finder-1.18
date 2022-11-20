package dev.kosmx.bedrockfinder



const val blockSize = 4096
fun snailRanges(aligned: Boolean = false, xDefault: Int = 0, zDefault: Int = 0): Sequence<Pair<IntProgression, IntProgression>> = sequence {
    val step = if (aligned) 16 else 1
    val chunkSize = step * blockSize
    val max = 30_000_000
    val seq = snail(false).iterator()
    seq.forEach {
        val x = it.first * chunkSize + xDefault
        val y = it.second * chunkSize + zDefault
        if (x > max && y > max) return@sequence

        yield(Pair(x until x + chunkSize step step, y until y + chunkSize step step))
    }

}

fun snail(aligned: Boolean = false, xDefault: Int = 0, zDefault: Int = 0) = sequence {
    var x = xDefault; var z = zDefault
    var stepsToTake = 1
    var stepsTaken = 0
    var sidesUntilIncremental = 0
    var direction = Direction.RIGHT
    while (true) {
        if (x > 30_000_000 || z > 30_000_000) {
            break
        }
        yield(Pair(x, z))

        // Check for direction change
        if (stepsTaken >= stepsToTake) {
            stepsTaken = 0
            sidesUntilIncremental++
            direction = when (direction) {
                Direction.LEFT -> Direction.DOWN
                Direction.RIGHT -> Direction.UP
                Direction.UP -> Direction.LEFT
                Direction.DOWN -> Direction.RIGHT
            }
        }

        // Increase steps to take
        if (sidesUntilIncremental >= 2) {
            sidesUntilIncremental = 0
            stepsToTake++
        }

        // Make Step
        if (aligned) {
            when (direction) {
                Direction.LEFT -> x -= 16
                Direction.RIGHT -> x += 16
                Direction.UP -> z += 16
                Direction.DOWN -> z -= 16
            }
        } else {
            when (direction) {
                Direction.LEFT -> x--
                Direction.RIGHT -> x++
                Direction.UP -> z++
                Direction.DOWN -> z--
            }
        }
        stepsTaken++
    }
}