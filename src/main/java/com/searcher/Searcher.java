package com.searcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Searcher {
    private final Path mainDirectory;

    public Searcher(String pathToMainDr){
        this.mainDirectory = Paths.get(pathToMainDr);
    }

    public List<String> search() {
        try {
            return Files.walk(this.mainDirectory)
                    .filter(Files::isRegularFile) // Фильтруем только обычные файлы
                    .map(Path::toString) // Преобразуем Path в String
                    .filter(s -> s.toLowerCase().endsWith(".txt")) // Фильтруем только .txt файлы
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ошибка при поиске файлов: " + e.getMessage());
        }
        return null;
    }

}
