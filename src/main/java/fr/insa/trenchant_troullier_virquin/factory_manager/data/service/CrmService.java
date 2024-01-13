package fr.insa.trenchant_troullier_virquin.factory_manager.data.service;

import com.vaadin.flow.component.notification.Notification;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Commande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.DefinitionCommande;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.EtatPossibleMachine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Exemplaires;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Habilitation;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.MatPremiere;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.MatiereProduit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Operation_Effectuee;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.PosteDeTravail;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Produit;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.CommandeRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.DefinitionCommandeRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.EtatMachineRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.EtatPossibleMachineRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.ExemplairesRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.HabilitationRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.MachineRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.MatPremiereRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.MatiereProduitRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.OperateurRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.OperationRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.Operation_EffectueeRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.PosteDeTravailRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.ProduitRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.StatusRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.StatutOperateurRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.TypeOperationRepository;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.repository.UtiliseRepository;

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
    private final MatiereProduitRepository matiereProduitRepository;
    private final OperationRepository operationRepository;
    private final ExemplairesRepository exemplairesRepository;
    private final PosteDeTravailRepository posteDeTravailRepository;
    private final UtiliseRepository utiliseRepository;
    private final HabilitationRepository habilitationRepository;
    private final Operation_EffectueeRepository operation_EffectueeRepository;


    public CrmService(StatusRepository statusRepository,
                      OperateurRepository operateurRepository,
                      StatutOperateurRepository statutOperateurRepository,
                      MachineRepository machineRepository,
                      EtatPossibleMachineRepository etatPossibleMachineRepository,
                      EtatMachineRepository etatMachineRepository,
                      ProduitRepository produitRepository,
                      CommandeRepository commandeRepository,
                      DefinitionCommandeRepository definitionCommandeRepository,
                      TypeOperationRepository typeOperationRepository,
                      MatPremiereRepository matPremiereRepository,
                      OperationRepository operationRepository,
                      ExemplairesRepository exemplairesRepository,
                      PosteDeTravailRepository posteDeTravailRepository,
                      UtiliseRepository utiliseRepository,
                      HabilitationRepository habilitationRepository,
                      Operation_EffectueeRepository operation_EffectueeRepository,
                      MatiereProduitRepository matiereProduitRepository) {
        this.statusRepository = statusRepository;
        this.operateurRepository = operateurRepository;
        this.statutOperateurRepository = statutOperateurRepository;
        this.machineRepository = machineRepository;
        this.etatPossibleMachineRepository = etatPossibleMachineRepository;
        this.etatMachineRepository = etatMachineRepository;
        this.produitRepository = produitRepository;
        this.commandeRepository = commandeRepository;
        this.typeOperationRepository = typeOperationRepository;
        this.definitionCommandeRepository = definitionCommandeRepository;
        this.matPremiereRepository = matPremiereRepository;
        this.operationRepository = operationRepository;
        this.exemplairesRepository = exemplairesRepository;
        this.posteDeTravailRepository = posteDeTravailRepository;
        this.utiliseRepository = utiliseRepository;
        this.habilitationRepository = habilitationRepository;
        this.operation_EffectueeRepository = operation_EffectueeRepository;
        this.matiereProduitRepository = matiereProduitRepository;

        verifierEtatPossibleMachineIntiaux();
        verifierStatutPossibleOperateurInitiaux();
    }

    private void verifierEtatPossibleMachineIntiaux() {
        //vérifier que les etats possibles de machines existent déjà
        if (etatPossibleMachineRepository.findEtatPossibleByDes("disponible") == null) {
            EtatPossibleMachine etatPossibleMachine = new EtatPossibleMachine();
            etatPossibleMachine.setDes("disponible");
            etatPossibleMachineRepository.save(etatPossibleMachine);
        }
        if (etatPossibleMachineRepository.findEtatPossibleByDes("en panne") == null) {
            EtatPossibleMachine etatPossibleMachine = new EtatPossibleMachine();
            etatPossibleMachine.setDes("en panne");
            etatPossibleMachineRepository.save(etatPossibleMachine);
        }
        if (etatPossibleMachineRepository.findEtatPossibleByDes("éteinte") == null) {
            EtatPossibleMachine etatPossibleMachine = new EtatPossibleMachine();
            etatPossibleMachine.setDes("éteinte");
            etatPossibleMachineRepository.save(etatPossibleMachine);
        }
        if (etatPossibleMachineRepository.findEtatPossibleByDes("en marche") == null) {
            EtatPossibleMachine etatPossibleMachine = new EtatPossibleMachine();
            etatPossibleMachine.setDes("en marche");
            etatPossibleMachineRepository.save(etatPossibleMachine);
        }

    }
    
    private void verifierStatutPossibleOperateurInitiaux(){
        if (statusRepository.findStatutPossibleByDes("disponible") == null){
            Statut statut = new Statut();
            statut.setName("disponible");
            statusRepository.save(statut);
        }
        if (statusRepository.findStatutPossibleByDes("en congé") == null){
            Statut statut = new Statut();
            statut.setName("en congé");
            statusRepository.save(statut);
        }
        if (statusRepository.findStatutPossibleByDes("absent") == null){
            Statut statut = new Statut();
            statut.setName("absent");
            statusRepository.save(statut);
        }
    }

    //////////////////////////// STATUT POSSIBLE OPERATEUR////////////////////////////
    public List<Statut> findAllStatuses() {
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

    public List<StatutOperateur> findAllStatutOperateurByOperateur(Operateur operateur) {
        return statutOperateurRepository.findByOperateur(operateur);
    }
    public List<StatutOperateur> findAllStatutOperateurFinisByOperateur(Operateur operateur) {
        return statutOperateurRepository.findAllStatutOperateurFinisByOperateur(operateur);
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

    public List<Machine> findAllMachineByPosteDeTravail(PosteDeTravail posteDeTravail) {
        return machineRepository.findByPosteDeTravail(posteDeTravail);
    }

    public List<Machine> findAllMachineDisponibles() {
        return machineRepository.findAllMachineDisponibles();
    }

    public List<Machine> findAllMachineDisponiblesForTypeOperation(TypeOperation typeOperation) {
        return machineRepository.findAllMachineDisponiblesForTypeOperation(typeOperation);
    }
    
    public List<Machine> findAllMachineByExemplaire(Exemplaires exemplaire){
        return machineRepository.findAllMachineByExemplaire(exemplaire);
    }

    //////////////////////////// ETAT MACHINE ////////////////////////////
    public List<EtatMachine> findAllEtatMachines(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return etatMachineRepository.findAll();
        } else {
            return etatMachineRepository.search(stringFilter);
        }
    }

    public List<EtatMachine> findAllLastEtatMachines(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return etatMachineRepository.findAllByFinIsNull();
        } else {
            return etatMachineRepository.searchLast(stringFilter);
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

    public List<EtatMachine> findAllEtatMachineByMachine(Machine machine) {
        return etatMachineRepository.findByMachine(machine);
    }
    public List<EtatMachine> findAllEtatMachineFinisByMachine(Machine selectedMachine) {
        return etatMachineRepository.findAllEtatMachineFinisByMachine(selectedMachine);
    }

    public EtatMachine findLastEtatMachineByMachine(Machine machine) {
        return etatMachineRepository.findCurrentEtatMachineByMachine(machine);
    }

    public void SetFinByEtatMachine(LocalDateTime fin, EtatMachine etatmachine) {
        etatMachineRepository.SetFinByEtatMachine(fin, etatmachine);
    }

    public EtatMachine findMostRecentEtatMachineByMachine(Machine machine) {
        List<EtatMachine> previousEtatMachines = etatMachineRepository.findPreviousEtatMachineByMachine(machine);

        // Retourne l'état le plus récent, ou null si aucun état n'est trouvé
        return previousEtatMachines.stream()
                .max(Comparator.comparing(EtatMachine::getDebut))
                .orElse(null);
    }

    public List<EtatMachine> findPreviousEtatsMachinesByMachine(Machine machine) {
        return etatMachineRepository.findPreviousEtatMachineByMachine(machine);
    }


    //////////////////////////// ETAT POSSIBLE MACHINE ////////////////////////////
    public List<EtatPossibleMachine> findAllEtatPossibleMachines() {
        return etatPossibleMachineRepository.findAll();
    }

    public void saveEtatPossibleMachine(EtatPossibleMachine etatPossibleMachine) {
        if (etatPossibleMachine == null) {
            System.err.println("EtatPossibleMachine is null. Are you sure you have connected your form to the application?");
            return;
        }
        etatPossibleMachineRepository.save(etatPossibleMachine);
    }

    public EtatPossibleMachine findEtatEnMarche() {
        return etatPossibleMachineRepository.findEtatEnMarche();
    }

    public EtatPossibleMachine findEtatDisponible() {
        return etatPossibleMachineRepository.findEtatDisponible();
    }

    public EtatPossibleMachine findEtatPossibleByDes(String des) {
        return etatPossibleMachineRepository.findEtatPossibleByDes(des);
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
        exemplairesRepository.deleteAllExemplaireEnAttenteByProduit(produit);//supprime les exemplaires associés au produit
        definitionCommandeRepository.deleteAllDefinitionByProduit(produit);//supprime les définitions de commande associées au produit
        produitRepository.delete(produit);
    }

    public List<Produit> findAllProduitByCommande(Commande commande) {
        return produitRepository.findByCommande(commande);
    }
    
    public Produit findProduitByID(long ID){
        return produitRepository.findProduitByID(ID);
    }

    //////////////////////////// Commmande ////////////////////////////
    public List<Commande> findAllCommande(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return commandeRepository.findAll();
        } else {
            return commandeRepository.search(stringFilter);
        }
    }

    public Commande findCommandeById(Long id) {
        return commandeRepository.findById(id).get();
    }

    public long countCommandes() {
        return commandeRepository.count();
    }

    public void deleteCommande(Commande commande) {
        List<Exemplaires> exemplaires = exemplairesRepository.findByCommande(commande);
        //si la commande est en cours, elle a des opérations effectuées et des machines occupées
        if (commande.getStatut().equals("En cours") || commande.getStatut().equals("En attente")) {
            //passer les machines associées aux exemplaires de la commande en disponible
            for (Exemplaires exemplaire : exemplaires) {
                List<Machine> machines = findAllMachineByExemplaire(exemplaire);
                for (Machine machine : machines) {
                    EtatMachine etatMachine = findLastEtatMachineByMachine(machine);
                    if (etatMachine.getEtat().getDes().equals("en marche")) {
                        //Recuper l'etat actuel et mettre l'heure de fin
                        this.SetFinByEtatMachine(LocalDateTime.now(), etatMachine);
                        //Creer un nouvel etat avec l'heure de début
                        this.saveEtatMachine(new EtatMachine(LocalDateTime.now(), machine, this.findEtatDisponible()));
                    }
                }
            }
        }
        //supprimer les opérations effectuées associées aux exemplaires associés à la commande
        for (Exemplaires exemplaire : exemplaires) {
            deleteAllOperationEffectueesByExemplaire(exemplaire);
        }
        deleteExemplairesByCommande(commande);//supprime les exemplaires associés à la commande
        deleteAllDefinitionByCommande(commande);//supprime les définitions de commande associées à la commande


        commandeRepository.delete(commande);
        Notification.show("La commande a été supprimé");
    }

    public void SetStatutCommande(Commande commande, String statut) {
        commandeRepository.setStatutCommande(commande, statut);
    }


    public void saveCommande(Commande commande) {
        if (commande == null) {
            System.err.println("Commande is null. Are you sure you have connected your form to the application?");
            return;
        }
        if (commande.getDebut() == null) {
            commande.setDebut(LocalDateTime.now());
        }
        if ((commande.getStatut().equals("Terminée") || commande.getStatut().equals("Annulée"))
                && commande.getFin() == null) {
            commande.setFin(LocalDateTime.now());
        } else commande.setFin(null);
        commandeRepository.save(commande);
    }

    public List<Commande> findAllCommandeEnAttente() {
        return commandeRepository.findAllCommandeEnAttente();
    }

    public List<Commande> findAllCommandeEnCours() {
        return commandeRepository.findAllCommandeEnCours();
    }

    public List<Commande> findAllCommandeTerminee(){
        return commandeRepository.findAllCommandeTerminee();
    }
    
    public void setFinCommande(Commande commande, LocalDateTime now) {
        commandeRepository.setFinCommande(commande, now);
    }

    //////////////////////////Definition Commande ////////////////////////////

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

    public List<DefinitionCommande> getDefinitionByProduitAndCommande(Produit produit, Commande commande) {
        return definitionCommandeRepository.findAllDefinitionByProduitAndCommande(produit, commande);
    }

    public DefinitionCommande getDefinitionByProduitAndCommandeUnique(Produit produit, Commande commande) {
        return definitionCommandeRepository.findDefinitionByProduitAndCommande(produit, commande);
    }

    public void deleteAllDefinitionByCommande(Commande commande) {
        definitionCommandeRepository.deleteAllDefinitionByCommande(commande);
    }

    public void deleteAllDefinitionByProduit(Produit produit) {
        definitionCommandeRepository.deleteAllDefinitionByProduit(produit);
    }

    public void deleteDefinitionCommande(DefinitionCommande definitionCommande) {
        definitionCommandeRepository.delete(definitionCommande);
    }


    // type d'opération
    //////////////////////////// TYPE OPERATION ////////////////////////////
    public List<TypeOperation> findAllTypeOperation() {
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

    public void saveMatPremiere(MatPremiere matPremiere) {
        if (matPremiere == null) {
            System.err.println("MatPremiere is null. Are you sure you have connected your form to the application?");
            return;
        }
        matPremiereRepository.save(matPremiere);
    }

    public List<MatPremiere> findAllMatPremiereForProduit(Produit produit) {
        List<MatiereProduit> matiereProduit = this.findMatiereProduitByProduit(produit);
        List<MatPremiere> matPremiere = new ArrayList<>();
        for (MatiereProduit matiereProduits : matiereProduit) {
            matPremiere.add(matiereProduits.getMatPremiere());
        }
        return matPremiere;
    }

    public void deleteMatPremiere(MatPremiere matPremiere) {
        matPremiereRepository.delete(matPremiere);
    }

    
    //////////////////////// MATIEREPRODUIT ////////////////////////////
    /*public void saveMatiereProduit(MatiereProduit matiereProduit) {
        if (matiereProduit == null) {
            System.err.println("MatiereProduit is null. Are you sure you have connected your form to the application?");
            return;
        }
        if(matiereProduitRepository.findByProduitAndMatPremiere(matiereProduit.getProduit(), matiereProduit.getMatPremiere())==null){
            matiereProduitRepository.save(matiereProduit);
        }else{
            matiereProduitRepository.updateQuantite(matiereProduit.getProduit(),matiereProduit.getMatPremiere(),matiereProduit.getQuantite());
        }
    }*/
    public void saveAllMatiereProduit(List<MatiereProduit> matiereProduitsList) {
        if (matiereProduitsList == null) {
            System.err.println("MatiereProduit is null. Are you sure you have connected your form to the application?");
            return;
        }
        List<MatiereProduit> matiereProduitsList2 = new ArrayList<>();
        matiereProduitsList2 = matiereProduitRepository.findByProduit(matiereProduitsList.get(0).getProduit().getId());
        for(MatiereProduit matiereProduit : matiereProduitsList2){
            matiereProduitRepository.delete(matiereProduit);
        }
        matiereProduitRepository.saveAll(matiereProduitsList);
    }
    public double getQuantiteForProduit(Produit produit, MatPremiere matPremiere){
         MatiereProduit mp = matiereProduitRepository.findByProduitAndMatPremiere(produit, matPremiere);
            if(mp!=null) {
                return mp.getQuantite();
            }else{
                return 0;
            }
    }

    public List<MatiereProduit> findMatiereProduitByProduit(Produit produit) {
        if (produit != null) {
            return matiereProduitRepository.findByProduit(produit.getId());
        } else return null;
    }
    
    public List<MatiereProduit> findMatProByProduitAndMatiere(Produit produit, MatPremiere matPre){
        return matiereProduitRepository.findByProduitAndMatiere(produit, matPre);
    }

    //////////////////////// OPERATION ////////////////////////////
    public List<Operation> findAllOperation() {
        return operationRepository.findAll();
    }

    //méthode pour récupérer les opérations d'un produit
    public List<Operation> findOperationByProduit(Produit produit) {
        if (produit != null) {
            return operationRepository.findByProduitId(produit.getId());
        } else return null;
    }
    
    public Operation findOneOperationByProduit(Produit produit) {
        if (produit != null) {
            return operationRepository.findOneByProduit(produit);
        } else return null;
    }
    
    public int CountOperationByProduit (Produit produit){
        return operationRepository.CountOperationByProduit(produit);
    }

    public void deleteAllOperationForProduit(Produit produit) {
        List<Operation> operations = operationRepository.findByProduitId(produit.getId());
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

    //////////////////////// OPERATION EFFECTUEE ////////////////////////////
    public void saveOpperation_Effectuee(Operation_Effectuee ope_effect) {
        if (ope_effect == null) {
            System.err.println("Machine is null. Are you sure you have connected your form to the application?");
            return;
        }
        operation_EffectueeRepository.save(ope_effect);
    }

    public void deleteOperationEffectuee(Operation_Effectuee operation_effectuee) {
        operation_EffectueeRepository.delete(operation_effectuee);
    }

    public boolean OperationEffectueeExiste(Exemplaires exemplaire, Operation operation) {
        return !operation_EffectueeRepository.OperationEffectueeExiste(exemplaire, operation).isEmpty();
    }

    public List<Operation_Effectuee> findAllOperationEffectueeByExemplaire(Exemplaires exemplaire) {
        return operation_EffectueeRepository.findByExemplaire(exemplaire);
    }

    public void SetFinOperationEffectuee(LocalDateTime time, Operation_Effectuee opf) {
        operation_EffectueeRepository.SetFinOperationEffectuee(time, opf);
    }

    public void deleteAllOperationEffectueesByExemplaire(Exemplaires exemplaire) {
        for (Operation_Effectuee operation_effectuee : findAllOperationEffectueeByExemplaire(exemplaire)) {
            operation_EffectueeRepository.delete(operation_effectuee);
        }
    }

    //////////////////////// Exemplaire ////////////////////////////
    public void saveExemplaire(Exemplaires exemplaire) {
        if (exemplaire == null) {
            System.err.println("Exemplaire is null. Are you sure you have connected your form to the application?");
            return;
        }
        exemplairesRepository.save(exemplaire);
    }

    public void saveAllExemplaire(List<Exemplaires> exemplaires) {
        if (exemplaires == null) {
            System.err.println("Exemplaire is null. Are you sure you have connected your form to the application?");
            return;
        }
        exemplairesRepository.saveAll(exemplaires);
    }

    public int countExemplairesByCommandeAndProduit(Commande commande, Produit produit) {
        return exemplairesRepository.countExemplairesByCommandeAndProduit(commande, produit);
    }

    public List<Exemplaires> findAllProdEnCours() {
        List<Exemplaires> exemplaires = exemplairesRepository.findAll();
        List<Exemplaires> exemplairesEnCours = new ArrayList<>();
        
            for (Exemplaires exemplaire : exemplaires) {
                List<Operation_Effectuee> tempo = operation_EffectueeRepository.findByExemplaire(exemplaire);
                if (!tempo.isEmpty()){
                    if (tempo.get(0).getFin() == null) {
                        exemplairesEnCours.add(exemplaire);
                    }
                }
        }
        return exemplairesEnCours;
    }

    public void deleteExemplairesByCommande(Commande commande) {
        exemplairesRepository.deleteAllExemplaireByCommande(commande);//supprime les exemplaires associés à la commande
    }

    public void deleteAllExemplaireEnAttenteByProduit(Produit produit) {
        exemplairesRepository.deleteAllExemplaireEnAttenteByProduit(produit);//supprime les exemplaires associés au produit dont etape = 0
    }

    public List<Exemplaires> findAllProdFini() {
        //récupère les exemplaires dont l'étape est supérieure au nombre d'opérations associé au produit
        List<Exemplaires> exemplaires = exemplairesRepository.findAll();
        List<Exemplaires> exemplairesFini = new ArrayList<>();
        for (Exemplaires exemplaire : exemplaires) {
            if (exemplaire.getEtape() > operationRepository.CountOperationByProduit(exemplaire.getProduit())) {
                exemplairesFini.add(exemplaire);
            }
        }
        return exemplairesFini;
    }

    public List<Exemplaires> findAllProdFiniByProduitAndCommande(Produit produit, Commande commande) {
        //récupère les exemplaires dont l'étape est supérieure au nombre d'opérations associé au produit
        // et qui sont associés à un produit et une commande donnée
        int nbOperation = operationRepository.CountOperationByProduit(produit);
        return exemplairesRepository.findAllProdFiniByProduitAndCommande(produit, commande, nbOperation);
    }

    public List<Exemplaires> findAllProdEnCoursByProduitAndCommande(Produit produit, Commande commande) {
        int nbOperation = operationRepository.CountOperationByProduit(produit);
        return exemplairesRepository.findAllProdEnCoursByProduitAndCommande(produit, commande, nbOperation);
    }

    public List<Exemplaires> findAllByCommande(Commande commande) {
        return exemplairesRepository.findByCommande(commande);
    }

    public List<Exemplaires> findAllByCommandeAndProduit(Commande commande, Produit produit) {
        return exemplairesRepository.findByCommandeAndProduit(commande, produit);
    }

    public void deleteNExemplaireByProduitAndCommande(int n, Produit produit, Commande commande) {
        exemplairesRepository.deleteNExemplaireByProduitAndCommande(n, produit.getId(), commande.getId());
    }

    public void ExemplaireFini(int nbOpe, Exemplaires exemplaire) {
        exemplairesRepository.ExemplaireFini(nbOpe, exemplaire);
    }
    
    public Exemplaires findONEByCommandeAndProduit(Commande commande, Produit produit){
        return exemplairesRepository.findONEByCommandeAndProduit(commande, produit);
    }
    
    public int countProdFiniByCommandeAndProduit(Commande commande, Produit produit){
        int nbOpe = operationRepository.CountOperationByProduit(produit);
        return exemplairesRepository.countProdFiniByCommandeAndProduit(commande, produit, nbOpe);
    }
    //////////////////////// POSTE DE TRAVAIL ////////////////////////////
    public List<PosteDeTravail> findAllPosteDeTravail(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return posteDeTravailRepository.findAll();
        } else {
            return posteDeTravailRepository.search(stringFilter);
        }
    }

    public void savePosteDeTravail(PosteDeTravail posteDeTravail) {
        if (posteDeTravail == null) {
            System.err.println("PosteDeTravail is null. Are you sure you have connected your form to the application?");
            return;
        }
        posteDeTravailRepository.save(posteDeTravail);
    }

    public void deletePosteDeTravail(PosteDeTravail posteDeTravail) {
        posteDeTravailRepository.delete(posteDeTravail);
    }

    //////////////////////// HABILITATION ////////////////////////////
    public List<Operateur> findAllOperateursHabilitesByPosteDeTravail(PosteDeTravail posteDeTravail) {
        if (posteDeTravail == null) {
            return null;
        }
        return habilitationRepository.findOperateursByPosteDeTravail(posteDeTravail);
    }

    public List<Habilitation> findAllHabilitationByPosteDeTravail(PosteDeTravail posteDeTravail) {
        return habilitationRepository.findAllHabilitationByPosteDeTravail(posteDeTravail);
    }

    public void deleteHabilitation(Habilitation habilitation) {
        habilitationRepository.delete(habilitation);
    }

    public void saveHabilitation(Habilitation habilitation) {
        if (habilitation == null) {
            System.err.println("Habilitation is null. Are you sure you have connected your form to the application?");
            return;
        }
        habilitationRepository.save(habilitation);
    }

    public Double getChiffreAffaireAnnuel() {
        List<Commande> commandes = commandeRepository.findAllCommandeTerminee();
        Double chiffreAffaire = 0.0;
        if (commandes.isEmpty()){
            for (Commande commande : commandes) {
                if(commande.getFin().getYear() == LocalDateTime.now().getYear()){
                    chiffreAffaire += commande.getCoutTotal(this);
                }
            }
        }
        return chiffreAffaire;
    }
}
