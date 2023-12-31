package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Operation extends AbstractEntity {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "typeOperation_id")
    private TypeOperation typeOperation;

    @NotNull
    private int ordre;

    public Operation() {
    }

    @Override
    public String toString() {
        return "Operation{" +
                "produit=" + produit.getDes() +
                ", typeOperation=" + typeOperation.getDes() +
                ", ordre=" + ordre +
                '}';
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

}

