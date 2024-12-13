package com.sorter;

import java.util.List;

public class RequireFile {
    private final String path;
    private final List<String> requireFiles;

    public RequireFile(String path){
        this.path = path;
        this.requireFiles = Sorter.getDependedFiles(path);
    }

    public String getPath() {
        return path;
    }

    public List<String> getRequireFiles() {
        return requireFiles;
    }

}
