package com.sorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sorter {
    private List<String> pathsOfFiles;

    private final List<String> noRequireFiles = new ArrayList<>();
    private final List<String> requireFiles = new ArrayList<>();
    private final String targetString = "require '";


    public Sorter(List<String> paths) {
        if (paths != null) {
            this.pathsOfFiles = paths;
            Collections.sort(this.pathsOfFiles);
            System.out.println(this.pathsOfFiles);
        }
    }

    public List<String> sort() throws IOException {
        divideFiles();

        HashMap<Integer,String> supposeToBeAddedFiles = new HashMap<>();
        int i = 1;
        for (String rf : requireFiles) {
            List<String> dependedFiles = getDependedFiles(rf);

            for (String nrf : noRequireFiles) {
                if(nrf.contains(dependedFiles.get(dependedFiles.size()-1))){
                    supposeToBeAddedFiles.put(noRequireFiles.indexOf(nrf) + i, rf);
                    i+=1;
                    break;
                }
            }
        }
        for (Integer index : supposeToBeAddedFiles.keySet()) {
            String file = supposeToBeAddedFiles.get(index);
            noRequireFiles.add(index,file);
        }


        return correctPathsNames(noRequireFiles);
    }

    private void divideFiles() throws IOException {
        for (String path : this.pathsOfFiles) {
            if(Files.lines(Paths.get(path)) != null) {
                if (Files.lines(Paths.get(path)).anyMatch(line -> line.contains(targetString))) {
                   this.requireFiles.add(path);
                } else {
                    this.noRequireFiles.add(path);
                }
            }
        }
    }

    public static List<String> correctPathsNames(List<String> paths){
        List<String> changedList = new ArrayList<>();

        for (String p : paths) {
            int index = p.indexOf("TZ")+"TZ\\".length();
            changedList.add(p.substring(index));
        }
        return changedList;
    }

    public static List<String> getDependedFiles(String path){
        String line;
        List<String> requireFiles = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("File [1-9](-[1-9])*");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) requireFiles.add(matcher.group(0));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return requireFiles;
    }
}
