/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author laelt
 */
public class Precede {
    private int opavant, opapres;

    public Precede(int opavant, int opapres) {
        this.opavant = opavant;
        this.opapres = opapres;
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert precede2 (opavant, opapres) values (?,?)")) {
            pst.setString(1, String.valueOf(this.opavant));
            pst.setString(2, String.valueOf(this.opapres));
            pst.executeUpdate();
        }
    }
    
}
