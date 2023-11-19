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
public class Typeoperation {
    private int id;
    private String des;
    
    public Typeoperation(int id, String des){
        this.id = id;
        this.des = des;
    }
    
    public Typeoperation (String des){
        this(-1, des);
    }
    
    public static Typeoperation nouveautype (){
        System.out.println("Description :");
        String des = Lire.S();
        return new Typeoperation(des);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert typeoperation2 (des) values (?)")) {
            pst.setString(1, this.des);
            pst.executeUpdate();
        }
    }
    
    public static List<Typeoperation> ToutesLestypesOperations(Connection con) throws SQLException {
        List<Typeoperation> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,des from typeoperation2")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String des = rs.getString("des");
                    res.add(new Typeoperation(id, des));
                }
            }
        }
        return res;
    }
    
    public static int choixtypeoperation(Connection con) throws SQLException {
        int choix;
        System.out.println(" ");
        System.out.println("Liste des types d'operation :");
        System.out.println(ListUtils.enumerateList(Typeoperation.ToutesLestypesOperations(con)));
        System.out.println("Chosir un type d'operation :");
        choix = Lire.i();
        return choix;
    }

    @Override
    public String toString() {
        return " " + des;
    }
    
    
}
