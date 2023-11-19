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
public class Machine {
    private int id;
    private String ref;
    private String des;
    private float puissance;
    
    public Machine(int id, String ref, String des, float puissance) {
        this.id = id;
        this.ref = ref;
        this.des = des;
        this.puissance = puissance;
    }

    public Machine(String ref, String des, float puissance) {
        this(-1, ref, des, puissance);
    }
    
    public static Machine nouvelleMachine(){
        System.out.println("Référence :");
        String ref = Lire.S();
        System.out.println("Description :");
        String des = Lire.S();
        System.out.println("Puissance :");
        float p = Lire.f();
        return new Machine(ref, des, p);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert machine2 (ref,des,puissance) values (?,?,?)")) {
            pst.setString(1, this.ref);
            pst.setString(2, this.des);
            pst.setString(3, String.valueOf(this.puissance));
            pst.executeUpdate();
        }
    }
    
    public static List<Machine> ToutesLesMachines(Connection con) throws SQLException {
        List<Machine> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,ref,des,puissance from machine2")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    float puissance = Float.parseFloat(rs.getString("puissance"));
                    res.add(new Machine(id, ref, des, puissance));
                }
            }
        }
        return res;
    }
    public static int choixmachine(Connection con) throws SQLException {
        int choix;
        System.out.println(" ");
        System.out.println(ListUtils.enumerateList(Machine.ToutesLesMachines(con)));
        System.out.println("Choix de la machine :");
        choix = Lire.i();
        return choix;
    }

    @Override
    public String toString() {
        return "ref = " + ref + ", des = " + des + ", puissance = " + puissance;
    }
    
    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setPuissance(float puissance) {
        this.puissance = puissance;
    }

    
    public String getRef() {
        return ref;
    }

    public String getDes() {
        return des;
    }

    public float getPuissance() {
        return puissance;
    }
    
    
}
