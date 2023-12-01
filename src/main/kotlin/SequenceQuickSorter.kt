import kotlin.random.Random

class SequenceQuickSorter : Sorter() {
    override fun IntArray.sort(
        l: Int,
        r: Int,
    ) {
        seqSort(this, l, r, Random::nextInt)
    }
}
