package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import fr.insa.trenchant_troullier_virquin.applicationwebm3.data.entity.Operateur;

import java.util.function.Consumer;

@SpringComponent
public class ConfirmationSuppressionView extends VerticalLayout {

    private Dialog confirmationDialog;
    private Consumer<Boolean> confirmationCallback; // Callback to handle confirmation result

    private ConfirmationSuppressionView() {
        createConfirmationDialog();
    }

    private void createConfirmationDialog() {
        confirmationDialog = new Dialog();
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);
        Label confirmationLabel = new Label("La suppression de l'opérateur nécessite de supprimer tous ses statuts. Voulez-vous continuer ?");
        Button confirmButton = new Button("Confirmer");
        Button cancelButton = new Button("Annuler");

        confirmButton.addClickListener(event -> {
            confirmationDialog.close();
        });

        cancelButton.addClickListener(event -> confirmationDialog.close());

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        confirmationDialog.add(new VerticalLayout(confirmationLabel, buttonsLayout));
        add(confirmationDialog);
    }

    public void openConfirmationDialog(Consumer<Boolean> callback) {
        this.confirmationCallback = callback; // Set the callback function
        confirmationDialog.open();
    }

    public void closeConfirmationDialog() {
        confirmationDialog.close();
    }

}
