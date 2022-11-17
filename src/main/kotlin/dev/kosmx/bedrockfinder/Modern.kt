package dev.kosmx.bedrockfinder

import com.mike.BedrockReader
import com.mike.BedrockReader.BedrockType
import dev.kosmx.bedrockfinder.mc.BedrockBlock
import dev.kosmx.bedrockfinder.mc.BlockPos
import kotlinx.cli.*
import java.lang.IllegalStateException
import java.util.Scanner


/**
 * Modern reimplementation of the old bedrock finder tool
 *
 * Modernized: parallel computing, Kt arg parser (arg order can vary), input from file and from lines
 */
fun ktMain(args: Array<String>) {
    val parser = ArgParser("Bedrock formation finder")

    val seed: Long by parser.option(LongArg, shortName = "s", fullName = "seed", description = "World seed").required()

    val center: Pair<Int, Int> by parser.option(
        CoordinateArg,
        "center",
        description = "Center of the search.\nIf doing aligned search, this is the alignment.\nDefault: 0:0"
    ).default(
        Pair(0, 0)
    )

    val aligned: Boolean by parser.option(ArgType.Boolean, "aligned", "a", description = "Chunk aligned search")
        .default(false)

    val bedrockType: BedrockType by parser.option(
        ArgType.Choice<BedrockType>(),
        "bedrockType",
        shortName = "b",
        description = "bedrock_floor for Overworld floor\nnether_floor for nether floor\nnether_roof for nether roof"
    ).default(BedrockType.BEDROCK_FLOOR)

    val threads: Int by parser.option(
        ArgType.Int,
        fullName = "threads",
        shortName = "t",
        "Computational threads used for locating.\nDefault is computer thread count - 1"
    ).default(Runtime.getRuntime().availableProcessors() - 1)

    val bedrocksInput: List<BedrockBlock>? by parser.option(
        BedrocksArg,
        fullName = "blocks",
        "l",
        "Bedrock blocks, if not given, program will read in STDIN until EOF"
    )
    val terminateOnMatch: Boolean by parser.option(
        ArgType.Boolean,
        fullName = "firstMatch",
        description = "Terminate on first positive match, default: false"
    ).default(false)

    parser.parse(args)

    val bedrocks: List<BedrockBlock> = bedrocksInput ?: run {
        val scanner = Scanner(System.`in`)
        return@run sequence<String> {
            while (scanner.hasNextLine())
                try {
                    yield(scanner.nextLine())
                } catch (e: IllegalStateException) {
                    return@sequence //sequence end
                }
        }
            .flatMap { it.split(" ") }
            .map { BedrockBlock(it) }
            .toList()
    }

    if (bedrocks.isEmpty()) error("You didn't specify any block")

    // Compute
    val bedrockReader = BedrockReader(seed, bedrockType)

    val candidates = snailRanges(aligned, xDefault = center.first, zDefault = center.second).mapParallel(numThreads = threads) {
        val list = mutableListOf<BlockPos>()
        for (x in it.first) {
            for (z in it.second) {
                if (bedrockReader.check(bedrocks, x, z) || bedrockReader.checkInvert(bedrocks, x, z)) {
                    list += BlockPos(x, 0, z)
                }
            }
        }
        return@mapParallel if (list.isEmpty()) null else list
    }.mapNotNull { it }

    if (terminateOnMatch) {
        candidates.firstOrNull()?.firstOrNull()?.let { println(it.printString()) }
    } else {
        candidates.flatMap { it }.forEach { println(it) }
    }
}

fun BedrockReader.check(blocks: List<BedrockBlock>, x: Int, z: Int): Boolean {
    for (block in blocks) {
        if (isBedrock(block.x + x, block.y, block.z + z) != block.shouldBeBedrock) return false
    }
    return true
}

fun BedrockReader.checkInvert(blocks: List<BedrockBlock>, x: Int, z: Int): Boolean {
    for (block in blocks) {
        if (isBedrock(x - 1 - block.x, block.y, z - 1 - block.z) != block.shouldBeBedrock) return false
    }
    return true
}


object LongArg : ArgType<Long>(true) {
    override val description: kotlin.String
        get() = "{ Long }"

    override fun convert(value: kotlin.String, name: kotlin.String): Long =
        value.toLongOrNull()
            ?: throw ParsingException("Option $name is expected to be integer number. $value is provided.")

}

object CoordinateArg : ArgType<Pair<Int, Int>>(true) {
    override val description: kotlin.String
        get() = "{ Int:Int }"

    override fun convert(value: kotlin.String, name: kotlin.String): Pair<kotlin.Int, kotlin.Int> {
        val array = value.split(Regex("[,;:]")).dropLastWhile { it.isEmpty() }
        val error = ParsingException("Option $name is expected to be a coordinate pair. $value is provided")
        if (array.size < 2) throw error
        return array[0].toIntOrNull()?.let { x -> array[1].toIntOrNull()?.let { z -> Pair(x, z) } } ?: throw error
    }
}

object BedrocksArg : ArgType<List<BedrockBlock>>(true) {
    override val description: kotlin.String
        get() = "{ X:Y:X,isBedrock }..."

    override fun convert(value: kotlin.String, name: kotlin.String): List<BedrockBlock> =
        value.split(" ").map { BedrockBlock(it) }.toList()
}
