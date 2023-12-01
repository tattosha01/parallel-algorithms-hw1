Практическое задание #1.

Quicksort.
Требуется написать последовательную версию алгоритма (seq) и параллельную версию (par).
Для тестирования взять случайный массив из 10^8 элементов. Усреднить по пяти запускам.
Сравнить время работы par на 4 процессах и seq на одном процессе - должно быть хотя бы в три раза быстрее.

Параметры тестирования:<br/>
Количество потоков: 4<br/>
Размер блока после которого останавливаем параллелизацию задач сортировки: 3000 элементов<br/>
Размер блока для pfor: 2000<br/>
Размер блока на котором делаем BubbleSort (при последовательной QuickSort): 12<br/>

Результаты тестирования:

The warming up has started... | now = 23:48:08<br/>
The warming up has finished   | now = 23:50:06<br/>
Generation of input[0] with size = 100000000 started... | now = 23:50:06<br/>
Generation of input[0] with size = 100000000 finished   | now = 23:50:06<br/>
Sorting of input[0] with size = 100000000 started... | now = 23:50:06<br/>
Sorting of input[0] with size = 100000000 finished   | now = 23:51:44<br/>

Generation of input[1] with size = 100000000 started... | now = 23:51:44<br/>
Generation of input[1] with size = 100000000 finished   | now = 23:51:44<br/>
Sorting of input[1] with size = 100000000 started... | now = 23:51:44<br/>
Sorting of input[1] with size = 100000000 finished   | now = 23:53:19<br/>

Generation of input[2] with size = 100000000 started... | now = 23:53:19<br/>
Generation of input[2] with size = 100000000 finished   | now = 23:53:19<br/>
Sorting of input[2] with size = 100000000 started... | now = 23:53:19<br/>
Sorting of input[2] with size = 100000000 finished   | now = 23:54:53<br/>

Generation of input[3] with size = 100000000 started... | now = 23:54:53<br/>
Generation of input[3] with size = 100000000 finished   | now = 23:54:53<br/>
Sorting of input[3] with size = 100000000 started... | now = 23:54:53<br/>
Sorting of input[3] with size = 100000000 finished   | now = 23:56:25<br/>

Generation of input[4] with size = 100000000 started... | now = 23:56:25<br/>
Generation of input[4] with size = 100000000 finished   | now = 23:56:25<br/>
Sorting of input[4] with size = 100000000 started... | now = 23:56:25<br/>
Sorting of input[4] with size = 100000000 finished   | now = 23:57:58<br/>

Sequence | ParallelByFilter | ParallelBySeqAndForkJoin<br/>
10.609s  | 81.887s          | 3.234s<br/>
10.274s  | 80.200s          | 3.189s<br/>
10.079s  | 79.618s          | 3.349s<br/>
10.250s  | 77.290s          | 3.303s<br/>
10.343s  | 78.586s          | 3.344s<br/>
avg time (sequence) = 10.311s<br/>
avg time (filtered) = 79.516s<br/>
avg time (forkJoin) = 3.284s<br/>
