package fr.insa.trenchant_troullier_virquin.factory_manager.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class EtatPossibleMachine extends AbstractEntity {
    @NotBlank(message = "La description ne peut pas être vide")
    private String des;

    public EtatPossibleMachine() {

    }


    public EtatPossibleMachine(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

}