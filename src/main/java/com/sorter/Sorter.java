package com.sorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorter {
    private List<String> pathsOfFiles;
    private final String targetString = "require '";

    public Sorter(List<String> paths) {
        if (paths != null) {
            this.pathsOfFiles = paths;
            Collections.sort(this.pathsOfFiles);
        }
    }

    public List<String> sort() throws IOException {
        List<String> sortedPaths = new ArrayList<>();
        List<String> leftoverPs = new ArrayList<>();

        for (String path : this.pathsOfFiles) {
            if(Files.isReadable(Paths.get(path))) {

                if (Files.lines(Paths.get(path)).anyMatch(line -> line.contains(targetString))) {
                    leftoverPs.add(path);
//                    try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
//                        StringBuilder requireFiles = new StringBuilder();
//                        while (reader.readLine() != null) {
//
//                        }
//                    } catch (IOException e) {
//                        System.err.println("Ошибка при чтении файла: " + e.getMessage());
//                    }
                } else {
                    sortedPaths.add(path);
                }
            }
        }
        sortedPaths.addAll(leftoverPs);
        return changePathsNames(sortedPaths);
    }

    public static List<String> changePathsNames(List<String> paths){
        List<String> changedList = new ArrayList<>();
        for (String p:paths) {
            int index = p.indexOf("TZ")+"TZ\\".length();
            changedList.add(p.substring(index));
        }
        return changedList;
    }
}
