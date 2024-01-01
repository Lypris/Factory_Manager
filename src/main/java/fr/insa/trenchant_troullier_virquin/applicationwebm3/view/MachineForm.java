package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatPossibleMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.util.List;

public class MachineForm extends FormLayout {
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    TextField puissance = new TextField("Puissance (kW)");
    ComboBox<TypeOperation> typeOperationComboBox = new ComboBox<>("Type d'opération");
    ComboBox<EtatPossibleMachine> etatComboBox = new ComboBox<>("État actuel");


    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    BeanValidationBinder<Machine> binder = new BeanValidationBinder<>(Machine.class);

    CrmService service;

    public MachineForm(List<TypeOperation> typeOperations, List<EtatPossibleMachine> etatsPossibles, CrmService service) {
        this.service = service;
        binder.bindInstanceFields(this);
        addClassName("Machine-form");
        typeOperationComboBox.setItems(typeOperations);
        typeOperationComboBox.setItemLabelGenerator(TypeOperation::getDes);
        etatComboBox.setItems(etatsPossibles);
        etatComboBox.setItemLabelGenerator(EtatPossibleMachine::getDes);

        add(ref, des, puissance, typeOperationComboBox, etatComboBox,
                createButtonsLayout());
    }

    public void setMachine(Machine machine) {
        binder.setBean(machine);
        if (machine != null) {
            delete.setEnabled(machine.getId() != null);
            // Récupérer le type d'opération de la machine et le sélectionner dans le ComboBox
            TypeOperation typeOperation = machine.getTypeOperation();
            if (typeOperation != null) {
                this.typeOperationComboBox.setValue(typeOperation);
            }
            if (machine.getId() != null) {
                // Récupérer l'état actuel de la machine
                EtatMachine currentEtatMachine = service.findLastEtatMachineByMachine(machine);
                // Déterminer les états possibles suivants en fonction de l'état actuel
                if (currentEtatMachine != null) {
                    EtatPossibleMachine etatActuel = currentEtatMachine.getEtat();
                    if ("en marche".equals(etatActuel.getDes())) { // Si la machine est en marche, on peut la mettre en panne
                        etatComboBox.setItems(service.findEtatPossibleByDes("en panne"));
                        delete.setEnabled(false); // On ne peut pas supprimer une machine en marche
                    } else if ("disponible".equals(etatActuel.getDes())) {
                        etatComboBox.setItems(service.findEtatPossibleByDes("en panne")); // On peut mettre la machine en panne
                    } else if ("en panne".equals(etatActuel.getDes())) {
                        // Si la machine est en panne, on peut la remettre en marche si elle était en marche avant
                        //on récupère l'état précédent
                        EtatMachine previousEtatMachine = service.findMostRecentEtatMachineByMachine(machine);
                        if (previousEtatMachine.getEtat().getDes().equals("en marche")) { // Si la machine était en marche avant
                            etatComboBox.setItems(service.findEtatPossibleByDes("en marche"));
                            delete.setEnabled(false); // On ne peut pas supprimer une machine en marche
                        } else {
                            etatComboBox.setItems(service.findEtatPossibleByDes("disponible"));
                        }
                    } else {
                        etatComboBox.setItems(service.findAllEtatPossibleMachines());
                    }
                } else {
                    etatComboBox.setItems(service.findAllEtatPossibleMachines());
                }
            } else {
                etatComboBox.setItems(service.findAllEtatPossibleMachines());
            }
            etatComboBox.setItemLabelGenerator(EtatPossibleMachine::getDes);
        }
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new MachineForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new MachineForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(MachineForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<MachineForm.SaveEvent> listener) {
        return addListener(MachineForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<MachineForm.CloseEvent> listener) {
        return addListener(MachineForm.CloseEvent.class, listener);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            Machine machine = binder.getBean();
            boolean isNewMachine = machine.getId() == null;
            TypeOperation selectedTypeOperation = typeOperationComboBox.getValue();
            if (selectedTypeOperation != null) {
                machine.setTypeOperation(selectedTypeOperation);
            } else {
                Notification.show("Veuillez sélectionner un type d'opération");
                return;
            }
            fireEvent(new MachineForm.SaveEvent(this, binder.getBean()));
            if (isNewMachine) {
                // Création d'un nouvel état machine
                this.service.saveEtatMachine(new EtatMachine(LocalDateTime.now(), machine, service.findEtatDisponible()));
            } else {
                // Mise à jour de l'état de la machine existante
                EtatPossibleMachine selectedEtat = etatComboBox.getValue();
                if (selectedEtat != null) {
                    // Trouver et mettre à jour l'état actuel ou créer un nouvel état si nécessaire
                    EtatMachine currentEtatMachine = service.findLastEtatMachineByMachine(machine);
                    if (currentEtatMachine != null) {
                        currentEtatMachine.setFin(LocalDateTime.now()); // Met fin à l'état actuel
                        service.saveEtatMachine(currentEtatMachine);
                    }

                    // Créer un nouvel état avec la date de début actuelle et l'état sélectionné
                    EtatMachine newEtatMachine = new EtatMachine();
                    newEtatMachine.setDebut(LocalDateTime.now());
                    newEtatMachine.setMachine(machine);
                    newEtatMachine.setEtat(selectedEtat);
                    service.saveEtatMachine(newEtatMachine);

                }
            }
        }
    }

    // Events
    public static abstract class MachineFormEvent extends ComponentEvent<MachineForm> {
        private final Machine Machine;

        protected MachineFormEvent(MachineForm source, Machine Machine) {
            super(source, false);
            this.Machine = Machine;
        }

        public Machine getMachine() {
            return Machine;
        }
    }

    public static class SaveEvent extends MachineForm.MachineFormEvent {
        SaveEvent(MachineForm source, Machine Machine) {
            super(source, Machine);
        }
    }

    public static class DeleteEvent extends MachineForm.MachineFormEvent {
        DeleteEvent(MachineForm source, Machine Machine) {
            super(source, Machine);
        }

    }

    public static class CloseEvent extends MachineForm.MachineFormEvent {
        CloseEvent(MachineForm source) {
            super(source, null);
        }
    }
}
