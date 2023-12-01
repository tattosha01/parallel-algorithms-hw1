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