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
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;

public class OperateurForm extends FormLayout {
    TextField nom = new TextField("Nom");
    TextField prenom = new TextField("Prénom");
    EmailField mail = new EmailField("Adresse E-Mail");
    TextField tel = new TextField("Numéro de téléphone");

    Button save = new Button("Enregistrer");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    BeanValidationBinder<Operateur> binder = new BeanValidationBinder<>(Operateur.class);

    public OperateurForm() {
        binder.bindInstanceFields(this);
        addClassName("operateur-form");

        add(nom,
                prenom,
                mail,
                tel,
                createButtonsLayout());
    }

    public void setOperateur(Operateur operateur) {
        binder.setBean(operateur);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new OperateurForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new OperateurForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    public Registration addDeleteListener(ComponentEventListener<OperateurForm.DeleteEvent> listener) {
        return addListener(OperateurForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<OperateurForm.SaveEvent> listener) {
        return addListener(OperateurForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<OperateurForm.CloseEvent> listener) {
        return addListener(OperateurForm.CloseEvent.class, listener);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new OperateurForm.SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class OperateurFormEvent extends ComponentEvent<OperateurForm> {
        private final Operateur operateur;

        protected OperateurFormEvent(OperateurForm source, Operateur operateur) {
            super(source, false);
            this.operateur = operateur;
        }

        public Operateur getOperateur() {
            return operateur;
        }
    }

    public static class SaveEvent extends OperateurForm.OperateurFormEvent {
        SaveEvent(OperateurForm source, Operateur Operateur) {
            super(source, Operateur);
        }
    }

    public static class DeleteEvent extends OperateurForm.OperateurFormEvent {
        DeleteEvent(OperateurForm source, Operateur Operateur) {
            super(source, Operateur);
        }

    }

    public static class CloseEvent extends OperateurForm.OperateurFormEvent {
        CloseEvent(OperateurForm source) {
            super(source, null);
        }
    }
}
