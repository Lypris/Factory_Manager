package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilise")
public class Utilise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "operateur_id")
    private Operateur operateur;

    @ManyToOne
    @JoinColumn(name = "poste_de_travail_id")
    private PosteDeTravail posteDeTravail;

    private LocalDateTime debut;

    private LocalDateTime fin;

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

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }
}
