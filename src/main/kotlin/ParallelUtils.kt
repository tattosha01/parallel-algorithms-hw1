import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

private const val BLOCK_SIZE = 2000
const val THREADS_COUNT = 4

@OptIn(DelicateCoroutinesApi::class)
val coroutineSorterDispatcher = newFixedThreadPoolContext(THREADS_COUNT, "ParallelSortersThreadPool") // use for a single sorter in parallel

inline fun scanSerial(
    a: IntArray,
    l: Int = 0,
    r: Int = a.size - 1,
    v: Int = 0,
    itemsScanAction: (Int, Int) -> Int, // potential overflow
) {
    a[l] = itemsScanAction(a[l], v)
    for (i in (l + 1)..r.coerceAtMost(a.size - 1)) {
        a[i] = itemsScanAction(a[i], a[i - 1])
    }
}

inline fun reduceSerial(
    a: IntArray,
    l: Int,
    r: Int,
    itemReduceAction: (Int, Int) -> Int, // potential overflow
): Int {
    var res = a[l]
    for (i in (l + 1)..r.coerceAtMost(a.size - 1)) {
        res = itemReduceAction(res, a[i])
    }
    return res
}

suspend inline fun pfilter(
    a: IntArray,
    l: Int = 0,
    r: Int = a.size - 1,
    crossinline predicate: (Int) -> Boolean,
): IntArray {
    val filtered = IntArray(r - l + 1)

    pmap(filtered, 0, filtered.size - 1) { i ->
        if (predicate(i)) 1 else 0
    }
    pscan(filtered, 0, filtered.size - 1, Int::plus)

    val res = IntArray(filtered.last())
    pfor(0, filtered.size - 1) { i ->
        if ((i == 0 && filtered[0] != 0) || (i > 0 && filtered[i] != filtered[i - 1])) {
            res[filtered[i] - 1] = a[i]
        }
    }

    return res
}

suspend fun pscan(
    a: IntArray,
    l: Int = 0,
    r: Int = a.size - 1,
    f: (Int, Int) -> Int // potential overflow
) {
    if (r - l < BLOCK_SIZE) {
        scanSerial(a, l, r, 0, f)
        return
    }

    val sumsSize = (r - l) / BLOCK_SIZE + 1
    val sums = IntArray(sumsSize)

    pfor(0, sumsSize - 1) { iBlock ->
        sums[iBlock] = reduceSerial(a, l + iBlock * BLOCK_SIZE, l + (iBlock + 1) * BLOCK_SIZE - 1, f)
    }

    pscan(sums, 0, sums.size - 1, f)

    pfor(0, sumsSize - 1) { iBlock ->
        scanSerial(a, l + iBlock * BLOCK_SIZE, l + (iBlock + 1) * BLOCK_SIZE - 1, if (iBlock == 0) 0 else sums[iBlock - 1], f)
    }
}

suspend inline fun pmap(
    a: IntArray,
    l: Int,
    r: Int,
    crossinline itemTransformAction: (Int) -> Int,
) {
    pfor(l, r) { i ->
        a[i] = itemTransformAction(i)
    }
}

suspend fun pfor(
    l: Int,
    r: Int,
    indexAction: (Int) -> Unit,
) {
    if (r - l < BLOCK_SIZE) {
        for (i in l..r) {
            indexAction(i)
        }
        return
    }

    val m = (l + r) / 2
    forkJoin(
        { pfor(l, m, indexAction) },
        { pfor(m + 1, r, indexAction) }
    )
}

suspend inline fun forkJoin(
    crossinline f1: suspend () -> Unit,
    crossinline f2: suspend () -> Unit
) {
    coroutineScope {
        launch { f1() }
        launch { f2() }
    }
}
