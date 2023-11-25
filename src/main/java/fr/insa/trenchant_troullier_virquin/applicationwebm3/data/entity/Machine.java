package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Machine extends AbstractEntity {
    @NotEmpty
    private String ref = "";

    @NotEmpty
    private String des = "";
    @NotNull
    private float puissance = 0;

    @Override
    public String toString() {
        return "Machine{" +
                "ref='" + ref + '\'' +
                ", des='" + des + '\'' +
                ", puissance=" + puissance +
                '}';
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
}
