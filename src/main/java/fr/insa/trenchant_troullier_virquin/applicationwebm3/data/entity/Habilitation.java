package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Habilitation extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "operateur_id")
    private Operateur operateur;

    @ManyToOne
    @JoinColumn(name = "poste_de_travail_id")
    private PosteDeTravail posteDeTravail;

    public Operateur getOperateur() {
        return operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public PosteDeTravail getPosteDeTravail() {
        return posteDeTravail;
    }

    public void setPosteDeTravail(PosteDeTravail posteDeTravail) {
        this.posteDeTravail = posteDeTravail;
    }


}
