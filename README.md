# x3p1
Индивидуальный проект для ОС Android версии 8.0 и выше для демонстрации навыков разработки и подтверждения компетенции педагога ИТ школы Samsung.

## Обязательные требования
- [x] Необходимо использовать многопоточность
  Пользователь управляет запуском двух независимых потоков, производящих вычисления.
- [x] Необходимо использовать БД + любую стороннюю библиотеку ИЛИ сервисы Firebase
  Использована встроенная БД SQLite.
  Использованы сторонние бибилиотеки [libGDX](https://libgdx.com/) и [Box2D](https://box2d.org/)
- [x] В интерфейсе одной из активностей/фрагментов должен быть реализован список ListView/RecyclerView
  Реализован список RecyclerView, отображающий результаты работы потоков, производящих вычисления.

## Дополнительная функциональность
- [x] Физически корректная симуляция "пузырьков в коробке"
- [x] Пузырьки реагируют на резкие движения телефона (задействован акселерометр)
- [x] Переключение между экранами приложения осуществляется с помощью `TabLayout`
- [x] Приложение реагирует на интуитивные жесты (свайп влево/вправо)
- [x] Приложение переходит в альбомный режим при повороте экрана (все элементы масштабируются)
- [x] UI в режиме реального времени отображает прогресс независимых потоков, производящих вычисления

## Что приложение делает?
В основе приложения лежат вычисления, построенные на правиле "3n+1", более известном как [Collatz conjecture](https://en.wikipedia.org/wiki/Collatz_conjecture) (youtube: [Veritasium](https://www.youtube.com/watch?v=094y1Z2wpJg).

)