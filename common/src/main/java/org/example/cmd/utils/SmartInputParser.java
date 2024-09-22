package org.example.cmd.utils;

import org.example.interfaces.Console;

public class SmartInputParser {
    public static String parseToString(Console console, String fieldName, int lowerLengthLimit, boolean isRec) throws Ask.AskBreak {
        String value;
        while (true) {
            if(!isRec)console.print(fieldName + ": ");
            value = console.readln().trim();
            if (!value.isEmpty()) {
                if (value.equals("exit")) throw new Ask.AskBreak();
                else if (value.equals("null")) console.printWarning("Не может быть null");
                else if (value.length() >= lowerLengthLimit) break;
                else {
                    if(!isRec)console.printWarning("Введите строку длиной " + lowerLengthLimit + " или более");
                }
            }
            else {
                if(!isRec)console.printWarning("Введите непустое значение");
            }
        }
        return value;
    }

    public static String parseToString(Console console, String fieldName, boolean isRec) throws Ask.AskBreak {
        return parseToString(console, fieldName, 1, isRec);
    }

    public static long parseToLong(Console console, String fieldName, boolean moreThanZero, boolean isRec) throws Ask.AskBreak {
        String value;
        long answer;
        while (true){
            if(!isRec)console.print(fieldName + ": ");
            value = console.readln().trim();
            if (!value.isEmpty()) {
                if(value.equals("exit")) throw new Ask.AskBreak();
                else if (value.equals("null")) console.printWarning("Не может быть null");
                else
                    try {
                        answer = Long.parseLong(value);
                        if (moreThanZero){
                            if (answer > 0) break;
                            else {
                                if(!isRec)console.printWarning("Введите число больше 0");
                            }
                        }else break;
                    }
                    catch(NumberFormatException e) {
                        if(!isRec)console.printWarning("Число неверного формата");
                    }
            } else {
                if(!isRec)console.printWarning("Введите непустое значение");
            }
        }
        return answer;
    }
    public static int parseTo_int(Console console, String fieldName, boolean moreThanZero, boolean isRec) throws Ask.AskBreak {
        String value;
        int answer;
        while (true){
            if(!isRec)console.print(fieldName + ": ");
            value = console.readln().trim();
            if (!value.isEmpty()) {
                if(value.equals("exit")) throw new Ask.AskBreak();
                else if (value.equals("null")) {
                    console.printWarning("Не может быть null");
                }
                else
                    try {
                        answer = Integer.parseInt(value);
                        if (moreThanZero){
                            if (answer > 0) break;
                            else {
                                if(!isRec)console.printWarning("Введите число больше 0");
                            }
                        }else break;
                    }
                    catch(NumberFormatException e) {
                        if(!isRec)console.printWarning("Число неверного формата");
                    }
            } else {
                if(!isRec)console.printWarning("Введите непустое значение");
            }
        }
        return answer;
    }


}
