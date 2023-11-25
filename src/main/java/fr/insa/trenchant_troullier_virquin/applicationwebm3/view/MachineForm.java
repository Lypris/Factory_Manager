package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;

public class MachineForm extends FormLayout {
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    TextField puissance = new TextField("Puissance (kW)");

    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    BeanValidationBinder<Machine> binder = new BeanValidationBinder<>(Machine.class);

    public MachineForm() {
        binder.bindInstanceFields(this);
        addClassName("Machine-form");

        add(ref, des, puissance,
                createButtonsLayout());
    }
    public void setMachine(Machine Machine) {
        binder.setBean(Machine);
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


    // Events
    public static abstract class MachineFormEvent extends ComponentEvent<MachineForm> {
        private Machine Machine;

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
        if(binder.isValid()) {
            fireEvent(new MachineForm.SaveEvent(this, binder.getBean()));
        }
    }
}
