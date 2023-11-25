package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.EtatPossibleMachine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;

public class EtatPossibleMachineDialog extends Dialog {

    private final TextField descriptionField = new TextField("Description");
    private final Dialog dialog;
    BeanValidationBinder<EtatPossibleMachine> binder = new BeanValidationBinder<>(EtatPossibleMachine.class);

    public EtatPossibleMachineDialog() {

        dialog = new Dialog();
        dialog.setHeaderTitle("Ajouter un état possible");
        VerticalLayout dialogLayout = new VerticalLayout(descriptionField);
        dialog.add(dialogLayout);

        Button saveButton = createSaveButton();
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickShortcut(Key.ENTER);
        saveButton.getStyle().set("margin-right", "auto");
        dialog.getFooter().add(saveButton);

        Button cancelButton = new Button("Cancel", (e) -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickShortcut(Key.ESCAPE);
        dialog.getFooter().add(cancelButton);

        binder.forField(descriptionField)
                .bind(EtatPossibleMachine::getDes, EtatPossibleMachine::setDes);

    }

    private Button createSaveButton() {
        return new Button("Save", event -> {
            String description = descriptionField.getValue();
            // TODO : Ajouter la logique pour sauvegarder la description
            dialog.close();
        });
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void openDialog() {
        dialog.open();
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
        if(binder.isValid()) {
            fireEvent(new EtatPossibleMachineDialog.SaveEvent(this, binder.getBean()));
        }
    }
}
