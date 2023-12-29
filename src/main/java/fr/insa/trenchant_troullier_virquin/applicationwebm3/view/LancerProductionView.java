package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.util.List;

@Route(value = "lancerProduction/:commandeID", layout = MainLayout.class)
public class LancerProductionView extends VerticalLayout implements BeforeEnterObserver {
    private final CrmService service;
    private Grid<DefinitionCommande> gridDefinitionCommande;
    private ComboBox<Machine> machineComboBox;
    private ComboBox<Produit> produitComboBox;
    private Grid<Operation> gridEtapes;
    private Long commandeId;

    public LancerProductionView(CrmService service) {
        this.service = service;
        initGridEtapes();
    }
    //Cette méthode est appelée avant que la vue ne soit affichée afin de récupérer l'ID de la commande
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        commandeId = Long.valueOf(event.getRouteParameters().get("commandeID").orElse("0"));
        if (commandeId > 0) {
            Commande commande = service.findCommandeById(commandeId);
            if (commande != null) {
                // Utiliser la commande pour initialiser les composants
                setupProduitComboBox(commande);

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

    private void initGridEtapes() {
        gridEtapes = new Grid<>(Operation.class);
        gridEtapes.removeAllColumns();
        gridEtapes.addColumn(Operation -> Operation.getTypeOperation().getDes()).setHeader("Nom de l'opération");
        gridEtapes.addColumn(Operation::getOrdre).setHeader("Ordre").setSortable(true);
        // Ajoute une colonne avec un bouton dans chaque ligne
        gridEtapes.addComponentColumn(operation -> {
            Button choixMachineBtn = new Button("Associer une machine", event -> {
                openMachineDialog(operation);
            });
            return choixMachineBtn;
        });
    }

    private void setupProduitComboBox(Commande commande) {
        produitComboBox = new ComboBox<>("Choisir un produit");
        produitComboBox.setItemLabelGenerator(Produit::getDes);
        produitComboBox.setAllowCustomValue(false);
        produitComboBox.setAllowedCharPattern("[]");

        produitComboBox.addValueChangeListener(e -> {
            Produit selectedProduit = e.getValue();
            if (selectedProduit != null) {
                // Mettre à jour les éléments du grid avec les opérations du produit sélectionné
                gridEtapes.setItems(service.findOperationByProduit(selectedProduit));
                this.add(gridEtapes);
            } else {
                // Si aucun produit n'est sélectionné, effacer les éléments du grid
                gridEtapes.setItems();
            }
        });
        // Remplir le ComboBox avec les produits disponibles
        produitComboBox.setItems(service.findAllProduitByCommande(commande));
        this.add(produitComboBox);
    }

    private void openMachineDialog(Operation operation) {
        Dialog dialog = createDialog();
        FormLayout form = createMachineSelectionForm(dialog, operation);
        dialog.add(form);
        dialog.open();
    }

    private Dialog createDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
        return dialog;
    }

    private FormLayout createMachineSelectionForm(Dialog dialog, Operation operation) {
        FormLayout form = new FormLayout();
        form.addClassName("dialog-form");
        ComboBox<Machine> machineComboBox = setupMachineComboBox(operation);
        form.add(machineComboBox);
        HorizontalLayout buttonsLayout = createDialogButtonsLayout(dialog, machineComboBox);
        form.add(buttonsLayout);
        return form;
    }

    private ComboBox<Machine> setupMachineComboBox(Operation operation) {
        ComboBox<Machine> machineComboBox = new ComboBox<>("Veuillez choisir une machine disponible et compatible:");
        machineComboBox.setItemLabelGenerator(Machine::getDes);

        // Récupère les machines disponibles et dont le type d'opération correspond à celui de l'étape sélectionnée
        List<Machine> machinesDisponibles = service.findAllMachineDisponiblesForTypeOperation(operation.getTypeOperation().getId());
        machineComboBox.setItems(machinesDisponibles);

        return machineComboBox;
    }

    private HorizontalLayout createDialogButtonsLayout(Dialog dialog, ComboBox<Machine> machineComboBox) {
        Button confirmBtn = new Button("Confirmer", event -> {
            // Ici tu définis ce qui se passe quand l'utilisateur clique sur le bouton
            // Par exemple, mettre à jour la machine sélectionnée dans le grid
            dialog.close();
        });
        Button cancelBtn = new Button("Annuler", event -> {
            dialog.close();
        });
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        H1 titre = new H1("");
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmBtn, titre, cancelBtn);
        buttonsLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        buttonsLayout.expand(titre);
        buttonsLayout.addClassNames(LumoUtility.Padding.Vertical.NONE, LumoUtility.Padding.Horizontal.MEDIUM);
        return buttonsLayout;
    }

}
