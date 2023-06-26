package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.net.SocketException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final List<Wine> wines;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ClientHandler(Socket socket, List<Wine> wines) {
        this.socket = socket;
        this.wines = wines;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            //System.out.println("Connessione stabilita con il client: " + socket);  PRESENTE GIA' NEL TCP SERVER

            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println("Richiesta ricevuta: " + request);

                String[] commands = request.split(";");
                StringBuilder responseBuilder = new StringBuilder();

                for (String command : commands) {
                    String response = processCommand(command.trim());
                    responseBuilder.append(response).append("\n");
                }

                String response = responseBuilder.toString();
                writer.println(response);
                System.out.println("Risposta inviata al client: " + response);
            }

            socket.close();
        } catch (SocketException e) {
            // Ignora l'eccezione quando la connessione viene resettata
            System.out.println("Connessione chiusa dal client.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processCommand(String command) {
        String response;
        switch (command) {
            case "red":
                response = getWinesByType("red");
                break;
            case "white":
                response = getWinesByType("white");
                break;
            case "sorted_by_name":
                response = getWinesSortedByName();
                break;
            case "sorted_by_price":
                response = getWinesSortedByPrice();
                break;
            case "stop":
                response = "Server stopped";
                stopServer();
                break;
            default:
                response = "Comando non valido: " + command;
                break;
        }
        return response;
    }

    private void stopServer() {
        try {
            socket.close(); // Chiude la connessione del socket nel ClientHandler
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getWinesByType(String type) {
        StringBuilder responseBuilder = new StringBuilder();

        for (Wine wine : wines) {
            if (wine.getType().equalsIgnoreCase(type)) {
                String json = gson.toJson(wine);
                responseBuilder.append(json).append("\n");
            }
        }

        return responseBuilder.toString();
    }

    private String getWinesSortedByName() {
        wines.sort((wine1, wine2) -> wine1.getName().compareToIgnoreCase(wine2.getName()));
        StringBuilder responseBuilder = new StringBuilder();

        for (Wine wine : wines) {
            String json = gson.toJson(wine);
            responseBuilder.append(json).append("\n");
        }

        return responseBuilder.toString();
    }

    private String getWinesSortedByPrice() {
        wines.sort(Comparator.comparingDouble(Wine::getPrice));
        StringBuilder responseBuilder = new StringBuilder();

        for (Wine wine : wines) {
            String json = gson.toJson(wine);
            responseBuilder.append(json).append("\n");
        }

        return responseBuilder.toString();
    }
}
