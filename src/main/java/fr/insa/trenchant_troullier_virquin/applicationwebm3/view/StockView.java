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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.MatPremiere;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

/**
 * @author laelt
 */

@Route(value = "stock", layout = MainLayout.class)
@PageTitle("Gestion des Stocks | Appli TP Info")
public class StockView extends VerticalLayout {
    private final Button b_MatPre = new Button("Matières premières");
    private final Button b_ProdFini = new Button("Produits finis");
    private final Button b_ProdEnCours = new Button("Produits en cours");
    private final Button b_Ajouter_MatPre = new Button("Ajouter une nouvelle matière première");
    private final CrmService service;
    private final Grid<Exemplaires> gridProduitFini = new Grid<>(Exemplaires.class);
    private final Grid<MatPremiere> gridMatPremiere = new Grid<>(MatPremiere.class);
    private final Grid<Exemplaires> gridProdEnCours = new Grid<>(Exemplaires.class);
    private final HorizontalLayout Bandeau = new HorizontalLayout();
    private final HorizontalLayout Entete = new HorizontalLayout();
    TextField filterText = new TextField();
    private MatPremiereForm matPremiereForm;

    public StockView(CrmService service) {
        this.service = service;
        configureGridMatPremiere();
        configureGridProduitFini();
        configureGridProdEnCours();
        configureMatPremiereForm();
        ChangeToMatPremiere();
        b_MatPre.setIcon(VaadinIcon.BOOK.create());
        this.b_MatPre.addClickListener((t) -> {
            this.ChangeToMatPremiere();
        });
        b_ProdEnCours.setIcon(VaadinIcon.COGS.create());
        this.b_ProdEnCours.addClickListener((t) -> {
            this.ChangeToProdEnCours();
        });
        b_ProdFini.setIcon(VaadinIcon.CHECK.create());
        this.b_ProdFini.addClickListener((t) -> {
            this.ChangeToProduitFini();
        });
        b_Ajouter_MatPre.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        b_Ajouter_MatPre.setIcon(VaadinIcon.PLUS.create());
        this.b_Ajouter_MatPre.addClickListener((t) -> {
            this.AjouterMatPremiere();
        });
        setSizeFull();
    }

    private HorizontalLayout getEntete() {
        this.Entete.removeAll();
        H1 titre = new H1("Gestion des Stocks");
        this.Entete.add(titre);
        return this.Entete;
    }

    public void updateList(GridProduit grid) {
        grid.setItems(service.findAllProduits(null));
    }

    public void ChangeToMatPremiere() {
        this.removeAll();
        updateListMatPremiere();
        this.add(getEntete(), getBandeauMatPremiere(), getContentMatPremiere());
    }

    public void ChangeToProduitFini() {
        this.removeAll();
        updateListProduit();
        this.add(getEntete(), getBandeau(), this.gridProduitFini);
    }

    public void ChangeToProdEnCours() {
        this.removeAll();
        updateListProdEncours();
        this.add(getEntete(), getBandeau(), this.gridProdEnCours);
    }

    private void configureGridMatPremiere() {
        this.gridMatPremiere.addClassName("matpremiere-grid");
        this.gridMatPremiere.setSizeFull();
        this.gridMatPremiere.removeAllColumns();
        this.gridMatPremiere.addColumn(MatPremiere::getRef).setHeader("Référence");
        this.gridMatPremiere.addColumn(MatPremiere::getDes).setHeader("Description");
        this.gridMatPremiere.addColumn(MatPremiere::getQuantite).setHeader("Quantité (kg)");
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
    
    private void AjouterMatPremiere() {
        gridMatPremiere.asSingleSelect().clear();
        editMatPremiere(new MatPremiere());
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
            return (produit != null) ? produit.getDes() : "";
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
            return (produit != null) ? produit.getDes() : "";
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

    public HorizontalLayout getBandeau() {
        this.Bandeau.removeAll();
        this.Bandeau.add(this.b_MatPre, this.b_ProdEnCours, this.b_ProdFini);
        return this.Bandeau;
    }

    public HorizontalLayout getBandeauMatPremiere() {
        this.Bandeau.removeAll();
        H1 txtVide = new H1(" ");
        this.Bandeau.setWidthFull();
        this.Bandeau.add(this.b_ProdEnCours, this.b_ProdFini, txtVide, this.b_Ajouter_MatPre);
        this.Bandeau.expand(txtVide);
        return this.Bandeau;
    }
}
