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
public class Produit {
    private int id;
    private String ref , des;

    public Produit(int id, String ref, String des) {
        this.id = id;
        this.ref = ref;
        this.des = des;
    }
    
    public Produit (String ref, String des){
        this(-1, ref, des);
    }
    
    public static Produit NouveauProduit(){
        System.out.println("Référence du produit :");
        String ref = Lire.S();
        System.out.println("Description du produit :");
        String des = Lire.S();
        return new Produit(ref, des);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert produit2 (ref, des) values (?,?)")) {
            pst.setString(1, this.ref);
            pst.setString(2, this.des);
            pst.executeUpdate();
        }
    }
    
    public static List<Produit> ToutLesProduits (Connection con) throws SQLException {
        List<Produit> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,ref,des from produit2")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    res.add(new Produit(id, ref, des));
                }
            }
        }
        return res;
    }
    
    public static int ChoixProduit(Connection con) throws SQLException {
        int choix;
        System.out.println(" ");
        System.out.println("Liste des produits :");
        System.out.println(ListUtils.enumerateList(Produit.ToutLesProduits(con)));
        System.out.println("Chosir un produit :");
        choix = Lire.i();
        return choix;
    }

    @Override
    public String toString() {
        return " " + ref + " " + des;
    }
}
