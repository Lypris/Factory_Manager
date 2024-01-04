package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Machine;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.TypeOperation;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.util.List;

public class TypeOperationForm extends FormLayout {
    TextField des = new TextField("Description");
    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    CrmService service;
    BeanValidationBinder<TypeOperation> binder = new BeanValidationBinder<>(TypeOperation.class);

    public TypeOperationForm( CrmService service) {
        this.service = service;
        binder.bindInstanceFields(this);
        addClassName("TypeOperation-form");
        des.setValueChangeMode(ValueChangeMode.EAGER);
        des.addValueChangeListener(e ->
                save.setEnabled(!e.getValue().isEmpty()));
        add(des,
                createButtonsLayout());
    }

    public void setTypeOperation(TypeOperation TypeOperation) {
        binder.setBean(TypeOperation);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> {
            List<Machine> machines = service.findAllMachines(null);
            String usageContext = "";
            for (Machine machine : machines) {
                if (machine.getTypeOperation().equals(binder.getBean())) {
                    usageContext = "machine";
                    break; // Sortie de la boucle dès qu'un cas d'utilisation est trouvé
                }
            }

            if (usageContext.isEmpty()) {
                List<Operation> operations = service.findAllOperation();
                for (Operation operation : operations) {
                    if (operation.getTypeOperation().equals(binder.getBean())) {
                        usageContext = "produit";
                        break; // Sortie de la boucle dès qu'un cas d'utilisation est trouvé
                    }
                }
            }

            if (usageContext.isEmpty()) {
                if (binder.getBean().getId() != null) {
                    fireEvent(new TypeOperationForm.DeleteEvent(this, binder.getBean()));
                } else {
                    Notification.show("Impossible de supprimer un type d'opération qui n'existe pas");
                }
            } else {
                String message = "Impossible de supprimer un type d'opération utilisé par une " +
                        (usageContext.equals("machine") ? "machine" : "dans le plan de fabrication d'un produit");
                Notification notification = new Notification(message, 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });
        close.addClickListener(event -> fireEvent(new TypeOperationForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public Registration addDeleteListener(ComponentEventListener<TypeOperationForm.DeleteEvent> listener) {
        return addListener(TypeOperationForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<TypeOperationForm.SaveEvent> listener) {
        return addListener(TypeOperationForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<TypeOperationForm.CloseEvent> listener) {
        return addListener(TypeOperationForm.CloseEvent.class, listener);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new TypeOperationForm.SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class TypeOperationFormEvent extends ComponentEvent<TypeOperationForm> {
        private final TypeOperation TypeOperation;

        protected TypeOperationFormEvent(TypeOperationForm source, TypeOperation TypeOperation) {
            super(source, false);
            this.TypeOperation = TypeOperation;
        }

        public TypeOperation getTypeOperation() {
            return TypeOperation;
        }
    }

    public static class SaveEvent extends TypeOperationForm.TypeOperationFormEvent {
        SaveEvent(TypeOperationForm source, TypeOperation TypeOperation) {
            super(source, TypeOperation);
        }
    }

    public static class DeleteEvent extends TypeOperationForm.TypeOperationFormEvent {
        DeleteEvent(TypeOperationForm source, TypeOperation TypeOperation) {
            super(source, TypeOperation);
        }

    }

    public static class CloseEvent extends TypeOperationForm.TypeOperationFormEvent {
        CloseEvent(TypeOperationForm source) {
            super(source, null);
        }
    }
}
