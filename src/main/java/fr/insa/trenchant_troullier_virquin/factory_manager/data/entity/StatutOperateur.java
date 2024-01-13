package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class StatutOperateur extends AbstractEntity {
    //cette entité sert à définir les différents status des opérateurs
    //il y a les attributs suivants: un timestamp de début, un timestamp de fin
    //et des clés étrangères vers les opérateurs et les statuts
    private LocalDateTime debut;
    private LocalDateTime fin;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "operateur_id")
    private Operateur operateur;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "statut_id")
    private Statut statut;

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

    public Operateur getOperateur() {
        return operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
}