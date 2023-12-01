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
            seqSort(a, l, r, randomNextInt)
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
            
            val sortingLeft = async {
                val left = leftDeferred.await()
                psort(left)
                System.arraycopy(left, 0, a, 0, left.size)
            }
            val sortingRight = async {
                val right = rightDeferred.await()
                psort(right)
                System.arraycopy(right, 0, a, r - l + 1 - right.size, right.size)
            }

            sortingLeft.await()
            sortingRight.await()
        }
    }
}
