package com.solver;

import com.searcher.Searcher;
import com.sorter.Sorter;

import java.io.IOException;

public class Solver {
    public static void main(String[] args) throws IOException {
        Searcher searcherOfFiles = new Searcher("C:/Users/lenovo/Desktop/TZ");
        Sorter sorterOfFiles = new Sorter(searcherOfFiles.search());
        System.out.println(sorterOfFiles.sort());
    }
}
