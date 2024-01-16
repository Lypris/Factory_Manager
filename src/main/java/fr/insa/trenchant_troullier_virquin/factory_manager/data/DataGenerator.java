package fr.insa.trenchant_troullier_virquin.factory_manager.data;

import fr.insa.trenchant_troullier_virquin.factory_manager.data.entity.*;
import fr.insa.trenchant_troullier_virquin.factory_manager.data.service.CrmService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private CrmService service;
    private Random random = new Random();

    public DataGenerator(CrmService service) {
        this.service = service;
    }

    public void generateDataFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Operateur operateur = parseLineToOperateur(line);
                service.saveOperateur(operateur);
                assignRandomStatutToOperateurs(List.of(operateur), service.findAllStatuses());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void generateTypeOperationFromFile(String filePath){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null){
                TypeOperation typeOperation = parseLineToTypeOperation(line);
                service.saveTypeOperation(typeOperation);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void generateMachineFromFile(String filePath){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = br.readLine()) != null){
                Machine machine = parseLineToMachine(line);
                assignTypeOperatioToMachine(List.of(machine), service.findAllTypeOperation());
                service.saveMachine(machine);
                // Créer un nouvel état avec la date de début actuelle et l'état sélectionné
                EtatMachine newEtatMachine = new EtatMachine();
                newEtatMachine.setDebut(LocalDateTime.now());
                newEtatMachine.setMachine(machine);
                List<EtatPossibleMachine> etatPossibleMachines = service.findAllEtatPossibleMachines();
                newEtatMachine.setEtat(etatPossibleMachines.get(random.nextInt(etatPossibleMachines.size())));
                service.saveEtatMachine(newEtatMachine);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Operateur parseLineToOperateur(String line) {
        // Assumons que chaque ligne est formatée comme "nom,prénom,email,phone"
        String[] parts = line.split(",");
        Operateur operateur = new Operateur();
        operateur.setNom(parts[0]);
        operateur.setPrenom(parts[1]);
        operateur.setMail(parts[2]);
        operateur.setTel(parts[3]);
        // Ajouter d'autres champs si nécessaire
        return operateur;
    }
    private TypeOperation parseLineToTypeOperation(String line){
        TypeOperation typeOperation = new TypeOperation();
        typeOperation.setDes(line);
        return typeOperation;
    }
    private Machine parseLineToMachine(String line){
        String[] parts = line.split(",");
        Machine machine = new Machine();
        machine.setRef(parts[0]);
        machine.setDes(parts[1]);
        //puissance est un float
        machine.setPuissance(Float.parseFloat(parts[2]));
        return machine;
    }

    public void assignRandomStatutToOperateurs(List<Operateur> operateurs, List<Statut> statuts) {
        for (Operateur operateur : operateurs) {
            StatutOperateur statutOperateur = new StatutOperateur();
            statutOperateur.setOperateur(operateur);
            statutOperateur.setStatut(statuts.get(random.nextInt(statuts.size())));
            statutOperateur.setDebut(LocalDateTime.now());
            service.saveStatutOperateur(statutOperateur);
        }
    }
    public void assignTypeOperatioToMachine(List<Machine> machines, List<TypeOperation> typeOperations){
        for (Machine machine : machines){
            //On choisit un type d'operation au hasard
            TypeOperation typeOperation = typeOperations.get(random.nextInt(typeOperations.size()));
            //on l'ajoute à la machine
            machine.setTypeOperation(typeOperation);
        }
    }
}
