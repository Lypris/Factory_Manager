package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service;

import com.vaadin.flow.component.notification.Notification;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.DefinitionCommande;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class CrmService {

    private final StatusRepository statusRepository;
    private final OperateurRepository operateurRepository;
    private final StatutOperateurRepository statutOperateurRepository;
    private final MachineRepository machineRepository;
    private final EtatPossibleMachineRepository etatPossibleMachineRepository;
    private final EtatMachineRepository etatMachineRepository;
    private final ProduitRepository produitRepository;
    private final CommandeRepository commandeRepository;
    private final DefinitionCommandeRepository definitionCommandeRepository;
    private final TypeOperationRepository typeOperationRepository;
    private final MatPremiereRepository matPremiereRepository;
    private final OperationRepository operationRepository;
    private final ExemplairesRepository exemplairesRepository;


    public CrmService(StatusRepository statusRepository,
                      OperateurRepository operateurRepository,
                      StatutOperateurRepository statutOperateurRepository,
                      MachineRepository machineRepository,
                      EtatPossibleMachineRepository etatPossibleMachineRepository,
                      EtatMachineRepository etatMachineRepository,
                      ProduitRepository produitRepositor,
                      CommandeRepository commandeRepository,
                      DefinitionCommandeRepository definitionCommandeRepository,
                      TypeOperationRepository typeOperationRepository,
                      MatPremiereRepository matPremiereRepository,
                      OperationRepository operationRepository,
                      ExemplairesRepository exemplairesRepository) {
        this.statusRepository = statusRepository;
        this.operateurRepository = operateurRepository;
        this.statutOperateurRepository = statutOperateurRepository;
        this.machineRepository = machineRepository;
        this.etatPossibleMachineRepository = etatPossibleMachineRepository;
        this.etatMachineRepository = etatMachineRepository;
        this.produitRepository = produitRepositor;
        this.commandeRepository = commandeRepository;
        this.typeOperationRepository = typeOperationRepository;
        this.definitionCommandeRepository = definitionCommandeRepository;
        this.matPremiereRepository = matPremiereRepository;
        this.operationRepository = operationRepository;
        this.exemplairesRepository = exemplairesRepository;
    }



    //////////////////////////// STATUT POSSIBLE OPERATEUR////////////////////////////
    public List<Statut> findAllStatuses(){
        return statusRepository.findAll();
    }


    //////////////////////////// OPERATEUR ////////////////////////////
    public List<Operateur> findAllOperateurs(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return operateurRepository.findAll();
        } else {
            return operateurRepository.search(stringFilter);
        }
    }

    public long countOperateurs() {
        return operateurRepository.count();
    }

    public void deleteOperateur(Operateur operateur) {
        List<StatutOperateur> statutOperateurs = statutOperateurRepository.findByOperateur(operateur);

        if (statutOperateurs.isEmpty()) {
            // No associated statutOperateurs, proceed with deletion
            operateurRepository.delete(operateur);
        } else {
            deleteAssociatedStatuts(statutOperateurs);
            operateurRepository.delete(operateur);
        }
    }

    public void deleteAssociatedStatuts(List<StatutOperateur> statutOperateurs) {
        for (StatutOperateur statutOperateur : statutOperateurs) {
            statutOperateurRepository.delete(statutOperateur);
        }
    }

    public void saveOperateur(Operateur operateur) {
        if (operateur == null) {
            System.err.println("Operator is null. Are you sure you have connected your form to the application?");
            return;
        }
        operateurRepository.save(operateur);
    }
    //////////////////////////// STATUT D'OPERATEUR ////////////////////////////

    public List<StatutOperateur> findAllStatutOperateurs(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return statutOperateurRepository.findAll();
        } else {
            return statutOperateurRepository.search(stringFilter);
        }
    }

    public long countStatutOperateurs() {
        return statutOperateurRepository.count();
    }

    public void deleteStatutOperateur(StatutOperateur statutOperateur) {
        statutOperateurRepository.delete(statutOperateur);
    }

    public void saveStatutOperateur(StatutOperateur statutOperateur) {
        if (statutOperateur == null) {
            System.err.println("StatutOperateur is null. Are you sure you have connected your form to the application?");
            return;
        }
        statutOperateurRepository.save(statutOperateur);
    }

    //////////////////////////// MACHINE ////////////////////////////
    public List<Machine> findAllMachines(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return machineRepository.findAll();
        } else {
            return machineRepository.search(stringFilter);
        }
    }
    public long countMachines() {
        return machineRepository.count();
    }

    public void deleteMachine(Machine machine) {
        List<EtatMachine> etatMachines = etatMachineRepository.findByMachine(machine);

        if (etatMachines.isEmpty()) {
            machineRepository.delete(machine);
        } else {
            deleteAssociatedEtatMachines(etatMachines);
            machineRepository.delete(machine);
        }
    }

    public void saveMachine(Machine machine) {
        if (machine == null) {
            System.err.println("Machine is null. Are you sure you have connected your form to the application?");
            return;
        }
        machineRepository.save(machine);
    }
    private void deleteAssociatedEtatMachines(List<EtatMachine> etatMachines) {
        for (EtatMachine etatMachine : etatMachines) {
            etatMachineRepository.delete(etatMachine);
        }
    }
    //////////////////////////// ETAT MACHINE ////////////////////////////
    public List<EtatMachine> findAllEtatMachines(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return etatMachineRepository.findAll();
        } else {
            return etatMachineRepository.search(stringFilter);
        }
    }
    public void deleteEtatMachine(EtatMachine etatMachine) {
        etatMachineRepository.delete(etatMachine);
    }
    public void saveEtatMachine(EtatMachine etatMachine) {
        if (etatMachine == null) {
            System.err.println("EtatMachine is null. Are you sure you have connected your form to the application?");
            return;
        }
        etatMachineRepository.save(etatMachine);
    }



    //////////////////////////// ETAT POSSIBLE MACHINE ////////////////////////////
    public List<EtatPossibleMachine> findAllEtatPossibleMachines(){
        return etatPossibleMachineRepository.findAll();
    }

    public void saveEtatPossibleMachine(EtatPossibleMachine etatPossibleMachine) {
        if (etatPossibleMachine == null) {
            System.err.println("EtatPossibleMachine is null. Are you sure you have connected your form to the application?");
            return;
        }
        etatPossibleMachineRepository.save(etatPossibleMachine);
    }


    //////////////////////////// PRODUIT ////////////////////////////
    public List<Produit> findAllProduits(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return produitRepository.findAll();
        } else {
            return produitRepository.search(stringFilter);
        }
    }
    public long countProduits() {
        return produitRepository.count();
    }
    public void saveProduit(Produit produit) {
        if (produit == null) {
            System.err.println("Produit is null. Are you sure you have connected your form to the application?");
            return;
        }
        produitRepository.save(produit);
    }
    public void deleteProduit(Produit produit) {
        produitRepository.delete(produit);
    }
    //////////////////////////// Commmande ////////////////////////////
    public List<Commande> findAllCommande(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return commandeRepository.findAll();
        } else {
            return commandeRepository.search(stringFilter);
        }
    }
    public long countCommande() {
        return commandeRepository.count();
    }

    public void deleteCommande(Commande commande) {
        List<DefinitionCommande> defCommande = definitionCommandeRepository.findByIdCommande(commande.getId());

        if (defCommande.isEmpty()) {
            commandeRepository.delete(commande);
        } else {
            deleteAssociateDefinitionCommande(defCommande);
            commandeRepository.delete(commande);
        }
    }
    private void deleteAssociateDefinitionCommande(List<DefinitionCommande> defCommande) {
        for (DefinitionCommande def : defCommande) {
            definitionCommandeRepository.delete(def);
        }
    }

    public void saveCommande(Commande commande) {
        if (commande == null) {
            System.err.println("Commande is null. Are you sure you have connected your form to the application?");
            return;
        }
        if (commande.getDebut() == null){
            commande.setDebut(LocalDateTime.now());
        }
        if ((commande.getStatut().equals("Terminée") || commande.getStatut().equals("Annulée"))
                && commande.getFin() == null) {
            commande.setFin(LocalDateTime.now());
        }else commande.setFin(null);
        commandeRepository.save(commande);
    }
    //////////////////////////Defintion Commande ////////////////////////////

    public void saveDefinitionCommande(DefinitionCommande definitionCommande) {
        if (definitionCommande == null) {
            System.err.println("DefinitionCommande is null. Are you sure you have connected your form to the application?");
            return;
        }
        definitionCommandeRepository.save(definitionCommande);
    }
    public ArrayList findAllDefinitionCommandeByCommande(Commande commande) {
        return (ArrayList) definitionCommandeRepository.findByIdCommande(commande.getId());
    }
    public void deleteDefinitionCommande(DefinitionCommande definitionCommande) {
        definitionCommandeRepository.delete(definitionCommande);
    }
    // type d'opération
    //////////////////////////// TYPE OPERATION ////////////////////////////
    public List<TypeOperation> findAllTypeOperation(){
        return typeOperationRepository.findAll();
    }

    public void saveTypeOperation(TypeOperation typeOperation) {
        if (typeOperation == null) {
            System.err.println("TypeOperation is null. Are you sure you have connected your form to the application?");
            return;
        }
        typeOperationRepository.save(typeOperation);
    }
    public List<TypeOperation> findAllTypeOperationForProduit(Produit produit) {
        List<Operation> operationsproduit = this.findOperationByProduit(produit);
        List<TypeOperation> typeOperations = new ArrayList<>();
        for (Operation operation : operationsproduit) {
            typeOperations.add(operation.getTypeOperation());
        }
        return typeOperations;
    }
    public void deleteTypeOperation(TypeOperation typeOperation) {
        typeOperationRepository.delete(typeOperation);
    }
    //////////////////////// MATIERE PREMIERE ////////////////////////////
    public List<MatPremiere> findAllMatPremiere(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return matPremiereRepository.findAll();
        } else {
            return matPremiereRepository.search(stringFilter);
        }
    }

    //////////////////////// OPERATION ////////////////////////////
    public List<Operation> findAllOperation() {
        return operationRepository.findAll();
    }
    //méthode pour récupérer les opérations d'un produit
    public List<Operation> findOperationByProduit(Produit produit) {
        if(produit!=null){
            return operationRepository.findByProduit(produit.getId());
        }
        else return null;
    }
    public void deleteAllOperationForProduit(Produit produit) {
        List<Operation> operations = operationRepository.findByProduit(produit.getId());
        for (Operation operation : operations) {
            operationRepository.delete(operation);
        }
    }
    public void saveOperation(Operation operation) {
        if (operation == null) {
            System.err.println("Operation is null. Are you sure you have connected your form to the application?");
            return;
        }
        operationRepository.save(operation);
    }
    public void deleteOperation(Operation operation) {
        operationRepository.delete(operation);
    }

    //////////////////////// Exemplaire ////////////////////////////
    public void saveExemplaire(Exemplaires exemplaire) {
        if (exemplaire == null) {
            System.err.println("Exemplaire is null. Are you sure you have connected your form to the application?");
            return;
        }
        exemplairesRepository.save(exemplaire);
    }

    public List<Exemplaires> findAllProdEnCours() {
        return exemplairesRepository.findAllProdEnCours();
    }

    public List<Exemplaires> findAllProdFini() {
        return exemplairesRepository.findAllProdFini();
    }

}