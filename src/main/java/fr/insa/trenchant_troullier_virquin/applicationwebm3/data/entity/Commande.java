package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Commande extends AbstractEntity {
    private String des = "";
    private String ref = "";
    private String statut = "";
    private LocalDateTime debut;
    private LocalDateTime fin;
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

    public ArrayList<Produit> getProduit(CrmService service) {
        return new ArrayList<>(service.findAllProduitByCommande(this));
    }
    public void creatExemplairesAssociate(CrmService service) {
        service.deleteExemplaire(this);//supprime les exemplaires associés à la commande dont etape = 0
        for (Produit p : this.getProduit(service)) { //pour chaque produit associé à la commande
            int nb = service.findAllExemplaireEnCoursByProduitAndCommande(p, this).size();//on compte le nombre d'exemplaires dont etape>0 associé à la commande et au produit
            for (DefinitionCommande d : service.getDefinitionByProduitAndCommande(p, this)) {//pour chaque definition de commande associé à la commande et au produit
                for (int i = 0; i < (d.getNbr() - nb); i++) {//on créé le nombre d'exemplaire manquant
                    Exemplaires exemplaire = new Exemplaires();
                    exemplaire.setProduit(d.getProduit());
                    exemplaire.setCommande(this);
                    exemplaire.setOperation_effectuee(null);
                    exemplaire.setEtape(0);
                    service.saveExemplaire(exemplaire);
                }
            }
        }
    }
}
/*
for (DefinitionCommande d : this.getDefinitionCommande(service)) { //
        for (int i = 0; i < (d.getNbr() - nb); i++) {
        Exemplaires exemplaire = new Exemplaires();
        exemplaire.setProduit(d.getProduit());
        exemplaire.setCommande(this);
        exemplaire.setOperation_effectuee(null);
        exemplaire.setEtape(0);
        service.saveExemplaire(exemplaire);
        }
        }*/