package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Entity
public class Commande extends AbstractEntity {
    @NotEmpty
    private String des = "";
    @NotEmpty
    private String ref = "";

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

    @Override
    public String toString() {
        return "Commande{" +
                "des='" + des + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
