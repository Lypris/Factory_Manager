package fr.insa.trenchant_troullier_virquin.applicationwebm3.data;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.BDD.Gestion;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.model.Operateur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeDataGenerator {
    //TODO : méthode pour générer des opérateurs
    private static final String[] prenoms = {"Jean", "Marie", "Claire", "Pierre", "Sophie", "Luc", "Emma", "Antoine"};
    private static final String[] noms = {"Dubois", "Lefevre", "Martin", "Bernard", "Thomas", "Robert", "Richard", "Petit"};
    private static final String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "icloud.com"};

    private static String generatePhoneNumber() {
        StringBuilder phoneNumber = new StringBuilder("+33"); // Indicatif France
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            phoneNumber.append(random.nextInt(10)); // Ajouter des chiffres aléatoires
        }
        return phoneNumber.toString();
    }
    // Méthode pour générer une liste d'opérateurs avec des données aléatoires
    public static List<Operateur> generateOperators(int numberOfOperators) {
        List<Operateur> operators = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfOperators; i++) {
            String prenom = prenoms[random.nextInt(prenoms.length)];
            String nom = noms[random.nextInt(noms.length)];
            String email = prenom.toLowerCase() + "." + nom.toLowerCase() + "@" + domains[random.nextInt(domains.length)];
            String telephone = generatePhoneNumber();

            Operateur operator = new Operateur(prenom, nom, email, telephone);
            operators.add(operator);
        }

        return operators;
    }
}
