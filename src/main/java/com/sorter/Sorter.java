package com.sorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sorter {
    private final List<String> pathsOfFiles;
    private final List<String> noRequireFiles = new ArrayList<>();
    private final List<String> requireFiles = new ArrayList<>();


    public Sorter(List<String> paths) {
            this.pathsOfFiles = paths;
            Collections.sort(this.pathsOfFiles);
            System.out.println(this.pathsOfFiles);
    }

    public List<String> sort() throws IOException {
        divideFiles();
        if(!isCycle(this.requireFiles)) {
            Map<Integer, String> supposeToBeAddedFiles = new HashMap<>();
            int i = 1;
            for (String rf : requireFiles) {
                List<String> dependedFiles = getDependedFiles(rf);

                for (String nrf : noRequireFiles) {
                    if (nrf.contains(dependedFiles.get(dependedFiles.size() - 1))) {
                        supposeToBeAddedFiles.put(noRequireFiles.indexOf(nrf) + i, rf);
                        i += 1;
                        break;
                    }
                }
            }
            for (Integer index : supposeToBeAddedFiles.keySet()) {
                String file = supposeToBeAddedFiles.get(index);
                noRequireFiles.add(index, file);
            }
        }


        return correctPathsNames(noRequireFiles);
    }

    private void divideFiles() throws IOException {
        for (String path : this.pathsOfFiles) {
            if(Files.lines(Paths.get(path)) != null) {
                if (Files.lines(Paths.get(path)).anyMatch(line -> line.contains("require '"))) {
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
        List<String> dependedFiles = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            while ((line = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("File [1-9](-[1-9])*");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) dependedFiles.add(matcher.group(0));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        return dependedFiles;
    }
    private boolean isCycle(List<String> requireFiles){
        List<RequireFile> files = new ArrayList<>();
        for (String path : requireFiles) {
            files.add(new RequireFile(path));
        }

        for (RequireFile rf : files) {
            for (RequireFile rf1 : files) {
                for (String dependedFile : rf1.getRequireFiles()){
                    if (rf.getPath().contains(dependedFile)){
                        System.err.println("Error the cycle dependence was found among files: \n" + rf.getPath() + ", " + rf1.getPath());
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
