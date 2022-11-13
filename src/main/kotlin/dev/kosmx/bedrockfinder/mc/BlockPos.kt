package dev.kosmx.bedrockfinder.mc

data class BlockPos(val x: Int, val y: Int, val z: Int) {

    companion object {

        @Suppress("unused")
        fun fromString(arg: String): BlockPos? {
            val coords = arg.split(",".toRegex())
                .dropLastWhile { it.isEmpty() }

            return coords[0].toIntOrNull()?.let { x ->
                coords[1].toIntOrNull()?.let { y ->
                    coords[2].toIntOrNull()?.let { z ->
                        BlockPos(x, y, z)
                    }
                }
            }
        }
    }

    fun printString() = "Found Bedrock Formation at X:$x Z:$z\n/tp $x ~ $z"
}