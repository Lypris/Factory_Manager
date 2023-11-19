package fr.insa.trenchant_troullier_virquin.applicationwebm3.model;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.Lire;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.utils.list.ListUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Operateur {
    private String name, lastname, mail, phone;
    private Status_Operateur status;

    public Operateur(String name, String lastname, String mail, String phone) {
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.phone = phone;
        this.status = new Status_Operateur("Actif"); //par défaut, un opérateur est actif
    }
    @Override
    public String toString() {
        return "nom=" + name + ", prenom=" + lastname;
    }
    public String getName() {
        return name;
    }
    public String getLastname() {
        return lastname;
    }
    public String getMail() {
        return mail;
    }
    public String getPhone() {
        return phone;
    }
    public Status_Operateur getStatus() {
        return status;
    }


    public static Operateur createOperateur(Connection conn) {
        //on demande les infos de l'opérateur
        System.out.println("Nom :");
        String name = Lire.S();
        System.out.println("Prénom :");
        String lastname = Lire.S();
        System.out.println("Mail :");
        String mail = Lire.S();
        System.out.println("Téléphone :");
        String phone = Lire.S();
        return new Operateur(name, lastname, mail, phone);
    }

    public void saveInDB(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
            "INSERT INTO r_operateur (nom, prenom, mail, tel) VALUES (?, ?, ?, ?)")) {
            pst.setString(1, this.name);
            pst.setString(2, this.lastname);
            pst.setString(3, this.mail);
            pst.setString(4, this.phone);
            pst.executeUpdate();
        }
    }

    public void saveInDBV2(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO operateur (mail, nom, prenom, tel) VALUES (?, ?, ?, ?)")) {
            pst.setString(2, this.name);
            pst.setString(3, this.lastname);
            pst.setString(1, this.mail);
            pst.setString(4, this.phone);
            pst.executeUpdate();
        }
    }

    //method to find all Operateur in the database with a name, lastname, mail or phone number matching the searchTerm
    public static List<Operateur> searchAllOperateurs(Connection con, String searchTerm) throws SQLException {
        List<Operateur> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                //take the table r_etat_operateur and r_etat_possible_operateur and r_operateur and join them
                //and then select the name, lastname, mail and phone number of the operator if the searchTerm is matching
                "select r_operateur.nom, r_operateur.prenom, r_operateur.mail, r_operateur.tel, r_etat_possible_operateur.des from r_etat_operateur " +
                        "join r_etat_possible_operateur on r_etat_operateur.idetatpossible = r_etat_possible_operateur.id " +
                        "join r_operateur on r_etat_operateur.idoperateur = r_operateur.id " +
                        "where lower(r_operateur.nom) like lower(concat('%', :searchTerm, '%')) " +
                        "or lower(r_operateur.prenom) like lower(concat('%', :searchTerm, '%'))" +
                        "or lower(r_operateur.mail) like lower(concat('%', :searchTerm, '%'))" +
                        "or lower(r_operateur.tel) like lower(concat('%', :searchTerm, '%'))")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Operateur(rs.getString("nom"), rs.getString("prenom"), rs.getString("mail"), rs.getString("tel")));
                    new Status_Operateur(rs.getString("des"));
                }
                return res;
            }
        }
    }

    public static List<Operateur> allOperateurs(Connection con) throws SQLException {
        List<Operateur> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                //take the table r_etat_operateur and r_etat_possible_operateur and r_operateur and join them
                "select r_operateur.nom, r_operateur.prenom, r_operateur.mail, r_operateur.tel, r_etat_possible_operateur.des from r_etat_operateur " +
                        "join r_etat_possible_operateur on r_etat_operateur.idetatpossible = r_etat_possible_operateur.id " +
                        "join r_operateur on r_etat_operateur.idoperateur = r_operateur.id")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    res.add(new Operateur(rs.getString("nom"), rs.getString("prenom"), rs.getString("mail"), rs.getString("tel")));
                    new Status_Operateur(rs.getString("des"));
                }
                return res;
            }
        }
    }

    public static void deleteOperateur(Connection conn){
        //on demande l'id de l'opérateur à supprimer
    }

    //fonction qui permet de modifier les infos d'un opérateur
    public static void modifyOperateur(Connection conn){
        //on demande l'id de l'opérateur à modifier
        System.out.println("Id de l'opérateur à modifier :");
        int id = Lire.i();
        //on demande les infos de l'opérateur
        System.out.println("Nom :");
        String name = Lire.S();
        System.out.println("Prénom :");
        String lastname = Lire.S();
        System.out.println("Mail :");
        String mail = Lire.S();
        System.out.println("Téléphone :");
        String phone = Lire.S();
        //on modifie l'opérateur
        try (PreparedStatement pst = conn.prepareStatement(
            "UPDATE r_operateur SET nom = ?, prenom = ?, mail = ?, tel = ? WHERE id = ?")) {
            pst.setString(1, name);
            pst.setString(2, lastname);
            pst.setString(3, mail);
            pst.setString(4, phone);
            pst.setInt(5, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de l'opérateur");
        }
    }

}
