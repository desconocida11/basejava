package com.basejava.webapp;


public class MainString {
    public static void main(String[] args) {
        String[] strArray = new String[]{"1", "2", "3", "4", "5"};
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str).append(", ");
        }
        System.out.println(sb.toString());

        String str1 = "abc";
        String str5 = new String("abc");
        String str3 = "c";
        String str2 = "ab" + str3;
        String str4 = ("ab" + str3).intern();
        System.out.println(str1.equals(str4));
        System.out.println(str1.equals(str5));
    }
}
