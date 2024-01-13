package fr.insa.trenchant_troullier_virquin.factory_manager.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.MatPremiere;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.MatiereProduit;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface SaveListenerDefMat {
    void onSave();
}

public class DialogDefMat extends Dialog {

    private final CrmService service;
    private final Grid<MatPremiere> grid1 = setupGrid("Matières disponibles", false);
    private final Grid<MatPremiere> grid2 = setupGrid("Matières définies", true);
    private final ArrayList<MatPremiere> matpremieres = new ArrayList<>();
    private final GridListDataView<MatPremiere> dataView1 = grid1.setItems(matpremieres);
    private final List<SaveListenerDefMat> saveListenerDefMats = new ArrayList<>();
    private static Map<MatPremiere, NumberField> QuantMatPremiereMap = new HashMap<>();

    public ArrayList<MatPremiere> matpremieresDefini = new ArrayList<>();
    private final GridListDataView<MatPremiere> dataView2 = grid2.setItems(matpremieresDefini);
    private MatPremiere draggedItem;
    private Produit produit;
    private Grid<MatPremiere> dragSourceGrid;
    private ProductView.ProduitDetails produitDetails;

    public DialogDefMat(List<MatPremiere> matPremieres, CrmService service, Produit produit, ProductView.ProduitDetails produitDetails) {
        this.produitDetails = produitDetails;
        this.produit = produit;
        this.service = service;
        this.matpremieres.addAll(matPremieres);
        this.matpremieresDefini.addAll(service.findAllMatPremiereForProduit(produit));

        grid1.setDropMode(GridDropMode.ON_GRID);
        grid1.setRowsDraggable(true);
        grid1.addDragStartListener(this::handleDragStart);
        grid1.addDropListener(e -> {
            if (dragSourceGrid != null && dragSourceGrid.equals(grid2)) {
                // L'élément est en train d'être déplacé de grid2 à grid1
                dataView2.removeItem(draggedItem);
                dataView1.addItem(draggedItem);
            }
            // Réinitialise la source du glisser-déposer après utilisation
            dragSourceGrid = null;
        });

        grid1.addDragEndListener(this::handleDragEnd);

        grid2.setDropMode(GridDropMode.ON_GRID);
        grid2.setRowsDraggable(true);
        grid2.addDragStartListener(this::handleDragStart);
        grid2.addDropListener(e -> {
            dataView2.addItem(draggedItem);
        });
        grid2.addDragEndListener(this::handleDragEnd);

        grid2.setDropMode(GridDropMode.BETWEEN);
        grid2.setRowsDraggable(true);

        grid2.addDragStartListener(e -> draggedItem = e.getDraggedItems().get(0));

        grid2.addDropListener(e -> {
            MatPremiere targetOperation = e.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = e.getDropLocation();

            boolean operationWasDroppedOntoItself = draggedItem.equals(targetOperation);

            if (targetOperation == null || operationWasDroppedOntoItself)
                return;

            dataView2.removeItem(draggedItem);

            if (dropLocation == GridDropLocation.BELOW) {
                dataView2.addItemAfter(draggedItem, targetOperation);
            } else {
                dataView2.addItemBefore(draggedItem, targetOperation);
            }
        });

        grid2.addDragEndListener(e -> draggedItem = null);

        Div container = new Div(grid1, grid2);
        setContainerStyles(container);

        add(container);
        configureFooter();
    }


    private  Grid<MatPremiere> setupGrid(String header, boolean addQuantColumn) {
        Grid<MatPremiere> grid = new Grid<>(MatPremiere.class);
        grid.removeAllColumns();

        if (addQuantColumn) {
            grid.addComponentColumn(item -> createQuantPeaker(grid, item))
                    .setHeader("Quantite").setSortable(true).setAutoWidth(true);
        }

        grid.addColumn(MatPremiere::getDes).setHeader(header);

        setGridStyles(grid);
        return grid;
    }

    private static void setGridStyles(Grid<MatPremiere> grid) {
        grid.getStyle().set("width", "300px").set("height", "300px")
                .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                .set("align-self", "unset");
    }

    private  NumberField createQuantPeaker(Grid<MatPremiere> grid, MatPremiere item) {
        NumberField quantPicker = new NumberField();
        if(item != null && QuantMatPremiereMap.containsKey(item)) {
            quantPicker.setValue(QuantMatPremiereMap.get(item).getValue());
        }
        else if(item != null) {
            quantPicker.setValue(service.getQuantiteForProduit(produit, item));
        }
        quantPicker.setMin(0.0);
        Div suffix = new Div();
        suffix.setText("kg");
        quantPicker.setSuffixComponent(suffix);
        QuantMatPremiereMap.put(item, quantPicker);
        return quantPicker;
    }

    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }

    private void configureFooter() {
        Button saveButton = new Button("Enregistrer", e -> {
            save();
            this.close();
            notifySaveListeners();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("margin-right", "auto");
        this.getFooter().add(saveButton);

        Button cancelButton = new Button("Cancel", (e) -> this.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        this.getFooter().add(cancelButton);
    }

    private void notifySaveListeners() {
        for (SaveListenerDefMat listener : saveListenerDefMats) {
            listener.onSave();
        }
    }

    public void addSaveListener(SaveListenerDefMat listener) {
        saveListenerDefMats.add(listener);
    }

    private void handleDragStart(GridDragStartEvent<MatPremiere> e) {
        draggedItem = e.getDraggedItems().get(0);
        dragSourceGrid = e.getSource();
    }

    private void handleDragEnd(GridDragEndEvent<MatPremiere> e) {
        draggedItem = null;
    }

    private void save() {
        //TODO : Enregistrer les Matiere définies pour un produit
        List<MatiereProduit> matiereProduitsList = new ArrayList<>();
        for (MatPremiere matPremiere : matpremieresDefini) {
            MatiereProduit matiereProduit = new MatiereProduit();
            matiereProduit.setProduit(produit);
            matiereProduit.setMatPremiere(matPremiere);
            double quantite = QuantMatPremiereMap.get(matPremiere).getValue();
            matiereProduit.setQuantite(quantite);
            matiereProduitsList.add(matiereProduit);
        }
        service.saveAllMatiereProduit(matiereProduitsList);
        if (produitDetails != null) {
            produitDetails.refreshOperations();
        }
        notifySaveListeners();
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public ProductView.ProduitDetails getProduitDetails() {
        return produitDetails;
    }

    public void setProduitDetails(ProductView.ProduitDetails produitDetails) {
        this.produitDetails = produitDetails;
    }
}

