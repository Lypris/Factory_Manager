package fr.insa.trenchant_troullier_virquin.applicationwebm3.model;

import java.sql.Timestamp;

public class Status_Operateur {
    //cette classe permet de faire le lien entre les états possibles d'un opérateur et l'état actuel de l'opérateur
    private String name;
    //private Timestamp debut, fin;

    public Status_Operateur(String name/*, Timestamp debut, Timestamp fin*/) {
        this.name = name;
        //this.debut = debut;
        //this.fin = fin;
    }

    public String getName() {
        return name;
    }

    public void setNom(String name) {
        this.name = name;
    }

    //
}
