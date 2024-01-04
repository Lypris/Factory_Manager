package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route(value = "lancerproduction/:commandeID", layout = MainLayout.class)
@PageTitle("Lancement Production | Factory Manager")
public class LancerProductionView extends VerticalLayout implements BeforeEnterObserver {
    private final CrmService service;
    // Ajoutez un champ pour stocker les ComboBox des machines
    private final List<ComboBox<Machine>> machineComboBoxes = new ArrayList<>();
    private final HorizontalLayout entete = new HorizontalLayout();
    private final Span label = new Span(" ");
    private final Button lancerProdCommande; //Button pour finaliser la commande
    private final Button lancerProductionButton; // Bouton pour lancer la production
    private Grid<DefinitionCommande> gridDefinitionCommande;
    private ComboBox<Machine> machineComboBox;
    private ComboBox<Produit> produitComboBox;
    private Grid<Operation> gridEtapes;
    private Long commandeId;
    private Commande commande;
    private List<Produit> ListProduitCommande = new ArrayList<>();

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
            update();
            lancerProductionButton.setEnabled(false);
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
        updateLancerProdCommande();
        updateLancerProductionButtonState();
    }

    //Methode pour lancer la production d'un produit
    private void lancerProdProduit(Produit produit) {
        // Récupérer les machines sélectionnées
        List<Machine> Listmachines = new ArrayList<>();
        for (ComboBox<Machine> comboBox : machineComboBoxes) {
            Listmachines.add(comboBox.getValue());
        }
        //Vérifier qu'il y ait suffisamment de matière première
        if (AssezMatPremiere(produit, this.commande)){
            
            //créer toutes les OperationEffectuee associées
            CreerToutesOperationEffectuee(commandeId, produit, Listmachines);
            //Change l'etat des machine pour les mettre en production
            MettreMachinEnProduction(Listmachines);
            //Met a jour le stock de matiere premiere
            MiseAJourMatierePremiere(produit, commande);
            //Supprime le produit qui vient d'etre validé
            this.ListProduitCommande.remove(produit);
            //Met à jour l'interface
            update();
            // Afficher une notification
            Notification.show("Production lancée");   
        }else{
            Notification.show("PAS ASSEZ DE MATIERE");
        }
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
                updateProduitEnProd();
                this.lancerProductionButton.setEnabled(false);
                this.commande = service.findCommandeById(commandeId);
            } else {
                // Gérer le cas où la commande n'existe pas
                event.rerouteTo(DashboardView.class);
            }
        } else {
            // Gérer le cas où l'ID de la commande n'est pas fourni ou est invalide
            event.rerouteTo(DashboardView.class);
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
        boolean allSelected = machineComboBoxes.stream().allMatch(comboBox -> comboBox.getValue() != null);
        // Activez ou désactivez le bouton en fonction de la sélection
        lancerProductionButton.setEnabled(allSelected);
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
        configureEntete();
        updateProduitEnProd();
    }

    //méthode pour configurer l'entête de la vue
    private void configureEntete() {
        // Ajouter le ComboBox des produits à l'entête
        entete.add(produitComboBox);
        // Ajouter le bouton pour lancer la production à l'entête
        entete.add(lancerProductionButton);
        // Ajouter le label à l'entête
        entete.add(label);
        // Aligner les composants à gauche
        entete.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

    }

    //Met à jour les element du Combobox des produits
    private void updateProduitComboBox() {
        
        this.produitComboBox.setItems(this.ListProduitCommande);
        lancerProdCommande.setEnabled(this.ListProduitCommande.isEmpty());
    }

    //Methode qui met ajours les produit de la commande qui ne sont pas en production 
    private void updateProduitEnProd() {
        Commande commande = service.findCommandeById(this.commandeId);
        if (this.ListProduitCommande.size() == 1) {
            List<Operation> Listoperations = service.findOperationByProduit(this.ListProduitCommande.get(0));
            List<Exemplaires> ListExemplaires = service.findAllByCommandeAndProduit(commande, this.ListProduitCommande.get(0));
            for (Exemplaires e : ListExemplaires) {
                for (Operation o : Listoperations) {
                    //Si il existe déja une opeartion-Effectuee alors on supprime le produit de la liste des produit pas encore en production
                    if (service.OperationEffectueeExiste(e, o) && !this.ListProduitCommande.isEmpty()) {
                        this.ListProduitCommande.remove(this.ListProduitCommande.get(0));
                    }
                }
            }
        } else if (this.ListProduitCommande.size() > 1) {
            for (Produit prod : this.ListProduitCommande) {
                List<Operation> Listoperations = service.findOperationByProduit(prod);
                List<Exemplaires> ListExemplaires = service.findAllByCommandeAndProduit(commande, prod);
                for (Exemplaires e : ListExemplaires) {
                    for (Operation o : Listoperations) {
                        //Si il existe déja une opeartion-Effectuee alors on supprime le produit de la liste des produit pas encore en production
                        if (service.OperationEffectueeExiste(e, o)) {
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

    private void CreerToutesOperationEffectuee(Long commandeId, Produit prod, List<Machine> Listmachines) {
        //méthode qui récupère la commande avec l'ID
        Commande commande = service.findCommandeById(commandeId);
        //Methode qui récupère tous les exemplaires
        List<Exemplaires> ListExemplaires = service.findAllByCommandeAndProduit(commande, prod);
        int i = 0;
        //Boucles qui crée tous les operation_effectuee nécessaire a la production
        for (Exemplaires e : ListExemplaires) {
            i = 0;
            /*e.setEtape(0);
            service.saveExemplaire(e);*/
            for (Operation o : service.findOperationByProduit(prod)) {
                //Verifie qu'il n'existe pas encore d'operation effectuee <-> production de produit pas lancée
                //Si la prod du produit n'est pas lancée on crée tous les operation-effectuee
                if (!service.OperationEffectueeExiste(e, o)) {
                    Operation_Effectuee ope_eff = new Operation_Effectuee(e, Listmachines.get(i), o);
                    service.saveOpperation_Effectuee(ope_eff);
                } else {
                    Notification.show("La production de ce produit est déja lancée");
                    return;
                }
                i++;
            }
        }

    }

    //Met toutes les machines selectionnée en statut de production
    private void MettreMachinEnProduction(List<Machine> Listmachines) {
        for (Machine m : Listmachines) {
            //Recuper l'etat actuel et mettre l'heure de fin
            service.SetFinByEtatMachine(LocalDateTime.now(), service.findLastEtatMachineByMachine(m));
            //Creer un nouvel etat avec l'heure de début
            service.saveEtatMachine(new EtatMachine(LocalDateTime.now(), m, service.findEtatEnMarche()));
        }
    }

    //Change le statut de la commande et reviens sur la page production avec le update
    private void lancerProdCommande() {
        Commande commande = service.findCommandeById(this.commandeId);
        service.SetStatutCommande(commande, "En cours");
        update();
        Notification.show("La commande est en production");
    }

    //Met à jour tous les boutons et combobox et aussi la vue si la commande est en production
    private void update() {
        updateProduitComboBox();
        updateLancerProductionButtonState();
        updateLancerProdCommande();
        updateLabel();
        this.label.setText("");
        Commande commande = service.findCommandeById(this.commandeId);
        if (commande.getStatut().equals("En cours")) {
            getUI().ifPresent(ui -> ui.navigate("production"));
        }
    }

    //Met à jour le label en fonction du nombre de produit restant
    private void updateLabel() {
        if (this.ListProduitCommande.isEmpty()) {
            this.label.setText("Tous les produits sont en production");
        } else {
            this.label.setText(" ");
        }
    }
    
    private boolean AssezMatPremiere(Produit produit, Commande commande) {
        List<MatPremiere> ListMat = service.findAllMatPremiereForProduit(produit);
        if (ListMat.isEmpty()){
            Notification.show("Il n'y a pas de matiere première associée au produit");
            return false;
        }else{
            int nbExemplaires = service.countExemplairesByCommandeAndProduit(commande, produit);
            for (MatPremiere m : ListMat){
                List<MatiereProduit> ListMatPro = service.findMatProByProduitAndMatiere(produit, m);
                for(MatiereProduit mp :ListMatPro){
                    if (m.getQuantite() < mp.getQuantite()*nbExemplaires){
                        Notification.show("Il manque de "+m.getDes());
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void MiseAJourMatierePremiere(Produit produit, Commande commande){
        List<MatPremiere> ListMat = service.findAllMatPremiereForProduit(produit);
        int nbExemplaires = service.countExemplairesByCommandeAndProduit(commande, produit);
        for (MatPremiere m : ListMat){
            List<MatiereProduit> ListMatPro = service.findMatProByProduitAndMatiere(produit, m);
            for(MatiereProduit mp :ListMatPro){
                double q = m.getQuantite();
                m.setQuantite(q-mp.getQuantite()*nbExemplaires);
                service.saveMatPremiere(m);
            }
        }
    }

}
