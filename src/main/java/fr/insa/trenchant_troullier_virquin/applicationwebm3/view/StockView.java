/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatPremiere;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation_Effectuee;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

/**
 *
 * @author laelt
 */

@Route(value = "stock", layout = MainLayout.class)
@PageTitle("Intial | Appli TP Info")
public class StockView extends VerticalLayout{
    private Button b_MatPre = new Button("Matiere Première");
    private Button b_ProdFini = new Button("Produit fini");
    private Button b_ProdEnCours = new Button("En production");
    private CrmService service;
    private Grid<Exemplaires> gridProduitFini = new Grid<>(Exemplaires.class);
    private Grid<MatPremiere> gridMatPremiere = new Grid<>(MatPremiere.class);
    private Grid<Exemplaires> gridProdEnCours = new Grid<>(Exemplaires.class);
    private HorizontalLayout Bandeau = new HorizontalLayout();
    TextField filterText = new TextField();
    
    public StockView(CrmService service) {
        this.service = service;
        this.Bandeau.add(this.b_MatPre,this.b_ProdEnCours,this.b_ProdFini);
        
        configureGridMatPremiere();
        configureGridProduitFini();
        configureGridProdEnCours();
        
        this.b_MatPre.addClickListener((t) -> {
            this.ChangeToMatPremiere();
        });
        this.b_ProdEnCours.addClickListener((t) -> {
            this.ChangeToProdEnCours();
        });
        this.b_ProdFini.addClickListener((t) -> {
            this.ChangeToProduitFini();
        });
        setSizeFull();
        this.add(this.Bandeau);
    }
    public void updateList(GridProduit grid) {
        grid.setItems(service.findAllProduits(null));
    }
    public void ChangeToMatPremiere(){
            this.removeAll();
            updateListMatPremiere();
            this.add(Bandeau, this.gridMatPremiere);
        }
    public void ChangeToProduitFini(){
            this.removeAll();
            updateListProduit();
            this.add(Bandeau, this.gridProduitFini);
        }
    public void ChangeToProdEnCours(){
            this.removeAll();
            updateListProdEncours();
            this.add(Bandeau, this.gridProdEnCours);
        }
    private void configureGridMatPremiere() {
        this.gridMatPremiere.addClassName("matpremiere-grid");
        this.gridMatPremiere.setSizeFull();
        this.gridMatPremiere.removeColumnByKey("id");
        this.gridMatPremiere.removeColumnByKey("ref");
        this.gridMatPremiere.removeColumnByKey("des");
        this.gridMatPremiere.addColumn(MatPremiere::getRef).setHeader("Référence");
        this.gridMatPremiere.addColumn(MatPremiere::getDes).setHeader("Description");
        //TODO : Ajouter une colonne pour l'image
        this.gridMatPremiere.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.asSingleSelect().addValueChangeListener(event -> editProduit(event.getValue()));
    }
    
    private void configureGridProdEnCours() {
        this.gridProdEnCours.addClassName("ProdEnCours-grid");
        this.gridProdEnCours.setSizeFull();
        this.gridProdEnCours.removeAllColumns();
        this.gridProdEnCours.addColumn(Exemplaires -> {
                    Commande commande = Exemplaires.getCommande();
                    return (commande != null) ? commande.getRef() : "";
                    }).setHeader("Commande");
        this.gridProdEnCours.addColumn(Exemplaires -> {
                    Produit produit = Exemplaires.getProduit();
                    return (produit != null) ? produit.getDes(): "";
                    }).setHeader("Produit");
        this.gridProdEnCours.addColumn(Exemplaires::getEtape).setHeader("Etape");
        this.gridProdEnCours.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.asSingleSelect().addValueChangeListener(event -> editProduit(event.getValue()));
    }
    private void configureGridProduitFini() {
        this.gridProduitFini.addClassName("ProdFini-grid");
        this.gridProduitFini.setSizeFull();
        this.gridProduitFini.removeAllColumns();
        this.gridProduitFini.addColumn(Exemplaires -> {
                    Commande commande = Exemplaires.getCommande();
                    return (commande != null) ? commande.getRef() : "";
                    }).setHeader("Commande");
        this.gridProduitFini.addColumn(Exemplaires -> {
                    Produit produit = Exemplaires.getProduit();
                    return (produit != null) ? produit.getDes(): "";
                    }).setHeader("Produit");
        this.gridProduitFini.getColumns().forEach(col -> col.setAutoWidth(true));
        //grid.asSingleSelect().addValueChangeListener(event -> editProduit(event.getValue()));
    }
    
    private void updateListProduit() {
        gridProduitFini.setItems(service.findAllProdFini());
    }
    
    private void updateListProdEncours() {
        gridProdEnCours.setItems(service.findAllProdEnCours());
    }
    
    private void updateListMatPremiere() {
        gridMatPremiere.setItems(service.findAllMatPremiere(filterText.getValue()));
    }
}
