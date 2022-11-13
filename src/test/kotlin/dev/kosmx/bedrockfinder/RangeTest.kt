package dev.kosmx.bedrockfinder

import org.junit.jupiter.api.Test
import java.util.*


//by now, what is the difference between a function variable and a function
fun newProgressionSet() = TreeSet<IntProgression> { a, b -> a.compareTo(b) }

internal class RangeTest {


    @Test
    fun testRange() {
        val mappedX = newProgressionSet()
        val mappedY = newProgressionSet()

        for (it in snailRanges(false)) {
            mappedX.add(it.first)
            mappedY.add(it.second)
            if (it.first.first > 10_000) break
        }

        println(mappedX.stream().reduce { t, u ->
            if (t.last + 1 == u.first) return@reduce t.first..u.last
            else error("Not ordered: $t, $u")
        })
    }

}

private operator fun IntProgression.compareTo(b: IntProgression): Int = this.first.compareTo(b.first)
