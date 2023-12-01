import kotlinx.coroutines.*
import java.util.concurrent.ThreadLocalRandom

class ParallelForkJoinQuickSorter(
    private val quickSortBlockSize: Int,
) : Sorter() {
    private inline val randomNextInt: (Int, Int) -> Int
        get() = ThreadLocalRandom.current()::nextInt

    override fun IntArray.sort(
        l: Int,
        r: Int,
    ): Unit = runBlocking {
        launch(coroutineSorterDispatcher) {
            psort(this@sort, l, r)
        }
    }

    private suspend fun psort(
        a: IntArray,
        l: Int = 0,
        r: Int = a.size - 1,
    ) {
        if (r <= l) return
        if (r - l < quickSortBlockSize) {
            seqSort(a, l, r, randomNextInt)
            return
        }

        val m = partition(a, l, r, randomNextInt)
        forkJoin(
            f1 = { psort(a, l, m) },
            f2 = { psort(a, m + 1, r) }
        )
    }
}
