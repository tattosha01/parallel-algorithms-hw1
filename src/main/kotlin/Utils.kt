private const val N2_SORT_BLOCK_SIZE = 12

fun seqSort(
    a: IntArray,
    l: Int,
    r: Int,
    randomNextInt: (Int, Int) -> Int,
) {
    if (r - l < N2_SORT_BLOCK_SIZE) {
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
    seqSort(a, l, m, randomNextInt)
    seqSort(a, m + 1, r, randomNextInt)
}

inline fun partition(
    a: IntArray,
    l: Int,
    r: Int,
    randomNextInt: (Int, Int) -> Int,
): Int {
    val v = a[randomNextInt(l, r + 1)]
    var i = l
    var j = r
    while (i <= j) { // O(n)
        while (a[i] < v) {
            i++
        }
        while (a[j] > v) {
            j--
        }
        if (i >= j) {
            break
        }
        swap(a, i, j)
        i++
        j--
    }

    return j
}

inline fun swap(
    a: IntArray,
    i: Int,
    j: Int,
) {
    a[i] = a[j].also {
        a[j] = a[i]
    }
}
