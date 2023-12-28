/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
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
    private Button b_Ajouter_MatPre = new Button("Ajouter");
    private CrmService service;
    private Grid<Exemplaires> gridProduitFini = new Grid<>(Exemplaires.class);
    private Grid<MatPremiere> gridMatPremiere = new Grid<>(MatPremiere.class);
    private MatPremiereForm matPremiereForm;
    private Grid<Exemplaires> gridProdEnCours = new Grid<>(Exemplaires.class);
    private HorizontalLayout Bandeau = new HorizontalLayout();
    TextField filterText = new TextField();
    
    public StockView(CrmService service) {
        this.service = service;
        getBandeau();
        
        configureGridMatPremiere();
        configureGridProduitFini();
        configureGridProdEnCours();
        configureMatPremiereForm();
        
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
            this.add(getBandeauMatPremiere(), getContentMatPremiere());
        }
    public void ChangeToProduitFini(){
            this.removeAll();
            updateListProduit();
            this.add(getBandeau(), this.gridProduitFini);
        }
    public void ChangeToProdEnCours(){
            this.removeAll();
            updateListProdEncours();
            this.add(getBandeau(), this.gridProdEnCours);
        }
    private void configureGridMatPremiere() {
        this.gridMatPremiere.addClassName("matpremiere-grid");
        this.gridMatPremiere.setSizeFull();
        this.gridMatPremiere.removeColumnByKey("id");
        this.gridMatPremiere.removeColumnByKey("ref");
        this.gridMatPremiere.removeColumnByKey("des");
        this.gridMatPremiere.addColumn(MatPremiere::getRef).setHeader("Référence");
        this.gridMatPremiere.addColumn(MatPremiere::getDes).setHeader("Description");
        this.gridMatPremiere.getColumns().forEach(col -> col.setAutoWidth(true));
        this.gridMatPremiere.asSingleSelect().addValueChangeListener(event -> editMatPremiere(event.getValue()));
    }
    private void configureMatPremiereForm() {
        matPremiereForm = new MatPremiereForm(service);
        matPremiereForm.setWidth("35em");
        matPremiereForm.setVisible(false);
        matPremiereForm.addSaveListener(this::saveMatPremiere);
        matPremiereForm.addDeleteListener(this::deleteMatPremiere);
        matPremiereForm.addCloseListener(e -> closeEditor());
    }
    private Component getContentMatPremiere() {
        HorizontalLayout content = new HorizontalLayout(gridMatPremiere, matPremiereForm);
        content.setFlexGrow(2, gridMatPremiere);
        content.setFlexGrow(1, matPremiereForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void editMatPremiere(MatPremiere matPre) {
        if (matPre == null) {
            closeEditor();
        } else {
            matPremiereForm.setMatPremiere(matPre);
            matPremiereForm.setVisible(true);
            addClassName("editing");
        }
    }
    private void closeEditor() {
        matPremiereForm.setMatPremiere(null);
        matPremiereForm.setVisible(false);
        removeClassName("editing");
    }
    private void saveMatPremiere(MatPremiereForm.SaveEvent event) {
        service.saveMatPremiere(event.getMatPremiere());
        updateListMatPremiere();
        closeEditor();
    }
    private void deleteMatPremiere(MatPremiereForm.DeleteEvent event) {
        service.deleteMatPremiere(event.getMatPremiere());
        updateListMatPremiere();
        closeEditor();
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
    
    public HorizontalLayout getBandeau(){
        this.Bandeau.removeAll();
        this.Bandeau.add(this.b_MatPre,this.b_ProdEnCours,this.b_ProdFini);
        return this.Bandeau;
    }
    public HorizontalLayout getBandeauMatPremiere(){
        this.Bandeau.removeAll();
        H1 txtVide = new H1(" ");
        this.Bandeau.setWidthFull();
        this.Bandeau.add(this.b_MatPre,this.b_ProdEnCours,this.b_ProdFini, txtVide, this.b_Ajouter_MatPre);
        this.Bandeau.expand(txtVide);
        return this.Bandeau;
    }
}
