package org.example.interfaces;

import java.util.Scanner;

/**
 * Интерфейс класса, реализующего функционал консоли ввода-вывода
 */
public interface Console {
    /**
     * Вывод текста белым цветом
     * @param obj
     */
    void print(Object obj);

    /**
     * Вывод текста белым цветом и перенос строки
     * @param obj
     */
    void println(Object obj);

    /**
     * Считывание строки до ближайшего разделителя
     * @return считанная строка
     */
    String read();

    /**
     * Считывание строки целиком
     * @return считанная строка
     */
    String readln();

    /**
     * Проверка на возможность считывания (наличие информации)
     * @return true - есть информация для считывания, false - ввод пуст
     */
    boolean isCanReadln();

    /**
     * Вывод текста-предупреждения жёлтым цветом
     * @param obj
     */
    void printWarning(Object obj);

    /**
     * Вывод текста-сообщения об успехе зелёным цветом
     * @param o
     */
    void printSuccessful(Object o);

    /**
     * Вывод текста-ошибки красным цветом
     * @param o
     */
    void printError(Object o);

    /**
     * Вывод символа $
     */
    void prompt();

    /**
     * Переключить объект в режим считывания из файла
     * @param obj путь к файлу
     */
    void selectFileScanner(Scanner obj);

    /**
     * Вернуть объект в режим считывания из консоли
     */
    void selectConsoleScanner();
}
