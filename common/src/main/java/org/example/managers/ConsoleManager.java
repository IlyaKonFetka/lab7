package org.example.managers;

import org.example.interfaces.Console;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleManager implements Console {
    private static final String P = "$ ";

    private Scanner fileScanner;
    private static final Scanner defScanner = new Scanner(System.in);
    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public String read() throws NoSuchElementException, IllegalStateException {
        if (fileScanner != null){
            if (fileScanner.hasNext())
                return fileScanner.next();
            return "exit";
        }else return defScanner.next();
    }
    @Override
    public String readln() throws NoSuchElementException, IllegalStateException {
        if (fileScanner != null){
            if (fileScanner.hasNextLine())
                return fileScanner.nextLine();
            return "exit";
        }else return defScanner.nextLine();
    }

    @Override
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner != null ? fileScanner : defScanner).hasNextLine();
    }

    @Override
    public void printWarning(Object obj) {
        String brightYellowColorCode = "\u001B[33;1m";
        String resetColorCode = "\u001B[0m";
        System.out.println(brightYellowColorCode + obj + resetColorCode);
    }

    @Override
    public void printSuccessful(Object o) {
        String brightGreenColorCode = "\u001B[32;1m";
        String resetColorCode = "\u001B[0m";
        System.out.println(brightGreenColorCode + o + resetColorCode);
    }

    @Override
    public void printError(Object o) {
        String brightRedColorCode = "\u001B[31;1m";
        String resetColorCode = "\u001B[0m";
        System.out.println(brightRedColorCode + o + resetColorCode);
    }


    @Override
    public void prompt() {
        System.out.print(P);
    }


    @Override
    public void selectFileScanner(Scanner scanner) {
        fileScanner = scanner;
    }

    @Override
    public void selectConsoleScanner() {
        fileScanner = null;
    }

}