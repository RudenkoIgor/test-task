package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int THREAD_POOL_SIZE = 10;

    private final int serverPort;
    private final String rootPath;
    private final ExecutorService executor;

    public Server(int serverPort, String rootPath) {
        this.serverPort = serverPort;
        this.rootPath = rootPath;
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Server started on port " + serverPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    out.println("Connected");
                int depth = Integer.parseInt(in.readLine());
                String mask = in.readLine();

                SearchService searchService = new SearchService();
                searchService.start(rootPath, depth, mask);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
