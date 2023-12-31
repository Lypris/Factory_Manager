/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.grid.Grid;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

/**
 * @author laelt
 */
public class GridProduit extends Grid<Produit> {
    private final CrmService service;

    public GridProduit(CrmService service) {
        this.service = service;
        this.addClassName("operateur-grid");
        this.setSizeFull();
        //this.removeColumnByKey("id");
        //this.removeColumnByKey("version");
        this.addColumn(Produit::getRef).setHeader("Référence");
        this.addColumn(Produit::getDes).setHeader("Description");
        this.addColumn(Produit::getPrix).setHeader("Prix");
        this.getColumns().forEach(col -> col.setAutoWidth(true));
    }

}
