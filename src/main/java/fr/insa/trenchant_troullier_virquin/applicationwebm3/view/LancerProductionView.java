package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "lancerproduction/:commandeID", layout = MainLayout.class)
public class LancerProductionView extends VerticalLayout implements BeforeEnterObserver {
    private final CrmService service;
    private Grid<DefinitionCommande> gridDefinitionCommande;
    private ComboBox<Machine> machineComboBox;
    private ComboBox<Produit> produitComboBox;
    private Grid<Operation> gridEtapes;
    private Long commandeId;
    private List<Produit> ListProduitCommande = new ArrayList<>();
    // Ajoutez un champ pour stocker les ComboBox des machines
    private List<ComboBox<Machine>> machineComboBoxes = new ArrayList<>();
    
    private HorizontalLayout entete = new HorizontalLayout();
    private Label label = new Label(" ");
    private Button lancerProdCommande; //Button pour finaliser la commande
    private Button lancerProductionButton; // Bouton pour lancer la production

    public LancerProductionView(CrmService service) {
        this.service = service;
        initGridEtapes();
        // Initialiser le bouton pour lancer la production
        lancerProductionButton = new Button("Valider la production");
        lancerProductionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        lancerProductionButton.setEnabled(false);
        lancerProductionButton.addClickListener(event -> {
            //méthode pour lancer la production
            lancerProdProduit(produitComboBox.getValue());
        });
        
        //Initialise le bouton pour finaliser la commander
        lancerProdCommande = new Button("Valider la Commande");
        lancerProdCommande.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        lancerProdCommande.setEnabled(false);
        lancerProdCommande.addClickListener(event -> {
            //méthode pour finaliser la commande
            lancerProdCommande();
        });

        // Ajouter le bouton à la vue
        add(lancerProdCommande, entete);
    }
    
    //Methode pour lancer la production d'un produit
    private void lancerProdProduit(Produit produit) {
        // Récupérer les machines sélectionnées
        List<Machine> Listmachines = new ArrayList<>();
        for (ComboBox<Machine> comboBox : machineComboBoxes) {
            Listmachines.add(comboBox.getValue());
        }
        //Récupérer les opérations
        List<Operation> Listoperations = gridEtapes.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
        //TODO: Vérifier qu'il y ait suffisamment de matière première

        //créer toutes les OperationEffectuee associées
        CreerToutesOperationEffectuee(commandeId, produit, Listoperations, Listmachines);
        //Change l'etat des machine pour les mettre en production
        MettreMachinEnProduction(Listmachines);
        //Supprime le produit qui vient d'etre validé
        this.ListProduitCommande.remove(produit);
        //Met à jour l'interface
        update();
        // Afficher une notification
        Notification.show("Production lancée");
    }

    //Cette méthode est appelée avant que la vue ne soit affichée afin de récupérer l'ID de la commande
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        commandeId = Long.valueOf(event.getRouteParameters().get("commandeID").orElse("0"));
        this.ListProduitCommande = this.service.findAllProduitByCommande(this.service.findCommandeById(commandeId));
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
    
    //Initialise le grilles de operations
    private void initGridEtapes() {
        gridEtapes = new Grid<>(Operation.class);
        gridEtapes.removeAllColumns();
        gridEtapes.addColumn(Operation -> Operation.getTypeOperation().getDes()).setHeader("Nom de l'opération");
        gridEtapes.addColumn(Operation::getOrdre).setHeader("Ordre").setSortable(true);
        // Ajoute une colonne avec un bouton dans chaque ligne
        gridEtapes.addComponentColumn(operation -> {
            ComboBox<Machine> machineComboBox = setupMachineComboBox(operation);
            machineComboBoxes.add(machineComboBox); // Ajoutez le ComboBox à la liste
            machineComboBox.addValueChangeListener(event -> updateLancerProductionButtonState());
            return machineComboBox;
        });
    }
    private void updateLancerProductionButtonState() {
        // Vérifiez si tous les ComboBox ont une machine sélectionnée
        boolean allSelected = machineComboBoxes.stream()
                .allMatch(comboBox -> comboBox.getValue() != null);
        // Activez ou désactivez le bouton en fonction de la sélection
        lancerProductionButton.setEnabled(allSelected);
        lancerProductionButton.setEnabled(!this.ListProduitCommande.isEmpty());
    }
    private void updateLancerProdCommande() {
        // Vérifiez si tous les produits sont en production et active le bouton en fonction
        lancerProdCommande.setEnabled(this.ListProduitCommande.isEmpty());
    }
    
