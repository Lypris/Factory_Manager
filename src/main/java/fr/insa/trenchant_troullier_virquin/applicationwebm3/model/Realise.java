/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.model;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.Lire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author laelt
 */
public class Realise {
    private int idmachine, idtypeoperation, duree;

    public Realise(int idmachine, int idtypeoperation, int duree) {
        this.idmachine = idmachine;
        this.idtypeoperation = idtypeoperation;
        this.duree = duree;
    }  
    
    public static Realise NouveauRealise(int idmachine, int idtypeoperation){
        System.out.println("Quelle durée pour l'operation :");
        int duree = Lire.i();
        return new Realise(idmachine, idtypeoperation,duree);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert realise2 (idmachine, idtype, duree) values (?,?,?)")) {
            pst.setString(1, String.valueOf(this.idmachine));
            pst.setString(2, String.valueOf(this.idtypeoperation));
            pst.setString(3, String.valueOf(this.duree));
            pst.executeUpdate();
        }
    }
}
