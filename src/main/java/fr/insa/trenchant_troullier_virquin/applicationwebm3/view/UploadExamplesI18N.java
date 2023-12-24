package fr.insa.trenchant_troullier_virquin.applicationwebm3.view;


import com.vaadin.flow.component.upload.UploadI18N;

import java.util.Arrays;

/**
 * Provides a default I18N configuration for the Upload examples
 *
 * At the moment the Upload component requires a fully configured I18N instance,
 * even for use-cases where you only want to change individual texts.
 *
 * This I18N configuration is an adaption of the web components I18N defaults
 * and can be used as a basis for customizing individual texts.
 */
public class UploadExamplesI18N extends UploadI18N {
    public UploadExamplesI18N() {
        setDropFiles(new DropFiles().setOne("Glisser le fichier ici")
                .setMany("Glisser les fichiers ici"));
        setAddFiles(new AddFiles().setOne("Téléverser le fichier...")
                .setMany("Téléverser les fichiers..."));
        setError(new Error().setTooManyFiles("Trop de fichiers.")
                .setFileIsTooBig("Le fichier est trop volumineux.")
                .setIncorrectFileType("Type de fichier incorrect."));
        setUploading(new Uploading()
                .setStatus(new Uploading.Status().setConnecting("Connexion en cours...")
                        .setStalled("Stalled")
                        .setProcessing("Traitement du fichier").setHeld("Ajouté à la file d'attente"))
                .setRemainingTime(new Uploading.RemainingTime()
                        .setPrefix("remaining time: ")
                        .setUnknown("unknown remaining time"))
                .setError(new Uploading.Error()
                        .setServerUnavailable(
                                "Upload failed, please try again later")
                        .setUnexpectedServerError(
                                "Upload failed due to server error")
                        .setForbidden("Upload forbidden")));
        setUnits(new Units().setSize(Arrays.asList("B", "kB", "MB", "GB", "TB",
                "PB", "EB", "ZB", "YB")));
    }
}