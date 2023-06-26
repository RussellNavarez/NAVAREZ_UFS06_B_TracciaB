package org.example;

import java.util.ArrayList;
import java.util.List;

public class Warehouse extends TCPServer {
    private static List<Wine> wines = new ArrayList<>();

    public static List<Wine> getWines() {
        return wines;
    }

    public static void buildWineList() {
        wines.add(new Wine(13, "Dom Perignon Vintage Moet & Chandon 2008", 225.94, "white"));
        wines.add(new Wine(14, "Pignoli Radikon Radikon 2009", 133.0, "red"));
        wines.add(new Wine(124, "Pinot Nero Elena Walch Elena Walch 2018", 43.0, "red"));
        System.out.println(wines);
    }
}
