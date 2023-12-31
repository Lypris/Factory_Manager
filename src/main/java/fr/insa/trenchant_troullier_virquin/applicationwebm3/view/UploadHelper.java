package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;


import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import java.io.IOException;
import java.util.function.Consumer;

public class UploadHelper extends Div {

    private final MemoryBuffer buffer;
    private Consumer<byte[]> imageUploadListener;
    private byte[] currentImageData;
    private boolean imageUploaded = false;

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
            try {
                currentImageData = buffer.getInputStream().readAllBytes();
                if (imageUploadListener != null) {
                    imageUploadListener.accept(currentImageData);
                }
            } catch (IOException e) {
                Notification.show("Erreur lors de la conversion de l'image", 3000,
                        Notification.Position.MIDDLE);
            }
        });

        upload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();

            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
        upload.addDetachListener(event -> {
            if (imageUploadListener != null) {
                imageUploadListener.accept(null);
            }
        });
        // Ajoute un listener JavaScript pour le retrait de fichier
        upload.getElement().executeJs(
                "this.addEventListener('file-remove', (e) => $0.$server.fileRemove(e.detail.file.name));",
                getElement());
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

    public void setImageData(byte[] imageData) {
        this.currentImageData = imageData;
        if (imageUploadListener != null) {
            imageUploadListener.accept(imageData);
        }
    }

    public void setImageUploadListener(Consumer<byte[]> listener) {
        this.imageUploadListener = image -> {
            if (image != null) {
                imageUploaded = true; // Marque comme téléversée
                currentImageData = image;
            } else {
                imageUploaded = false; // Marque comme supprimée
                currentImageData = null;
            }
            listener.accept(image);
        };
    }

    // Méthode pour vérifier si une image a été téléversée
    public boolean isImageUploaded() {
        return imageUploaded;
    }

    @ClientCallable
    public void fileRemove(String fileName) {
        // Traiter l'événement de suppression de fichier ici
        if (imageUploadListener != null) {
            imageUploadListener.accept(null);
            setImageData(null);
        }
    }

    public void resetUpload() {
        currentImageData = null;
        imageUploaded = false;
        // Réinitialiser l'upload lui-même si nécessaire
    }

}

