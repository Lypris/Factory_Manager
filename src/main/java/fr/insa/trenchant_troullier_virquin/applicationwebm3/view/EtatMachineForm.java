package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.time.LocalDateTime;
import java.util.List;

public class EtatMachineForm extends FormLayout {
    ComboBox<Machine> machineComboBox = new ComboBox<>("Machines");
    ComboBox<EtatPossibleMachine> etats = new ComboBox<>("Etat");
    DateTimePicker debut = new DateTimePicker("Début");
    DateTimePicker fin = new DateTimePicker("Fin");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    BeanValidationBinder<EtatMachine> binder = new BeanValidationBinder<>(EtatMachine.class);

    public EtatMachineForm(List<Machine> Machines, List<EtatPossibleMachine> etatPossibleMachines) {
        binder.bindInstanceFields(this);
        addClassName("etat-machine-form");
        machineComboBox.setItems(Machines);
        machineComboBox.setItemLabelGenerator(Machine -> Machine.getDes() + " " + Machine.getRef());
        etats.setItems(etatPossibleMachines);
        etats.setItemLabelGenerator(EtatPossibleMachine::getDes);

        add(machineComboBox,
                debut,
                fin,
                etats,
                createButtonsLayout());
    }
    public void setEtatMachine(EtatMachine etatMachine) {
        binder.setBean(etatMachine);
        if (etatMachine != null){
            Machine machine = etatMachine.getMachine();
            if (machine != null) {
                machineComboBox.setValue(machine);
            }
            EtatPossibleMachine etat = etatMachine.getEtat();
            if (etat != null) {
                etats.setValue(etat);
            }
        }

    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }


    // Events
    public static abstract class EtatMachineFormEvent extends ComponentEvent<EtatMachineForm> {
        private EtatMachine EtatMachine;

        protected EtatMachineFormEvent(EtatMachineForm source, EtatMachine EtatMachine) {
            super(source, false);
            this.EtatMachine = EtatMachine;
        }

        public EtatMachine getEtatMachine() {
            return EtatMachine;
        }
    }

    public static class SaveEvent extends EtatMachineFormEvent {
        SaveEvent(EtatMachineForm source, EtatMachine EtatMachine) {
            super(source, EtatMachine);
        }
    }

    public static class DeleteEvent extends EtatMachineFormEvent {
        DeleteEvent(EtatMachineForm source, EtatMachine EtatMachine) {
            super(source, EtatMachine);
        }

    }

    public static class CloseEvent extends EtatMachineFormEvent {
        CloseEvent(EtatMachineForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
    public void updateEtatsPossibles(List<EtatPossibleMachine> etatsPossibles) {
        etats.setItems(etatsPossibles);
    }
    private void validateAndSave() {
        if (binder.isValid()) {
            EtatMachine etatMachine = binder.getBean();

            Machine selectedMachine = machineComboBox.getValue();
            EtatPossibleMachine selectedEtatPossibleMachine = etats.getValue();
            LocalDateTime debutValue = debut.getValue();
            LocalDateTime finValue = fin.getValue();

            if (selectedMachine == null) {
                Notification.show("Veuillez sélectionner une machine", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (selectedEtatPossibleMachine == null) {
                Notification.show("Veuillez sélectionner un état à appliquer", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (debutValue == null || finValue == null) {
                Notification.show("Veuillez saisir une date de début et une date de fin", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (debutValue.isAfter(finValue)) {
                Notification.show("Veuillez saisir une date de début antérieure à la date de fin", 3000, Notification.Position.MIDDLE);
                return;
            }

            // TODO : Vérifier si la machine a déjà un état pour la même période



            etatMachine.setMachine(selectedMachine);
            etatMachine.setEtat(selectedEtatPossibleMachine);
            etatMachine.setDebut(debutValue);
            etatMachine.setFin(finValue);

            fireEvent(new SaveEvent(this, etatMachine));
        }
    }

}
