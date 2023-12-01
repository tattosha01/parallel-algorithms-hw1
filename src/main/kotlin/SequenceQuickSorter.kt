import kotlin.random.Random

class SequenceQuickSorter : Sorter() {
    override fun IntArray.sort(
        l: Int,
        r: Int,
    ) {
        if (r <= l) {
            return
        }

        val m = partition(this, l, r, Random::nextInt)
        sort(l, m)
        sort(m + 1, r)
    }
}
