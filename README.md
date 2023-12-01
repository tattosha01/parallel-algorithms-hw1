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

Generation of input[0] with size = 100000000 started... | now = 19:18:48<br/>
Generation of input[0] with size = 100000000 finished   | now = 19:18:48<br/>
Sortings of input[0] with size = 100000000 started... | now = 19:20:33<br/>
Sortings of input[0] with size = 100000000 finished   | now = 19:22:29<br/>

Generation of input[1] with size = 100000000 started... | now = 19:22:29<br/>
Generation of input[1] with size = 100000000 finished   | now = 19:22:29<br/>
Sortings of input[1] with size = 100000000 started... | now = 19:22:29<br/>
Sortings of input[1] with size = 100000000 finished   | now = 19:24:15<br/>

Generation of input[2] with size = 100000000 started... | now = 19:24:15<br/>
Generation of input[2] with size = 100000000 finished   | now = 19:24:15<br/>
Sortings of input[2] with size = 100000000 started... | now = 19:24:15<br/>
Sortings of input[2] with size = 100000000 finished   | now = 19:26:11<br/>

Generation of input[3] with size = 100000000 started... | now = 19:26:11<br/>
Generation of input[3] with size = 100000000 finished   | now = 19:26:12<br/>
Sortings of input[3] with size = 100000000 started... | now = 19:26:12<br/>
Sortings of input[3] with size = 100000000 finished   | now = 19:27:51<br/>

Generation of input[4] with size = 100000000 started... | now = 19:27:51<br/>
Generation of input[4] with size = 100000000 finished   | now = 19:27:51<br/>
Sortings of input[4] with size = 100000000 started... | now = 19:27:51<br/>
Sortings of input[4] with size = 100000000 finished   | now = 19:29:38<br/>

Sequence | ParallelByFilter | ParallelBySeqAndForkJoin<br/>
11.852s  | 98.873s          | 3.553s<br/>
11.719s  | 88.915s          | 3.605s<br/>
11.829s  | 99.855s          | 3.402s<br/>
11.785s  | 82.907s          | 3.429s<br/>
12.096s  | 89.019s          | 3.989s<br/>

avg time (sequence) = 11.856s<br/>
avg time (filtered) = 91.914s<br/>
avg time (forkJoin) = 3.595s<br/>
