package com.janaza.Services;

import com.janaza.Models.Janaza;
import java.util.ArrayList;

public class JanazaManager {

    protected static JanazaManager INSTANCE = null;
    private ArrayList<Janaza> collection = new ArrayList<>();

    protected JanazaManager() {

    }

    public static JanazaManager getInstance() {
        if (INSTANCE == null) {
            synchronized (JanazaManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JanazaManager();
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<Janaza> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<Janaza> collection) {
        this.collection = collection;
    }


    public void addJanaza(Janaza janaza) {
        this.collection.add(janaza);
    }
}
