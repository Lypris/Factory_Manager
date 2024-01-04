package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
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
    public double getCoutTotal(CrmService service) {
        double coutTotal = 0;
        List<DefinitionCommande> definitionCommandes = service.findAllDefinitionCommandeByCommande(this);
        for (DefinitionCommande definitionCommande : definitionCommandes) {
            coutTotal += definitionCommande.getNbr() * definitionCommande.getProduit().getPrix();
        }
        return coutTotal;
    }

    public void creatExemplairesAssociate(CrmService service) {//fonctionne uniquement si la commande est en attente
        List<Exemplaires> nouveauxExemplaires = new ArrayList<>();
        ArrayList<DefinitionCommande> definitionCommandes = service.findAllDefinitionCommandeByCommande(this);//on recupere les definitions de commande associé à la commande
        for (DefinitionCommande d : definitionCommandes) { //pour chaque DefinitionCommande associé à la commande
            int nb_exemplaire = service.countExemplairesByCommandeAndProduit(this, d.getProduit());//on compte le nombre d'exemplaire associé à la commande et au produit
            if (nb_exemplaire > d.getNbr()) {//si le nombre d'exemplaire est superieur au nombre de produit demandé
                //on supprime le nombre d'exemplaire en trop
                service.deleteNExemplaireByProduitAndCommande((nb_exemplaire - d.getNbr()), d.getProduit(), this);
            } else if (nb_exemplaire < d.getNbr()) {//si le nombre d'exemplaire est inferieur au nombre de produit demandé
                //on créé le nombre d'exemplaire manquant
                for (int i = 0; i < (d.getNbr() - nb_exemplaire); i++) {
                    Exemplaires exemplaire = new Exemplaires();
                    exemplaire.setProduit(d.getProduit());
                    exemplaire.setCommande(this);
                    exemplaire.setEtape(0);
                    nouveauxExemplaires.add(exemplaire);
                }
                // Ajout de la liste d'Exemplaires à la base de données à l'aide du repository JPA
                service.saveAllExemplaire(nouveauxExemplaires);
            }//sinon on ne fait rien
        }
    }
}