package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class TypeOperation extends AbstractEntity{
    @NotEmpty
    private String des = "";

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    @Override
    public String toString() {
        return "TypeOperation{" +
                "des='" + des + '\'' +
                '}';
    }

}
