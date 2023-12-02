package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Produit extends AbstractEntity{
    @NotEmpty
    private String ref = "";

    @NotEmpty
    private String des = "";
    @NotNull
    private float prix;
    @Lob
    private byte[] image;


    @Override
    public String toString() {
        return "Produit{" +
                "ref='" + ref + '\'' +
                ", des='" + des + '\'' +
                ", prix=" + prix +
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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
