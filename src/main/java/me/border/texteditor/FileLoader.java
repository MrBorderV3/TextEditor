package me.border.texteditor;

import me.border.texteditor.file.CacheFile;

import java.io.*;
import java.util.Scanner;

public class FileLoader {

    private final static File dir = new File(System.getProperty("user.home") + File.separator + "TextEditor");

    public static String loadFile(File file) throws IOException {
        if (!file.exists()) {
            throw new NullPointerException("File " + file.toString() + " does not exist!");
        }

        Scanner reader = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while (reader.hasNextLine()){
            sb.append(reader.nextLine());
            sb.append(System.getProperty("line.separator"));
        }

        reader.close();
        return sb.toString();
    }

    public static void save(File file, String content) throws IOException {
        if (!file.exists()){
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }

    public static void cache(File file){
        String path = file.getAbsolutePath();
        CacheFile cacheFile = new CacheFile("cache", dir, path);
        cacheFile.setup();
        cacheFile.setItem(path);
        cacheFile.save();
    }

    public static boolean cacheExists(){
        if (!dir.exists()){
            return false;
        }

        CacheFile cacheFile = new CacheFile("cache", dir, null);
        return cacheFile.getFile().exists();
    }

    public static File openCache(){
        CacheFile cacheFile = new CacheFile("cache", dir, null);
        cacheFile.setup();

        return new File(cacheFile.getItem());
    }
}
