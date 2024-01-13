package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.DefinitionCommande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.util.ArrayList;
import java.util.List;

public class DialogDefCommande extends Dialog {

    ArrayList<DefinitionCommande> produitsDefini = new ArrayList<>();
    Grid<DefinitionCommande> grid1 = null;
    CrmService service;

    public DialogDefCommande(Commande commandes, List<Produit> produits, CrmService service) {
        this.service = service;
        this.setCloseOnEsc(true);
        addClassName("DefCommande-dialog");
        ArrayList<Produit> produit = (ArrayList<Produit>) produits;
        HorizontalLayout toolBar = setupToolBar(produit, commandes);
        produitsDefini = (ArrayList<DefinitionCommande>) service.findAllDefinitionCommandeByCommande(commandes);
        grid1 = setupGrid1(commandes);
        VerticalLayout layout = new VerticalLayout(toolBar, grid1);
        layout.setSizeFull();
        add(layout);
        configureFooter();
    }

    private static void setGridStyles(Grid<DefinitionCommande> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private void configureFooter() {
        Button save = new Button("Enregistrer", (e) -> this.close());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.getStyle().set("margin-right", "auto");
        this.getFooter().add(save);
    }

    private HorizontalLayout setupToolBar(ArrayList<Produit> produits, Commande commande) {
        HorizontalLayout toolBar = new HorizontalLayout();
        toolBar.setWidthFull();
        ComboBox<Produit> produit = new ComboBox<>("Produits");
        produit.setItems(produits);
        produit.setItemLabelGenerator(Produit -> Produit.getDes());
        produit.setWidthFull();
        IntegerField NumberField = new IntegerField();
        NumberField.setValue(1);
        NumberField.setStepButtonsVisible(true);
        NumberField.setMin(0);

        Button addButton = new Button("Ajouter");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            Produit produit1 = produit.getValue();
            boolean dejacommande = false;
            DefinitionCommande produit2 = new DefinitionCommande();
            for (DefinitionCommande prod : produitsDefini) {
                if (prod.getProduit().equals(produit1)) {
                    dejacommande = true;
                    break;
                }
            }
            if (!dejacommande) {
                int number = NumberField.getValue();
                produit2.setProduit(produit1);
                produit2.setNbr(number);
                if (commande != null) produit2.setCommande(commande);
                produitsDefini.add(produit2);
                grid1.setItems(produitsDefini);
                service.saveDefinitionCommande(produit2);
            } else Notification.show("Vous avez déjà commandé ce produit");
        });
        toolBar.add(produit, NumberField, addButton);
        toolBar.setAlignSelf(FlexComponent.Alignment.END, produit, NumberField, addButton);
        return toolBar;
    }

    private Grid<DefinitionCommande> setupGrid1(Commande commande) {
        Grid<DefinitionCommande> grid = new Grid<>(DefinitionCommande.class);
        grid.removeAllColumns();
        grid.addColumn(DefinitionCommande::getProduitName).setHeader("produit");
        grid.addColumn(DefinitionCommande::getNbr).setHeader("nbr");
        Grid.Column<DefinitionCommande> suppProduit = grid.addComponentColumn(produit -> {
            Button suppButton = new Button("Supprimer", VaadinIcon.TRASH.create());
            suppButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            suppButton.addClickListener(e -> {
                produitsDefini.remove(produit);
                grid.setItems(produitsDefini);
                service.deleteDefinitionCommande(produit);
            });
            return suppButton;
        }).setWidth("180px").setFlexGrow(0);
        grid.setItems(produitsDefini);
        setGridStyles(grid);
        grid.setSizeFull();
        return grid;
    }


}
