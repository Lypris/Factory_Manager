package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.*;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.service.CrmService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PosteDeTravailForm extends FormLayout {
    //cette classe doit permettre de créer un nouveau poste de travail
    //elle doit permettre de choisir les machines et les opérateurs
    //elle doit permettre de saisir la référence et la description
    //elle doit permettre de valider ou d'annuler
    //elle doit permettre de modifier un poste de travail existant
    TextField ref = new TextField("Référence");
    TextField des = new TextField("Description");
    private final ComboBox<Operateur> operateurComboBox = new ComboBox<>("Opérateurs abilités");
    private final ComboBox<Machine> machineComboBox = new ComboBox<>("Machines associées");
    private final HorizontalLayout selectedOperateursLayout = new HorizontalLayout();
    private final HorizontalLayout selectedMachinesLayout = new HorizontalLayout();
    Button save = new Button("Valider");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");
    private Set<Operateur> selectedOperateurs = new HashSet<>();
    private Set<Machine> selectedMachines = new HashSet<>();
    BeanValidationBinder<PosteDeTravail> binder = new BeanValidationBinder<>(PosteDeTravail.class);
    CrmService service;
    public PosteDeTravailForm(CrmService service) {
        binder.bindInstanceFields(this);
        this.service = service;
        updateOperateurComboBox();
        updateMachineComboBox();
        configureComboBoxes();
        configureBadgesLayout();
        add(ref, des, operateurComboBox, selectedOperateursLayout, machineComboBox, selectedMachinesLayout,createButtonsLayout());
    }

    private void configureComboBoxes() {
        operateurComboBox.setItemLabelGenerator(Operateur::toString);
        operateurComboBox.setAllowCustomValue(false);
        operateurComboBox.setAllowedCharPattern("[]");
        //Si on crée un nouveau poste de travail, on peut choisir tous les opérateurs
        //Si on modifie un poste de travail, on ne peut choisir que les opérateurs habilités
        if (binder.getBean() == null){
            operateurComboBox.setItems(service.findAllOperateurs(null));
        }
        else{
            operateurComboBox.setItems(service.findAllOperateursHabilitesByPosteDeTravail(binder.getBean()));
        }
        operateurComboBox.addValueChangeListener(e -> {
            Operateur selectedOperateur = e.getValue();
            if (selectedOperateur != null) {
                // Ajoute l'opérateur sélectionné à la liste des badges
                Span badge = createOperateurBadge(selectedOperateur);
                selectedOperateursLayout.add(badge);
                // Retire l'opérateur de la liste de sélection
                selectedOperateurs.add(selectedOperateur);
                updateOperateurComboBox();
                operateurComboBox.clear();
            }
        });
        machineComboBox.setItemLabelGenerator(Machine::toString);
        machineComboBox.setAllowCustomValue(false);
        machineComboBox.setAllowedCharPattern("[]");
        machineComboBox.setItems(service.findAllMachines(null)); //TODO: changer la méthode
        machineComboBox.addValueChangeListener(e -> {
            Machine selectedMachine = e.getValue();
            if (selectedMachine != null) {
                // Ajoute la machine sélectionnée à la liste des badges
                Span badge = createMachineBadge(selectedMachine);
                selectedMachinesLayout.add(badge);
                // Retire la machine de la liste de sélection
                selectedMachines.add(selectedMachine);
                updateMachineComboBox();
                machineComboBox.clear();
            }
        });
    }

    private void updateOperateurComboBox() {
        // Obtient la liste complète des opérateurs et les filtre par ceux déjà sélectionnés
        List<Operateur> allOperateurs = service.findAllOperateurs(null);
        List<Operateur> availableOperateurs = allOperateurs.stream()
                .filter(operateur -> !selectedOperateurs.contains(operateur))
                .collect(Collectors.toList());
        operateurComboBox.setItems(availableOperateurs);
    }
    private void updateMachineComboBox() {
        List<Machine> allMachines = service.findAllMachines(null);
        List<Machine> availableMachines = allMachines.stream()
                .filter(machine -> !selectedMachines.contains(machine))
                .collect(Collectors.toList());
        machineComboBox.setItems(availableMachines);
    }
    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new PosteDeTravailForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new PosteDeTravailForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private Span createOperateurBadge(Operateur operateur) {
        selectedOperateurs.add(operateur);
        operateurComboBox.setItems(service.findAllOperateurs(null)); // Mettre à jour pour exclure les opérateurs sélectionnés

        Button clearButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        clearButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        clearButton.getElement().setAttribute("aria-label", "Retirer " + operateur.getNom());
        clearButton.addClickListener(event -> {
            // Retire l'opérateur du badge et remet à jour la combobox
            selectedOperateurs.remove(operateur);
            updateOperateurComboBox();
            clearButton.getParent().ifPresent(component -> selectedOperateursLayout.remove(component));
        });

        Span badgeContent = new Span(operateur.getNom());
        Span badge = new Span(badgeContent, clearButton);
        badge.getElement().getThemeList().add("badge contrast pill");
        return badge;
    }
    private void configureBadgesLayout(){
        selectedOperateursLayout.getStyle().set("flex-wrap", "wrap");
        selectedOperateursLayout.setSpacing(true);
        selectedMachinesLayout.getStyle().set("flex-wrap", "wrap");
        selectedMachinesLayout.setSpacing(true);

    }
    private Span createMachineBadge(Machine machine) {
        selectedMachines.add(machine);
        machineComboBox.setItems(service.findAllMachines(null)); // Mettre à jour pour exclure les machines sélectionnées

        Button clearButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        clearButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);
        clearButton.getElement().setAttribute("aria-label", "Retirer " + machine.getRef());
        clearButton.addClickListener(event -> {
            // Retire la machine du badge et remet à jour la combobox
            selectedMachines.remove(machine);
            updateMachineComboBox();
            clearButton.getParent().ifPresent(component -> selectedMachinesLayout.remove(component));
        });

        Span badgeContent = new Span(machine.getRef());
        Span badge = new Span(badgeContent, clearButton);
        badge.getElement().getThemeList().add("badge contrast pill");
        return badge;
    }

    
    public void setPosteDeTravail(PosteDeTravail posteDeTravail) {

        binder.setBean(posteDeTravail);
        if (posteDeTravail != null){
            //TODO: Récupérer les opérateurs du PosteDeTravail et ajouter les badges correspondants
/*
            service.findAllOperateursHabilitesByPosteDeTravail(posteDeTravail).forEach(operateur -> {
                Span badge = createOperateurBadge(operateur);
                selectedOperateursLayout.add(badge);
                selectedOperateurs.add(operateur);  // Ajouter à la liste des opérateurs sélectionnés
            });
*/
            //TODO: Récupérer les machines du PosteDeTravail et ajouter les badges correspondants
        }
    }
    // Events
    public static abstract class PosteDeTravailFormEvent extends ComponentEvent<PosteDeTravailForm> {
        private PosteDeTravail PosteDeTravail;

        protected PosteDeTravailFormEvent(PosteDeTravailForm source, PosteDeTravail PosteDeTravail) {
            super(source, false);
            this.PosteDeTravail = PosteDeTravail;
        }

        public PosteDeTravail getPosteDeTravail() {
            return PosteDeTravail;
        }
    }

    public static class SaveEvent extends PosteDeTravailForm.PosteDeTravailFormEvent {
        SaveEvent(PosteDeTravailForm source, PosteDeTravail PosteDeTravail) {
            super(source, PosteDeTravail);
        }
    }

    public static class DeleteEvent extends PosteDeTravailForm.PosteDeTravailFormEvent {
        DeleteEvent(PosteDeTravailForm source, PosteDeTravail PosteDeTravail) {
            super(source, PosteDeTravail);
        }

    }

    public static class CloseEvent extends PosteDeTravailForm.PosteDeTravailFormEvent {
        CloseEvent(PosteDeTravailForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<PosteDeTravailForm.DeleteEvent> listener) {
        return addListener(PosteDeTravailForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<PosteDeTravailForm.SaveEvent> listener) {
        return addListener(PosteDeTravailForm.SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<PosteDeTravailForm.CloseEvent> listener) {
        return addListener(PosteDeTravailForm.CloseEvent.class, listener);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            PosteDeTravail posteDeTravail = binder.getBean();
            service.savePosteDeTravail(posteDeTravail);
            //on parcourt les opérateurs sélectionnés et on les ajoute au poste de travail en leur créant une habilitation
            selectedOperateurs.forEach(operateur -> {
                Habilitation habilitation = new Habilitation();
                habilitation.setOperateur(operateur);
                habilitation.setPosteDeTravail(posteDeTravail); // posteDeTravail doit déjà être sauvegardé
                service.saveHabilitation(habilitation); // Persiste l'habilitation
            });
            //on parcourt les machines sélectionnées et on leur ajoute le poste de travail
            selectedMachines.forEach(machine -> {
                machine.setPosteDeTravail(posteDeTravail);
                service.saveMachine(machine);
            });
            fireEvent(new PosteDeTravailForm.SaveEvent(this, binder.getBean()));
        }
    }
}
