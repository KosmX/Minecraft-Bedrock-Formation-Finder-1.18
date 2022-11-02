package com.mike


private enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

const val checkInvert = true

/*
fun modifiedSnail(aligned: Boolean = false) = sequence {

}*/

fun snail(aligned: Boolean = false, xDefault: Int = 0, zDefault: Int = 0) = sequence {
    var x = xDefault; var z = zDefault
    var stepsToTake = 1
    var stepsTaken = 0
    var sidesUntilIncremental = 0
    var extraDivision = 0
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
        if (sidesUntilIncremental > 2) {
            sidesUntilIncremental = 0
            stepsToTake++
            if (extraDivision++ > 1000) {
                extraDivision = 0
                println("$x;$z")
            }
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