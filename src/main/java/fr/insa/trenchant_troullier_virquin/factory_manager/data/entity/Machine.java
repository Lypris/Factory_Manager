package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Machine extends AbstractEntity {
    @NotEmpty
    private String ref = "";

    @NotEmpty
    private String des = "";
    @NotNull
    private float puissance;
    @NotNull
    private float durée;
    @ManyToOne
    @JoinColumn(name = "type_operation_id")
    private TypeOperation typeOperation;
    @ManyToOne
    @JoinColumn(name = "poste_de_travail_id")
    private PosteDeTravail posteDeTravail;

    @Override
    public String toString() {
        return ref + " | " + des;
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

    public float getPuissance() {
        return puissance;
    }

    public void setPuissance(float puissance) {
        this.puissance = puissance;
    }

    public float getDurée() {
        return durée;
    }

    public void setDurée(float durée) {
        this.durée = durée;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public PosteDeTravail getPosteDeTravail() {
        return posteDeTravail;
    }

    public void setPosteDeTravail(PosteDeTravail posteDeTravail) {
        this.posteDeTravail = posteDeTravail;
    }
}
