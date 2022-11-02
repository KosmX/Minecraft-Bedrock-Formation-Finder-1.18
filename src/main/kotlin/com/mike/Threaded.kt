package com.mike

import java.util.concurrent.Executors

fun run(sequence: Sequence<Pair<Int, Int>>, bedrockReader: BedrockReader) {
    sequence.consumeParallel { test(it) }
}

fun test(xz: Pair<Int, Int>) {
    val x = xz.first
    val z = xz.second
    if (Main.checkFormation(x, z) || checkInvert && Main.checkFormationInvert(x, z)) {
        println("Found Bedrock Formation at X:$x Z:$z")
        println("/tp $x ~ $z")
    }
}

fun <T> Sequence<T>.consumeParallel(action: (value: T) -> Unit) {
    val numThreads = Runtime.getRuntime().availableProcessors() - 1
    chunked(1024).map { chunk ->
        val threadPool = Executors.newFixedThreadPool(numThreads)
        try {
            chunk.map {
                threadPool.submit { -> action(it) }
            }
        } finally {
            threadPool.shutdown()
        }
    }.forEach { _ -> }
}
