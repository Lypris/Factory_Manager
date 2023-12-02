package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public class UploadHelper extends Div {

    public UploadHelper() {
        MemoryBuffer buffer = new MemoryBuffer();
        // tag::snippet[]
        H4 title = new H4("Téléverser une image");
        Paragraph hint = new Paragraph(
                "La taille de l'image ne doit pas dépasser 1 Mo. Les formats d'image pris en charge sont JPEG et PNG.");

        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/png", "image/jpeg");

        UploadExamplesI18N i18n = new UploadExamplesI18N();
        i18n.getAddFiles().setOne("Téléverser une image...");
        i18n.getDropFiles().setOne("Glisser l'image ici...");
        i18n.getError().setIncorrectFileType(
                "Le format de fichier n'est pas pris en charge. Veuillez sélectionner une image JPEG ou PNG.");
        upload.setI18n(i18n);

        add(title, hint, upload);
        // end::snippet[]

        title.getStyle().set("margin-top", "0");
        hint.getStyle().set("color", "var(--lumo-secondary-text-color)");

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

    }

}

