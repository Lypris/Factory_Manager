package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatPossibleMachine;

public class EtatPossibleMachineDialog extends Dialog {

    TextField des = new TextField("Description");

    BeanValidationBinder<EtatPossibleMachine> binder = new BeanValidationBinder<>(EtatPossibleMachine.class);

    public EtatPossibleMachineDialog() {

        binder.bindInstanceFields(this);
        addClassName("etatpossiblemachine-form");
        setHeaderTitle("Ajouter un état possible");
        VerticalLayout dialogLayout = new VerticalLayout(des);
        add(dialogLayout);
        Button saveButton = createSaveButton();
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickShortcut(Key.ENTER);
        saveButton.getStyle().set("margin-right", "auto");
        getFooter().add(saveButton);

        Button cancelButton = new Button("Cancel", (e) -> close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickShortcut(Key.ESCAPE);
        getFooter().add(cancelButton);
    }
    private Button createSaveButton() {
        return new Button("Save", event -> {
            validateAndSave();
            close();
        });
    }

    public void openDialog() {
        open();
    }


    // Events
    public static abstract class EtatPossibleMachineDialogEvent extends ComponentEvent<EtatPossibleMachineDialog> {
        private EtatPossibleMachine etat;

        protected EtatPossibleMachineDialogEvent(EtatPossibleMachineDialog source, EtatPossibleMachine etat) {
            super(source, false);
            this.etat = etat;
        }

        public EtatPossibleMachine getEtatPossibleMachine() {
            return etat;
        }
    }

    public static class SaveEvent extends EtatPossibleMachineDialog.EtatPossibleMachineDialogEvent {
        SaveEvent(EtatPossibleMachineDialog source, EtatPossibleMachine etat) {
            super(source, etat);
        }
    }
    public static class CloseEvent extends EtatPossibleMachineDialog.EtatPossibleMachineDialogEvent {
        CloseEvent(EtatPossibleMachineDialog source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<EtatPossibleMachineDialog.SaveEvent> listener) {
        return addListener(EtatPossibleMachineDialog.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<EtatPossibleMachineDialog.CloseEvent> listener) {
        return addListener(EtatPossibleMachineDialog.CloseEvent.class, listener);
    }
    private void validateAndSave() {
        if (binder.isValid()) {
            EtatPossibleMachine etatPossibleMachine = new EtatPossibleMachine(); // Initialisez l'objet si nécessaire
            binder.writeBeanIfValid(etatPossibleMachine);
            fireEvent(new EtatPossibleMachineDialog.SaveEvent(this, etatPossibleMachine));
        } else {
            // Afficher les erreurs de validation
            Notification.show("Veuillez remplir tous les champs correctement", 3000, Notification.Position.MIDDLE);
        }
    }

}
