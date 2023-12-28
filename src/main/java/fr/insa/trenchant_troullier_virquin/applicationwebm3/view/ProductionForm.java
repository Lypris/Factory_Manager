package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;

public class ProductionForm extends FormLayout {
    //grid de produits
    Grid<Produit> grid = new Grid<>(Produit.class);

    public ProductionForm() {
        addClassName("production-form");
    }
    //configure le grid

}
