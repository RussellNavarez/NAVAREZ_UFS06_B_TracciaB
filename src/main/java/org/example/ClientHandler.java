package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientHandler extends Thread {
    private final Socket socket;
    private static List<Wine> wines = Warehouse.getWines();
    private static Gson gson = new GsonBuilder().create();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connessione stabilita con il client: " + socket);

            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println("Richiesta ricevuta: " + request);

                String[] commands = request.split(";"); // Dividi i comandi utilizzando il carattere di delimitazione ";"
                StringBuilder responseBuilder = new StringBuilder();

                for (String command : commands) {
                    String response = processCommand(command.trim()); // Elabora il singolo comando
                    responseBuilder.append(response).append("\n");
                }

                String response = responseBuilder.toString();
                writer.println(response);
                System.out.println("Risposta inviata al client: " + response);
            }

            socket.close();
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
            default:
                response = "Comando non valido: " + command;
                break;
        }
        return response;
    }

    private String getWinesByType(String type) {
        List<Wine> filteredWines = new ArrayList<>();
        for (Wine wine : wines) {
            if (wine.getType().equalsIgnoreCase(type)) {
                filteredWines.add(wine);
            }
        }
        return buildJSONResponse(filteredWines);
    }

    private String getWinesSortedByName() {
        List<Wine> sortedWines = new ArrayList<>(wines);
        sortedWines.sort(Comparator.comparing(Wine::getName));
        return buildJSONResponse(sortedWines);
    }

    private String getWinesSortedByPrice() {
        List<Wine> sortedWines = new ArrayList<>(wines);
        sortedWines.sort(Comparator.comparingDouble(Wine::getPrice));
        return buildJSONResponse(sortedWines);
    }

    private String buildJSONResponse(List<Wine> wines) {
        JsonObject jsonResponse = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (Wine wine : wines) {
            JsonObject jsonWine = new JsonObject();
            jsonWine.addProperty("id", wine.getId());
            jsonWine.addProperty("name", wine.getName());
            jsonWine.addProperty("price", wine.getPrice());
            jsonWine.addProperty("type", wine.getType());
            jsonArray.add(jsonWine);
        }

        jsonResponse.add("wines", jsonArray);
        return gson.toJson(jsonResponse);
    }
}
