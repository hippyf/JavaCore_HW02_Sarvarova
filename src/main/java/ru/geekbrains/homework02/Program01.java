package ru.geekbrains.homework02;

import java.util.Random;
import java.util.Scanner;

/**
 * Переписала программу с семинара и немного доработала:
 * - программа для игры на поле любого размера(+ отрисовка поля с табуляцией);
 * - первый ход определяется рандомно;
 * - компьютер мешает человеку выиграть;
 * - победа при количестве фишек как в стандартой игре: должен быть заполнен столбец, строка или диагональ(если поле-квадрат)
 */
public class Program01 {
    private static final char DOT_HUMAN = 'X'; // final - аналог константы; DOT_HUMAN - фишка игрока
    private static final char DOT_AI = 'O'; // фишка компьютера
    private static  final  char DOT_EMPTY = '•'; // пустая ячейка
    private static  final Scanner SCANNER = new Scanner(System.in);
    private static char [] [] field; // двумерный массив, хранит текущее состояние игрового поля
    private static  final Random random = new Random(); // рандомизировать ход компьютера
    private  static  int fieldSizeX; // размерность игрового поля
    private static  int fieldSizeY; // размерность игрового поля


    public static void main(String[] args) {
        while (true) {
            initialSize();
            printField();
            System.out.println();
            startGame();
            System.out.println("Желаете сыграть еще раз? (y - да, n - нет) ");
            if ((!SCANNER.next().equalsIgnoreCase("Y"))) {
                SCANNER.close();
                break;

            }
        }
    }

