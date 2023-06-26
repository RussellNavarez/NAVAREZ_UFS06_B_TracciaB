package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class TCPServer {
    private static final int PORT = 5000;
    private static ServerSocket serverSocket;
    private static boolean isRunning;

    public static void main(String[] args) {
        List<Wine> wines = Inventory.getWines();
        isRunning = true;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server avviato. In attesa di connessioni...");

            while (isRunning) {
                Socket socket = serverSocket.accept();
                System.out.println("Connessione stabilita con il client: " + socket);
                System.out.println("Comandi:");
                System.out.println("-red -> Visualizza i vini rossi");
                System.out.println("-white -> Visualizza i vini bianchi");
                System.out.println("-sorted_by_name -> Visualizza i vini in ordine alfabetico");
                System.out.println("-sorted_by_price -> Visualizza i vini in ordine crescente di prezzo");
                System.out.println("-stop -> Chiude il programma");

                ClientHandler clientHandler = new ClientHandler(socket, wines);
                clientHandler.start();
            }
        } catch (SocketException e) {
            // Ignora l'eccezione quando la connessione viene resettata
            System.out.println("Connessione chiusa dal client.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
