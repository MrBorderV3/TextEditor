package me.border.texteditor.file;

import me.border.utilities.file.AbstractSerializedFile;

import java.io.File;

public class CacheFile extends AbstractSerializedFile<String> {
    public CacheFile(String file, File path, String item) {
        super(file, path, item);
    }
}