    /**
     * Инициализация игрового поля
     */
    private static void  initialSize() {
        // Установим размероность игрового поля
        fieldSizeX = 5;
        fieldSizeY = 5;

        field = new char[fieldSizeX][fieldSizeY];
        // инициализируем ячейки, пока пустым значением - точкой. Пройдем по всем элементам массива
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     *  Отрисовка игрового поля
     */
    private  static  void  printField() {

        System.out.print("+\t");
        for (int i = 0; i < fieldSizeX * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? "-\t" : (i / 2 + 1)+ "\t");
        }
        System.out.println();

        for (int i = 0; i < fieldSizeY; i++) {
            System.out.print(i + 1 + "\t" + "|\t");
            for (int j = 0; j < fieldSizeX; j++) {
                System.out.print(field[j][i] + "\t" + "|\t");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-\t");
        }
        System.out.println();
    }

    /**
     * Определение очерёдности хода
     * @return 0 -человек, 1 - компьютер.
     */
    private static int whoseTurn() {
        int x = random.nextInt(2);
        return x;
    }

    /**
     * Начало игры
     */
    private  static void startGame() {
        int x = whoseTurn();
        if (x == 0) {
            System.out.println("Первым ходите Вы");
            while (true) { //бесконечный цикл (if - проверка на завершение, break - завершение цикла)
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!")) break;
                aiTurnWithIntellect();
                printField();
                if (gameCheck(DOT_AI, "Победил компьютер :(")) break;
            }
        }
        else {
            System.out.println("Первым ходит компьютер");
            while (true) { //бесконечный цикл (if - проверка на завершение, break - завершение цикла)
                aiTurnWithIntellect();
                printField();
                if (gameCheck(DOT_AI, "Победил компьютер :(")) break;
                humanTurn();
                printField();
                if (gameCheck(DOT_HUMAN, "Вы победили!")) break;
            }
        }
    }

    /**
     * Обраотка хода игрока (человек)
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.print("Введите координаты хода X и Y (от 1 до 3) через пробел >>> ");
            x = SCANNER.nextInt() -1;
            y = SCANNER.nextInt() -1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y)); // сначала проверяем не вышли ли координаты за рамки поля
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Обработка хода компьютера (метод с семинара)
     */
    private static void aiTurn () {
        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y)); // проверяем только на пустоту ячейки, т.к. валидность была задана условием рандома
        field[x][y] = DOT_AI;

    }

    /**
     * Обработка хода компьютера с небольшим интеллектом
     */
    private static void aiTurnWithIntellect() {
        int count = 0;
        int exit = 0;
        // Проверка: игровое поле квадрат или прямоугольник
        if (fieldSizeX == fieldSizeY) { // если квадрат, то есть выигрыш по диагонали
            for (int y = 0; y < fieldSizeY; y++) { // проверка по горизонтали
                for (int x = 0; x < fieldSizeX; x++) {
                    if (field[x][y] == DOT_HUMAN) count = count +1;
                }
                if (count == fieldSizeX-1 && exit != 1) {
                    int x = 0;
                    while (x < fieldSizeX) {
                        if (isCellEmpty(x, y)) {
                            field[x][y] = DOT_AI;
                            exit = 1;
                            x = fieldSizeX;
                            y = fieldSizeY;
                        } else x = x +1;
                    }
                } else count = 0;
            }
            for (int x = 0; x < fieldSizeX; x++) { // проверка по вертикали
                for (int y = 0; y < fieldSizeY; y++) {
                    if (field[x][y] == DOT_HUMAN) count = count +1;
                }
                if (count == fieldSizeX-1 && exit != 1) {
                    int y = 0;
                    while (y < fieldSizeY) {
                        if (isCellEmpty(x, y)) {
                            field[x][y] = DOT_AI;
                            exit = 1;
                            y = fieldSizeY;
                            x = fieldSizeX;
                        } else y = y +1;
                    }
                } else count = 0;
            }

            for (int x = 0; x < fieldSizeX; x++) { // проверка по диагонали 1
                if (field[x][x] == DOT_HUMAN) count = count +1;
            }
            if (count == fieldSizeX -1 && exit != 1) {
                int y = 0;
                while (y < fieldSizeY) {
                    if (isCellEmpty(y,y)) {
                        field[y][y] = DOT_AI;
                        exit = 1;
                        y = fieldSizeY;
                    } else y = y + 1;
                }
            }

            int x = fieldSizeX -1; // проверка по диагонали 2
            int y = 0;
            count = 0;
            while (x >= 0) {
                if (field[x][y] == DOT_HUMAN){
                    count = count +1;
                }
                x = x -1;
                y = y +1;
            }
            if (count == fieldSizeX - 1 && exit != 1) {
                x = fieldSizeX -1;
                y =0;
                while (y < fieldSizeY) {
                    if (isCellEmpty(x, y)) {
                        field[x][y] = DOT_AI;
                        exit = 1;
                        y = fieldSizeY;
                    } else {
                        y = y +1;
                        x = x -1;
                    }
                }
            } if (exit != 1){ // если нет возможноти выигрыша человеком в 1 ход
                    do {
                        x = random.nextInt(fieldSizeX);
                        y = random.nextInt(fieldSizeY);
                    }
                    while (!isCellEmpty(x, y)); // проверяем только на пустоту ячейки, т.к. валидность была задана условием рандома
                    field[x][y] = DOT_AI;
            }
        }
        else { // если прямоугольник, то по диагонали нет выигрыша
            exit = 0;
            for (int y = 0; y < fieldSizeY; y++) { // проверка по горизонтали
                for (int x = 0; x < fieldSizeX; x++) {
                    if (field[x][y] == DOT_HUMAN) count = count +1;
                }
                if (count == fieldSizeX-1 && exit != 1) {
                    int x = 0;
                    while (x < fieldSizeX) {
                        if (isCellEmpty(x, y)) {
                            field[x][y] = DOT_AI;
                            exit = 1;
                            x = fieldSizeX;
                            y = fieldSizeY;
                        } else x = x +1;
                    }
                } else count = 0;
            }

            for (int x = 0; x < fieldSizeX; x++) { // проверка по вертикали
                for (int y = 0; y < fieldSizeY; y++) {
                    if (field[x][y] == DOT_HUMAN) count = count +1;
                }
                if (count == fieldSizeY-1 && exit != 1) {
                    int y = 0;
                    while (y < fieldSizeY) {
                        if (isCellEmpty(x, y)) {
                            field[x][y] = DOT_AI;
                            exit = 1;
                            x = fieldSizeX;
                            y = fieldSizeY;
                        } else y = y +1;
                    }
                } else count = 0;
            }
            if (exit != 1){ // если нет возможноти выигрыша человеком в 1 ход
                int x,y;
                do {
                    x = random.nextInt(fieldSizeX);
                    y = random.nextInt(fieldSizeY);
                }
                while (!isCellEmpty(x, y)); // проверяем только на пустоту ячейки, т.к. валидность была задана условием рандома
                field[x][y] = DOT_AI;
            }
        }

    }

    /**
     * Проверка, является ли ячейка пустой
     * @param x координата x
     * @param y координата y
     * @return true or false
     */
    static boolean isCellEmpty (int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка корректности ввода (координаты хода не должны превышать размерность массива игрового поля)
     * @param x координата x
     * @param y координата y
     * @return true or false
     */
    static  boolean isCellValid (int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка победы
     * @param c - фишка игрока или компьютера
     * @return true or false
     */
    static  boolean checkWin (char c) {
        int count = 0;
        // Проверка: игровое поле квадрат или прямоугольник
        if (fieldSizeX == fieldSizeY) { // если квадрат, то есть выигрыш по диагонали
            for (int y = 0; y < fieldSizeY; y++) { // проверка по горизонтали
                for (int x = 0; x < fieldSizeX; x++) {
                    if (field[x][y] == c) count = count +1;
                    else x = fieldSizeX -1; // выходим из цикла
                }
                if (count == fieldSizeX) return true;
                else count = 0;
            }
            for (int x = 0; x < fieldSizeX; x++) { // проверка по вертикали
                for (int y = 0; y < fieldSizeY; y++) {
                    if (field[x][y] == c)  count = count +1;
                    else y = fieldSizeY - 1;  // выходим из цикла
                }
                if (count == fieldSizeY) return true;
                else count = 0;
            }

            for (int x = 0; x < fieldSizeX; x++) { // проверка по диагонали 1
                if (field[x][x] == c) count = count +1;
                else {
                    x = fieldSizeX -1; // выходим из цикла
                    count = 0;
                }
            }
            if (count == fieldSizeX) return true;

            int x = fieldSizeX -1; // проверка по диагонали 2
            int y = 0;
            while (x >= 0) {
                if (field[x][y] == c) count = count +1;
                else x = -1; // выходим из условия
                x = x -1;
                y = y +1;
                if (count == fieldSizeX) return true;
            }
        }
        else { // если прямоугольник, то не существует выигрыша по диагонали
            for (int y = 0; y < fieldSizeY; y++) { // проверка по горизонтали
                for (int x = 0; x < fieldSizeX; x++) {
                    if (field[x][y] == c) count = count +1;
                    else x = fieldSizeX -1; // выходим из цикла
                }
                if (count == fieldSizeX) return true;
                else count = 0;
            }
            for (int x = 0; x < fieldSizeX; x++) { // проверка по вертикали
                for (int y = 0; y < fieldSizeY; y++) {
                    if (field[x][y] == c)  count = count +1;
                    else y = fieldSizeY - 1;  // выходим из цикла
                }
                if (count == fieldSizeY) return true;
                else count = 0;
            }
        }
        return false;
    }

        /**
         * Проверка на ничью
         * @return true or false
         */
        static boolean checkDraw () {
            for (int x = 0; x < fieldSizeX; x++) {
                for (int y = 0; y < fieldSizeY; y++) {
                    if (isCellEmpty(x, y)) return false;
                }
            }
            return true;
        }


        /**
         * Метод проверки состояния игры
         * @param c - фишка игрока или компьютера
         * @param str текст для победителя
         * @return true or false
         */
        static boolean gameCheck ( char c, String str){
            if (checkWin(c)) {
                System.out.println(str);
                return true;
            }
            if (checkDraw()) {
                System.out.println("Ничья");
                return true;
            }
            return false; // Игра продолжается
        }
    }
