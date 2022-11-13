package dev.kosmx.bedrockfinder

import java.util.concurrent.Executors

/**
 * Map a sequence parallel, using available - 1 threads by default
 * The process has no timeout or exception handling, please provide your own if needed.
 *
 */
fun <T, R> Sequence<T>.mapParallel(numThreads: Int = Runtime.getRuntime().availableProcessors() - 1, action: (value: T) -> R?): Sequence<R?> {
    return chunked(numThreads)
        .map { chunk ->
            val threadPool = Executors.newFixedThreadPool(numThreads)
            try {
                return@map chunk.map {
                    val callable: () -> R? = { action(it) }
                    threadPool.submit(callable)
                }
            } finally {
                threadPool.shutdown()
            }
        }.flatten()
        .map { future -> future.get() }
}
