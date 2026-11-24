package com.projecte;

import java.util.ArrayList;
import java.util.List;

// Sala de prova mentre no hi ha base de dades (tasca 19)
public class DadesTestMapa {

    public static List<ZonaMapa> crearZones() {
        List<ZonaMapa> zones = new ArrayList<>();

        // Platea Esquerra: 8 files x 6 seients, columnes 0-5
        ZonaMapa esquerra = new ZonaMapa("Platea Esquerra", "#3498db", 0, 0, 8, 6);
        esquerra.getSeient(2, 3).vendre("Laura Puig", "laura@mail.com", "E1-10000001", "General");
        esquerra.getSeient(5, 1).vendre("Pau Roca", "pau@mail.com", "E1-10000002", "General");
        zones.add(esquerra);

        // General: 10 files x 14 seients, columnes 8-21 (pas a les columnes 6 i 7)
        ZonaMapa central = new ZonaMapa("General", "#27ae60", 0, 8, 10, 14);
        central.getSeient(1, 7).vendre("Marta Vidal", "marta@mail.com", "E1-10000003", "General");
        central.getSeient(1, 8).vendre("Joan Vidal", "joan@mail.com", "E1-10000004", "General");
        central.getSeient(4, 10).vendre("Núria Soler", "nuria@mail.com", "E1-10000005", "General");
        central.getSeient(10, 2).vendre("Oriol Mas", "oriol@mail.com", "E1-10000006", "General");
        zones.add(central);

        // Platea Dreta: 8 files x 6 seients, columnes 24-29 (pas a les columnes 22 i 23)
        ZonaMapa dreta = new ZonaMapa("Platea Dreta", "#e67e22", 0, 24, 8, 6);
        dreta.getSeient(3, 4).vendre("Clara Font", "clara@mail.com", "E1-10000007", "General");
        dreta.getSeient(8, 6).vendre("Biel Serra", "biel@mail.com", "E1-10000008", "General");
        
        zones.add(dreta);

        // Zona general: sense seients, només aforament i entrades venudes
        zones.add(new ZonaMapa("Dempeus", "#9b59b6", 40, 15));

        return zones;
    }
}