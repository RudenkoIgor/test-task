package org.example;

public class Main {

    public static void main(String[] args) {

        SearchService searchService = new SearchService();

        String path = "D:\\Program files\\Projects";
        String mask = "gradle";
        int depth = 3;

//        searchService.start(path,depth,mask);
        Server server = new Server(8080,path);
        server.start();
    }
}