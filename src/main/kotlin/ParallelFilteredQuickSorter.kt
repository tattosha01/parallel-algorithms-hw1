import kotlinx.coroutines.*
import java.util.concurrent.ThreadLocalRandom

class ParallelFilteredQuickSorter(
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
            seqSort(a, l, r)
            return
        }

        val x = a[randomNextInt(l, r + 1)]

        coroutineScope {
            val leftDeferred = async {
                pfilter(a, l, r) { i -> a[i] < x  }
            }
            val rightDeferred = async {
                pfilter(a, l, r) { i -> a[i] >= x  }
            }

            val left = leftDeferred.await()
            val right = rightDeferred.await()

            val sortingLeft = async {
                psort(left)
                System.arraycopy(left, 0, a, 0, left.size)
            }
            val sortingRight = async {
                psort(right)
                System.arraycopy(right, 0, a, left.size, right.size)
            }

            sortingLeft.await()
            sortingRight.await()
        }
    }

    private fun seqSort(
        a: IntArray,
        l: Int,
        r: Int,
    ) {
        if (r <= l) {
            return
        }

        val m = partition(a, l, r, randomNextInt)
        seqSort(a, l, m)
        seqSort(a, m + 1, r)
    }
}
