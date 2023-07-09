package com.doantotnghiep.constant;

public class Alphabet {
    private static final String[] alphabet = {"A", "B", "C", "D", "E", "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T",
    "X","Y","Z","W"};

    public static String getPosition(int pos) {
        if (pos > 23|| pos < 0) return null;
        else {
          return alphabet[pos];
        }
    }
}
