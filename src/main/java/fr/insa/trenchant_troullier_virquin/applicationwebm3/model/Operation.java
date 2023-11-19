/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.model;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.Lire;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.utils.list.ListUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author laelt
 */
public class Operation {
    private int id, idtypeoperation, idproduit;

    public Operation(int id, int idtypeoperation, int idproduit) {
        this.id = id;
        this.idtypeoperation = idtypeoperation;
        this.idproduit = idproduit;
    }
    
    public Operation(int idtypeoperation, int idproduit){
        this(-1, idtypeoperation,idproduit);
    }
    
    public static Operation NouvelleOperation(int idtypeoperation, int idproduit){
        return new Operation(idtypeoperation, idproduit);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert operations2 (idtype, idproduit) values (?,?)")) {
            pst.setString(1, String.valueOf(this.idtypeoperation));
            pst.setString(2, String.valueOf(this.idproduit));
            pst.executeUpdate();
        }
    }
    
    public static List<Operation> ToutesLesOPerations (Connection con) throws SQLException {
        List<Operation> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,idtype,idproduit from operations2")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idtype = rs.getInt("idtype");
                    int idproduit = rs.getInt("idproduit");
                    res.add(new Operation(id, idtype, idproduit));
                }
            }
        }
        return res;
    }
    
    public static int ChoixOperation(Connection con) throws SQLException {
        int choix;
        System.out.println(" ");
        System.out.println("Liste des opérations :");
        System.out.println(ListUtils.enumerateList(Operation.ToutesLesOPerations(con)));
        System.out.println("Chosir de l'opération :");
        choix = Lire.i();
        return choix;
    }

    @Override
    public String toString() {
        return "idtypeoperation = " + idtypeoperation + ", idproduit = " + idproduit + '}';
    }
    
    
}
