package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "poste_de_travail")
public class PosteDeTravail{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ref;
    private String des;

    @OneToMany(mappedBy = "posteDeTravail")
    private Set<Machine> machines;
    @OneToMany(mappedBy = "posteDeTravail")
    private Set<Utilise> utilisations = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Machine> getMachines() {
        return machines;
    }

    public void setMachines(Set<Machine> machines) {
        this.machines = machines;
    }

    public Set<Utilise> getUtilisations() {
        return utilisations;
    }

    public void setUtilisations(Set<Utilise> utilisations) {
        this.utilisations = utilisations;
    }
}