    //Initialise le Combobox des Produits
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
        produitComboBox.setItems(this.ListProduitCommande);
        this.entete.add(this.produitComboBox, this.lancerProductionButton, this.label);
        updateProduitEnProd();
    }
    
    //Met à jour les element du Combobox des produits
    private void updateProduitComboBox(){
        this.produitComboBox.setItems(this.ListProduitCommande);
    }
    
    //Methode qui met ajours les produit de la commande qui ne sont pas en production 
    private void updateProduitEnProd(){ 
        Commande commande = service.findCommandeById(this.commandeId);
        if(this.ListProduitCommande.size() == 1){
            Notification.show(String.valueOf(this.ListProduitCommande.size()));
            List<Operation> Listoperations = service.findOperationByProduit(this.ListProduitCommande.get(0));
            List<Exemplaires> ListExemplaires = service.findAllByCommandeAndProduit(commande, this.ListProduitCommande.get(0));
            for (Exemplaires e : ListExemplaires){
                for (Operation o : Listoperations){
                    //Si il existe déja une opeartion-Effectuee alors on supprime le produit de la liste des produit pas encore en production
                    if (service.OperationEffectueeExiste(e, o) && !this.ListProduitCommande.isEmpty()){
                        this.ListProduitCommande.remove(this.ListProduitCommande.get(0));
                    }
                }
            }
        }else if(this.ListProduitCommande.size() > 1){
            for(Produit prod : this.ListProduitCommande){
                List<Operation> Listoperations = service.findOperationByProduit(prod);
                List<Exemplaires> ListExemplaires = service.findAllByCommandeAndProduit(commande, prod);
                for (Exemplaires e : ListExemplaires){
                    for (Operation o : Listoperations){
                        //Si il existe déja une opeartion-Effectuee alors on supprime le produit de la liste des produit pas encore en production
                        if (service.OperationEffectueeExiste(e, o)){
                            this.ListProduitCommande.remove(prod);
                        }
                    }
                }
            }
        }
        updateProduitComboBox();
    }
    
    //Initialise les ComboBox des machhines
    private ComboBox<Machine> setupMachineComboBox(Operation operation) {
        ComboBox<Machine> machineComboBox = new ComboBox<>("Veuillez choisir une machine disponible et compatible:");
        machineComboBox.setItemLabelGenerator(Machine::getDes);
        machineComboBox.setAllowCustomValue(false);
        machineComboBox.setAllowedCharPattern("[]");
        //setwitdh("100%") pour que la liste déroulante soit aussi large que le composant
        machineComboBox.setWidth("100%");

        // Récupère les machines disponibles et dont le type d'opération correspond à celui de l'étape sélectionnée
        List<Machine> machinesDisponibles = service.findAllMachineDisponiblesForTypeOperation(operation.getTypeOperation());
        machineComboBox.setItems(machinesDisponibles);

        return machineComboBox;
    }

    private void CreerToutesOperationEffectuee(Long commandeId, Produit prod, List<Operation> Listoperations, List<Machine> Listmachines) {
        //méthode qui récupère la commande avec l'ID
        Commande commande = service.findCommandeById(commandeId);
        //Methode qui récupère tous les exemplaires
        List<Exemplaires> ListExemplaires = service.findAllByCommandeAndProduit(commande, prod);
        int i = 0;
        //Boucles qui crée tous les operation_effectuee nécessaire a la production
        for (Exemplaires e : ListExemplaires){
            i = 0;
            for (Operation o : Listoperations){
                //Verifie qu'il n'existe pas encore d'operation effectuee <-> production de produit pas lancée
                //Si la prod du produit n'est pas lancée on crée tous les operation-effectuee
                if (!service.OperationEffectueeExiste(e, o)){
                    Operation_Effectuee ope_eff = new Operation_Effectuee(e, Listmachines.get(i), o);
                    service.saveOpperation_Effectuee(ope_eff);
                }else {
                    Notification.show("La production de ce produit est déja lancée");
                    return;
                }
                i++;
            }
        }
        
    }
    
    //Met toutes les machines selectionnée en statut de production
    private void MettreMachinEnProduction(List<Machine> Listmachines) {
        for (Machine m : Listmachines){
            //Recuper l'etat actuel et mettre l'heure de fin
            service.SetFinByEtatMachine(LocalDateTime.now(),service.findLastEtatMachineByMachine(m));
            //Creer un nouvel etat avec l'heure de début
            service.saveEtatMachine(new EtatMachine(LocalDateTime.now(), m, service.findEtatEnMarche()));
        }
        
        
    }
    
    //Change le statut de la commande est reviens sur la page production avec le update
    private void lancerProdCommande() {
        Commande commande = service.findCommandeById(this.commandeId);
        service.SetStatutCommande(commande, "En cours");
        update();
        Notification.show("LA COMMANDE EST EN PRODUCTION");
    }

    //Met à jour tous les boutons et combobox et aussi la vue si la commande est en production
    private void update() {
        updateProduitComboBox();
        updateLancerProductionButtonState();
        updateLancerProdCommande();
        updateLabel();
        Commande commande = service.findCommandeById(this.commandeId);
        if (commande.getStatut().equals("En cours")){
            getUI().ifPresent(ui -> ui.navigate("production"));
        }
    }
    
    //Met à jour le label en fonction du nombre de produit restant
    private void updateLabel() {
        if (this.ListProduitCommande.isEmpty()){
            this.label.setText("Tous les produits sont en production");
        }else{
            this.label.setText(" ");
        }
    }

}
