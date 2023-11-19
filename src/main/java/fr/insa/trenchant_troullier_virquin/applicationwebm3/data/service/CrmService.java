package fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service;

import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Contact;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Statut;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.StatutOperateur;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository.ContactRepository;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository.OperateurRepository;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository.StatusRepository;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.repository.StatutOperateurRepository;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.view.ConfirmationSuppressionView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final ContactRepository contactRepository;
    private final StatusRepository statusRepository;
    private final OperateurRepository operateurRepository;
    private final StatutOperateurRepository statutOperateurRepository;
    private final ConfirmationSuppressionView confirmationView;

    public CrmService(ContactRepository contactRepository,
                      StatusRepository statusRepository,
                      OperateurRepository operateurRepository,
                      StatutOperateurRepository statutOperateurRepository,
                      ConfirmationSuppressionView confirmationView) {
        this.contactRepository = contactRepository;
        this.statusRepository = statusRepository;
        this.operateurRepository = operateurRepository;
        this.statutOperateurRepository = statutOperateurRepository;
        this.confirmationView = confirmationView;
    }

    //////////////////////////// CONTACT ////////////////////////////
    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        contactRepository.save(contact);
    }

    //////////////////////////// STATUT DE CONTACT ////////////////////////////
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
            // Open confirmation dialog
            confirmationView.openConfirmationDialog(result -> {
                if (result) {
                    // User confirmed the deletion, proceed with deleting associated statutOperateurs and the operateur
                    deleteAssociatedStatuts(statutOperateurs);
                    operateurRepository.delete(operateur);
                } else {
                    // User canceled the deletion, do nothing
                }
            });
        }
    }

    private void deleteAssociatedStatuts(List<StatutOperateur> statutOperateurs) {
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

}