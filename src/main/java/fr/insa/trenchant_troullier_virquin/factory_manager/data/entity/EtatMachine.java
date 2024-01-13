package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class EtatMachine extends AbstractEntity {
    @NotNull
    private LocalDateTime debut;
    private LocalDateTime fin;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "etat_possible_machine_id")
    private EtatPossibleMachine etat;

    public EtatMachine(LocalDateTime debut, Machine machine, EtatPossibleMachine etat) {
        this.debut = debut;
        this.machine = machine;
        this.etat = etat;
    }

    public EtatMachine() {
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

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public EtatPossibleMachine getEtat() {
        return etat;
    }

    public void setEtat(EtatPossibleMachine etat) {
        this.etat = etat;
    }
}
