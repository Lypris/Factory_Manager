package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(mappedBy = "operateur")
    private Set<Utilise> utilisations = new HashSet<>();

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