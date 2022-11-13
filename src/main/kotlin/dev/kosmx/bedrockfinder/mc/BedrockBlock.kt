package dev.kosmx.bedrockfinder.mc


data class BedrockBlock(val x: Int, val y: Int, val z: Int, val shouldBeBedrock: Boolean) {


    companion object {
        // Example 1,0,2:1
        operator fun invoke(arg: String): BedrockBlock {
            val coordinates =
                arg.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val x = coordinates[0].toInt()
            val y = coordinates[1].toInt()
            val z = coordinates[2].toInt()
            val shouldBeBedrock = arg.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt() == 1
            return BedrockBlock(x, y, z, shouldBeBedrock)
        }
    }

    override fun toString(): String {
        return "BedrockBlock{x=$x, y=$y, z=$z, shouldBeBedrock=$shouldBeBedrock}"
    }
}
