package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "operateur")
public class Operateur extends AbstractEntity {

    @NotEmpty
    private String nom = "";

    @NotEmpty
    private String prenom = "";

    @Email
    @NotEmpty
    private String mail = "";
    @NotEmpty
    private String tel = "";

    @Override
    public String toString() {
        return nom + " " + prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}