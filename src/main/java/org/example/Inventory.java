package org.example;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private static final List<Wine> wines = new ArrayList<>();

    static {
        buildWineList();
    }

    private static void buildWineList() {
        wines.add(new Wine(13, "Dom Perignon Vintage Moet & Chandon 2008", 225.94, "white"));
        wines.add(new Wine(14, "Pignoli Radikon Radikon 2009", 133.0, "red"));
        wines.add(new Wine(124, "Pinot Nero Elena Walch Elena Walch 2018", 43.0, "red"));
        System.out.println(wines);
    }

    public static List<Wine> getWines() {
        return wines;
    }
}
