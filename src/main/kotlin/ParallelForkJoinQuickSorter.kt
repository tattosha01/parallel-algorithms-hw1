import kotlinx.coroutines.*
import java.util.concurrent.ThreadLocalRandom

class ParallelForkJoinQuickSorter(
    private val quickSortBlockSize: Int,
    private val localBlockSize: Int,
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
            seqSort(a, l, r)
            return
        }

        val m = partition(a, l, r, randomNextInt)
        forkJoin(
            f1 = { psort(a, l, m) },
            f2 = { psort(a, m + 1, r) }
        )
    }

    private fun seqSort(
        a: IntArray,
        l: Int = 0,
        r: Int = a.size - 1,
    ) {
        if (r - l < localBlockSize) {
            var i = 0
            var isSorted = false
            while (!isSorted) {
                isSorted = true
                for (j in l..(r - 1 - i)) {
                    if (a[j] > a[j + 1]) {
                        swap(a, j, j + 1)
                        isSorted = false
                    }
                }
                i++
            }

            return
        }

        val m = partition(a, l, r, randomNextInt)
        seqSort(a, l, m)
        seqSort(a, m + 1, r)
    }
}
