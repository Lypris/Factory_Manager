/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author laelt
 */
@Entity
public class MatPremiere extends AbstractEntity {
    @NotEmpty
    private String ref = "";

    @NotEmpty
    private String des = "";
    
    private double quantite;


    @Override
    public String toString() {
        return "Produit{" +
                "ref='" + ref + '\'' +
                ", des='" + des + '\'' +
                '}';
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
}
