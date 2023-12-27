package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Entity
public class Commande extends AbstractEntity {
    private String des = "";
    private String ref = "";

    private String statut = "";

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }


}
