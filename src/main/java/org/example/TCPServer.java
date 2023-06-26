package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        Warehouse.buildWineList();

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server avviato. In attesa di connessioni...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connessione stabilita con il client: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
