import java.text.DecimalFormat
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

const val COUNT_TEST = 5
const val ARRAY_SIZE = 100_000_000 // -Xmx8192m is required
const val QUICK_SORT_BLOCK_SIZE = 3_000

val formatter = { it: Any -> DecimalFormat("0.000").format(it) }

fun main() {
    val seqSorter = SequenceQuickSorter()
    val parFilteredSorter = ParallelFilteredQuickSorter(QUICK_SORT_BLOCK_SIZE)
    val parForkJoinSorter = ParallelForkJoinQuickSorter(QUICK_SORT_BLOCK_SIZE)

    println("The warming up has started... | now = ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}")
    warmingUp(seqSorter, IntArray(ARRAY_SIZE) { Random.nextInt() })
    warmingUp(parFilteredSorter, IntArray(ARRAY_SIZE) { Random.nextInt() })
    warmingUp(parForkJoinSorter, IntArray(ARRAY_SIZE) { Random.nextInt() })
    println("The warming up has finished   | now = ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}")
    
    val times = Array(COUNT_TEST) { index ->
        println("Generation of input[$index] with size = $ARRAY_SIZE started... | now = ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}")
        val inputData = IntArray(ARRAY_SIZE) { Random.nextInt() }
        println("Generation of input[$index] with size = $ARRAY_SIZE finished   | now = ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}")

        println("Sorting of input[$index] with size = $ARRAY_SIZE started... | now = ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}")
        val sortingResult = Triple(
            measureTimeAndSortingCheck(seqSorter, inputData.copyOf(), index).toDouble(DurationUnit.SECONDS),
            measureTimeAndSortingCheck(parFilteredSorter, inputData.copyOf(), index).toDouble(DurationUnit.SECONDS),
            measureTimeAndSortingCheck(parForkJoinSorter, inputData.copyOf(), index).toDouble(DurationUnit.SECONDS),
        )
        println("Sorting of input[$index] with size = $ARRAY_SIZE finished   | now = ${LocalTime.now().truncatedTo(ChronoUnit.SECONDS)}\n")

        sortingResult
    }

    println("Sequence | ParallelByFilter | ParallelBySeqAndForkJoin")
    times.forEach { time ->
        println("${formatter(time.first)}s  | ${formatter(time.second)}s          | ${formatter(time.third)}s")
    }
    val avg = times.fold(Triple(0.0, 0.0, 0.0)) { res, it ->
        Triple(
            res.first + it.first,
            res.second + it.second,
            res.third + it.third,
        )
    }

    println("avg time (sequence) = ${formatter(avg.first / times.size)}s")
    println("avg time (filtered) = ${formatter(avg.second / times.size)}s")
    println("avg time (forkJoin) = ${formatter(avg.third / times.size)}s")
}

private fun warmingUp(
    sorter: Sorter,
    a: IntArray,
) {
    with(sorter) {
        a.sort()
    }
}

@OptIn(ExperimentalTime::class)
fun measureTimeAndSortingCheck(
    sorter: Sorter,
    inputData: IntArray,
    index: Int,
): Duration {
    val input = inputData.copyOf()
    val time = with(sorter) {
        measureTime {
            input.sort()
        }
    }

    check(input.sortedArray() contentEquals input) {
        println("Error: input-array[$index] is not sorted, ${sorter::class}")
    }

    return time
}
