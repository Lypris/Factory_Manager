/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation_Effectuee;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laelt
 */

@Route(value = "GestionProduction/:commandeID", layout = MainLayout.class)
public class GestionProductionView extends VerticalLayout implements BeforeEnterObserver {
    private final Grid<Produit> gridProduit = new Grid<>(Produit.class);
    private final CrmService service;
    private final Button ValidationButton;
    //private Button ValiderExemplaire = new Button ("Valider");
    private final List<Button> ListButtonValider = new ArrayList<>();
    private long commandeId;
    private Commande commande;
    private List<Produit> ListProduitCommande;

    public GestionProductionView(CrmService service) {
        this.service = service;
        this.ValidationButton = new Button("Valider la Commande");
        this.ValidationButton.setEnabled(false);
        this.ValidationButton.addClickListener((t) -> {
            //Methdode Validation de la commande
        });
        this.add(this.ValidationButton);
    }


    //Cette méthode est appelée avant que la vue ne soit affichée afin de récupérer l'ID de la commande
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        commandeId = Long.valueOf(event.getRouteParameters().get("commandeID").orElse("0"));
        this.ListProduitCommande = this.service.findAllProduitByCommande(this.service.findCommandeById(commandeId));
        this.commande = this.service.findCommandeById(this.commandeId);
        if (commandeId > 0) {
            Commande commande = service.findCommandeById(commandeId);
            if (commande != null) {
                // Utiliser la commande pour initialiser les composants
                configureGridProduit();
                updateListProduit();

                // ... et ainsi de suite
            } else {
                // Gérer le cas où la commande n'existe pas
                event.rerouteTo(InitialView.class);
            }
        } else {
            // Gérer le cas où l'ID de la commande n'est pas fourni ou est invalide
            event.rerouteTo(InitialView.class);
        }
    }

    public void configureGridProduit() {
        gridProduit.removeAllColumns();
        gridProduit.addColumn(Produit ->
                Produit.getDes()).setHeader("Produits");
        gridProduit.addColumn(Produit ->
                custominfo(commande, Produit)).setHeader("Exemplaires");
        gridProduit.addColumn(new ComponentRenderer<>(Produit ->
                ExemplaireField(this.commande, Produit))).setHeader("Gestion Exemplaires");
        gridProduit.getColumns().forEach(col -> {
            col.setAutoWidth(true);
        });
        gridProduit.addClassName("my-grid");
        this.add(this.gridProduit);
    }

    private String custominfo(Commande commande, Produit produit) {
        //méthode qui permet d'afficher le nombre d'exemplaires finis pour un produit et une commande donnés
        // par rapport au nombre d'exemplaires commandés
        int NbrExemplaires = service.findAllProdFiniByProduitAndCommande(produit, commande).size();
        int NbrExemplairesCommandes = service.getDefinitionByProduitAndCommandeUnique(produit, commande).getNbr();
        return NbrExemplaires + "/" + NbrExemplairesCommandes + " Exemplaires produits";
    }

    private void updateListProduit() {
        this.gridProduit.setItems(this.service.findAllProduitByCommande(commande));
    }

    private HorizontalLayout ExemplaireField(Commande commande, Produit produit) {
        HorizontalLayout bloc = new HorizontalLayout();
        Button ValiderExemplaire = new Button("Valider");
        this.ListButtonValider.add(ValiderExemplaire);
        IntegerField ExemplaireField = new IntegerField();
        ValiderExemplaire.addClickListener((t) -> {
            AjouterExemplaires(produit, ExemplaireField.getValue());
            update(ValiderExemplaire, ExemplaireField, commande, produit);
            configureGridProduit();
        });
        update(ValiderExemplaire, ExemplaireField, commande, produit);
        bloc.add(ExemplaireField, ValiderExemplaire);
        return bloc;
    }

    private void update(Button ValiderExemplaire, IntegerField ExemplaireField, Commande commande1, Produit produit) {
        ExemplaireField.setValue(0);
        ExemplaireField.setStepButtonsVisible(true);
        ExemplaireField.setMin(0);
        int Max = service.getDefinitionByProduitAndCommandeUnique(produit, commande).getNbr() - service.findAllProdFiniByProduitAndCommande(produit, commande1).size();
        ExemplaireField.setMax(Max);
        if (Max == 0) {
            ValiderExemplaire.setEnabled(false);
            Notification.show("3");
        } else {
            ValiderExemplaire.setEnabled(true);
        }
        verifierEtatProduction();
    }

    private void AjouterExemplaires(Produit produit, Integer nbEx) {
        int nbOpe = this.service.findOperationByProduit(produit).size();
        List<Exemplaires> ListExemplaireEnCours = this.service.findAllProdEnCoursByProduitAndCommande(produit, this.commande);
        for (int i = 0; i < nbEx; i++) {
            this.service.ExemplaireFini(nbOpe + 1, ListExemplaireEnCours.get(i));
            List<Operation_Effectuee> ListOPF = this.service.findAllOperationEffectueeByExemplaire(ListExemplaireEnCours.get(i));
            for (Operation_Effectuee opf : ListOPF) {
                this.service.SetFinOperationEffectuee(LocalDateTime.now(), opf);
            }
        }
    }

    private void verifierEtatProduction() {
        boolean tousProduitsFinis = true;
        for (Produit produit : ListProduitCommande) {
            int nbExemplairesProduits = service.findAllProdFiniByProduitAndCommande(produit, commande).size();
            int nbExemplairesCommandes = service.getDefinitionByProduitAndCommandeUnique(produit, commande).getNbr();
            if (nbExemplairesProduits < nbExemplairesCommandes) {
                tousProduitsFinis = false;
                break;
            }
        }
        this.ValidationButton.setEnabled(tousProduitsFinis);
    }


}
