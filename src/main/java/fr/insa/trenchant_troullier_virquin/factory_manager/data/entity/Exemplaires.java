/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * @author laelt
 */
@Entity
public class Exemplaires extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
    
    /*@OneToOne
    @JoinColumn(name = "OperationEffectuee_id")
    private Operation_Effectuee operation_effectuee;*/

    private int etape;

    public Exemplaires() {

    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public int getEtape() {
        return etape;
    }

    public void setEtape(int etape) {
        this.etape = etape;
    }

    @Override
    public String toString() {
        return "Exemplaires{" + "Id" + this.getId() + "produit=" + produit + ", commande=" + commande + ", etape=" + etape + '}';
    }


}
