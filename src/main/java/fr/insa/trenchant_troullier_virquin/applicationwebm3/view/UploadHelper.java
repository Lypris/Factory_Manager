package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadHelper extends Div {

    private MemoryBuffer buffer;
    public UploadHelper() {
        buffer = new MemoryBuffer();
        H4 title = new H4("Téléverser une image");
        Paragraph hint = new Paragraph(
                "La taille de l'image ne doit pas dépasser 1 MB. Les formats d'image pris en charge sont JPEG et PNG.");

        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/png", "image/jpeg");
        int maxFileSizeInBytes = 1024 * 1024; // 1MB
        upload.setMaxFileSize(maxFileSizeInBytes);

        UploadExamplesI18N i18n = new UploadExamplesI18N();
        i18n.getAddFiles().setOne("Téléverser une image...");
        i18n.getDropFiles().setOne("Glisser l'image ici...");
        i18n.getError().setIncorrectFileType(
                "Le format de fichier n'est pas pris en charge. Veuillez sélectionner une image JPEG ou PNG.");
        upload.setI18n(i18n);

        add(title, hint, upload);

        title.getStyle().set("margin-top", "0");
        hint.getStyle().set("color", "var(--lumo-secondary-text-color)");

        upload.addSucceededListener(event -> {
            // L'image a été téléchargée avec succès
            InputStream inputStream = buffer.getInputStream();
            // Convertir InputStream image redimensionnée en tableau d'octets
            try {
                byte[] imageData = inputStream.readAllBytes();
                //TODO : Afficher l'image redimensionnée

            } catch (IOException e) {
                Notification notification = Notification.show("Erreur lors de la conversion de l'image", 3000,
                        Notification.Position.MIDDLE);
            }

        });

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

    }
    public MemoryBuffer getBuffer() {
        return buffer;
    }
    public byte[] getImageData() {
        try {
            return buffer.getInputStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            Notification notification = Notification.show("Erreur lors du téléversement de l'image", 3000,
                    Notification.Position.MIDDLE);
            return null;
        }
    }

}

