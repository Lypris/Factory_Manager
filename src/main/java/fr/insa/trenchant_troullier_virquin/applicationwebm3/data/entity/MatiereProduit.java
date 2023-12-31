package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class MatiereProduit extends AbstractEntity {

    //@NotNull
    private float quantite;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;
    //@NotNull
    @ManyToOne
    @JoinColumn(name = "id_matiere")
    private MatPremiere matPremiere;

    public MatiereProduit() {
    }


    public float getQuantite() {
        return quantite;
    }

    public void setQuantite(float quantite) {
        this.quantite = quantite;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public MatPremiere getMatPremiere() {
        return matPremiere;
    }

    public void setMatPremiere(MatPremiere matPremiere) {
        this.matPremiere = matPremiere;
    }

    @Override
    public String toString() {
        return "MatiereProduit{" +
                "quantite=" + quantite +
                ", produit=" + produit +
                ", matPremiere=" + matPremiere +
                '}';
    }
}