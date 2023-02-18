package org.example;
import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.out;


public class SearchService {
    private static BlockingQueue<File> queue = new LinkedBlockingQueue<>();


    public void start(String rootPath, int depth, String mask){
        //searches for files and directories and adds them to the BlockingQueue
        Thread producerThread = new Thread(() -> {
            fileSearch(rootPath, depth, mask);
        });
        //retrieves elements from queue and prints them to the console
        Thread consumerThread = new Thread(() -> {
            while (true) {
                try {
                    File file = queue.take();
                    out.println(file.getAbsolutePath());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        producerThread.start();
        consumerThread.start();
    }


    private void fileSearch(String rootPath, int depth, String mask) {

        Stack<File> stack = new Stack<>();
        stack.push(new File(rootPath));

        while (!stack.isEmpty()) {
            File current = stack.pop();
            if (current.isDirectory()) {
                int currentDepth = current.getAbsolutePath().split("\\\\").length - rootPath.split("\\\\").length;
                if (currentDepth <= depth) {
                    for (File file : current.listFiles()) {
                        stack.push(file);
                    }
                }
            } else {
                if (current.getName().contains(mask)) {
                    System.out.println(current.getAbsolutePath());
                }
            }
        }
    }
}

