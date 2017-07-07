# Projekt zaliczeniowy ASDII
## Wojciech Ozimek

*Implementacja w języku Java z wykorzystaniem biblioteki graficznej Swing*

## Struktura

Program oparty jest o wykorzystnie obiektów dwóch klas *komórki / Cell* oraz *labiryntu / Maze*

Komórka przechowuje współrzędne x oraz y, swój kolor oraz cztery ściany na brzegach.

Labirynt przechoduje tablice wszystkich komórek, przechowując dodatkowo pola oznaczające wymiary labiryntu (labirynt kwadratowy) oraz licznik zapobiegający zbyt łatwemu rozwiązaniu labiryntu.

## Generator labiryntu

Generator labiryntu opiera się o wykorzystanie algorytmu DFS, zaczynając od wybranej przez użytkownika komórki. Generator to faktycznie zrandomizowana wersja algorytmu przeszukiwania w głąb. Dla każdej komórki znajdowani są nieodwiedzeni sądziedzi, identyfikowani po kolorze. Jeśli liczba nieodwiedzonych sąsiadów wynosi więcej niż jeden, to algorytm losowo przestawia ich kolejność. Dla każdego z sąsiadów w nowej, losowej kolejności usuwa dzielącą komórki ścianę i rekurencyjnie wykonuje to samo dla danego sąsiada. Jeśli liczba sąsiadów jest równa zero, algorytm jest "wystarczająco daleko" od początku labiryntu a do tego dana komórka znajduje się na obrzeżu labiryntu, to miejsce oznaczane jest jako wyjście z labiryntu przez usunięcie jednej z zewnetrznych ścian.

## Rozwiązywanie labiryntu

Rozwiązywanie labiryntu również opiera się o wykorzystanie algorytmu DFS, tym razem jest on jeszce bardziej zmodyfikowany. Zaczynając od początkowej komórki, algorytm znajduje sąsiadów, nie patrząc jednak na kolor, ale na to czy dwie komórki sa rozdzielone ścianą. Jeśli nie są, algorytm traktuję tą komórkę jako swojego sąsiada. Dla każdego sąsiada rekurencyjnie wywoływana jest ta sama procedura znajdowania sąsiadów. Jeśli zajdzie sytuacja, że dana komórka nie ma sąsiadów, oznacza to że algo znaleźliśmy wyjście z labirytnu, albo algorytm doszedł do ścieżki bez wyjścia. W celu sprawdzenia, wywołujemy funkcje sprawdzającą czy dana komórka znajduje sie na brzegu oraz czy ściana zewnętrzna jest usunięta. Jeśli znaleźliśmy wyjście, algorytm zmienia wartość koloru dla wszystkich komórek po drodze (w kolejności od końca), dając tym samym możliwość narysowania ścieżki w labiryncie.
