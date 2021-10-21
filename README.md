Задача. Русские шашки.
-

Дана композиция начала или середины партии русских шашек. Далее даётся список из 1 или нескольких ходов. На выходе нужно вывести итоговую композицию или сообщение об ошибке если какой-то из ходов списка невозможен.
В данной реализации шашки могут не только бить, но и ходить назад.
Типы сообщений об ошибке:

- busy cell - целевая клетка занята
- white cell - целевая клетка белая (шашки расставляются только на чёрные и, в силу правил, оказаться на белых не могут),
- invalid move - в шашках бить обязательно. Причём, бить надо всю цепочку до конца. Ошибка выводится в том случае если у игрока есть вариант побить шашку, но он его не использует, а идёт на другую клетку. Если вариантов боя несколько, можно взять любой.
- error - другие ошибки.

Все исходные позиции - валидные.
