package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Realise extends AbstractEntity{
    //durée de l'opération
    private float duree;
    //clé étrangère vers le type d'opération
    @NotNull
    @OneToOne
    @JoinColumn(name = "typeoperation_id")
    private TypeOperation typeoperation;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    public float getDuree() {
        return duree;
    }

    public void setDuree(float duree) {
        this.duree = duree;
    }

    public TypeOperation getTypeoperation() {
        return typeoperation;
    }

    public void setTypeoperation(TypeOperation typeoperation) {
        this.typeoperation = typeoperation;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public String toString() {
        return "Realise{" +
                "duree=" + duree +
                ", typeoperation=" + typeoperation +
                ", machine=" + machine +
                '}';
    }
}
