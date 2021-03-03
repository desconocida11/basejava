package com.basejava.webapp;

import java.io.File;
import java.io.IOException;

public class MainFile {

    private static void recursiveDirCall(File file, int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(' ');
        }
        if (file.isDirectory()) {
            System.out.println(sb.toString() + file.getName());
            File[] list = file.listFiles();
            if (list != null) {
                depth++;
                for (File fileName : list) {
                    recursiveDirCall(fileName, depth);
                }
            }
        } else {
            System.out.println(sb.toString() + file.getName());
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(".\\.gitignore");
        System.out.println(file.getCanonicalPath());
        File dir = new File("src");
        recursiveDirCall(dir, 0);
    }
}
