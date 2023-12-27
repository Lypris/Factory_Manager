package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class DefinitionCommande extends AbstractEntity{
    @NotNull
    private int nbr;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;
    //@NotNull
    @ManyToOne
    @JoinColumn(name = "id_commande")
    private Commande commande;

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public Produit getProduit() {
        return produit;
    }
    public String getProduitName() {
        return produit.getDes();
    }
    public void setProduitName(String produitName) {
        this.produit.setDes(produitName);
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
}
