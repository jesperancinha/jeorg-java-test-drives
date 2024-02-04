package org.jesperancinha;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class SecretDocument {

    public static final String SOURCE_DOCUMENT_TEST_PDF = "secret-document/test.pdf";

    public static void main(String[] args) throws IOException {
        currentDirectoryInfo();
        createSecretFile(SOURCE_DOCUMENT_TEST_PDF);
        readAndCopySecretFile(getSecretName(SOURCE_DOCUMENT_TEST_PDF));
    }

    private static String getSecretName(String name) {
        return getNameWithPrefix(name, "secret");
    }

    private static String getSecretReadName(String name) {
        return getNameWithPrefix(name, "secret");
    }

    private static String getNameWithPrefix(String name, String secret) {
        return String.format("%s%s%s%s", name.split("\\.pdf")[0], "-", secret, ".pdf");
    }

    private static void currentDirectoryInfo() {
        out.println(Arrays
                .stream(Objects.requireNonNull(new File(".")
                        .listFiles()))
                .map(File::getAbsolutePath)
                .collect(Collectors.joining("\n")));
    }

    public static void readAndCopySecretFile(String secretDocumentRead) throws IOException {
        var secretReadName = getSecretReadName(secretDocumentRead);
        if(new File(secretReadName).delete()){
            out.printf("File %s has been deleted! %n",secretReadName);
        }
        var pdDocument = Loader.loadPDF(new File(secretDocumentRead));
        pdDocument.save(new File(secretReadName));
        pdDocument.close();
    }

    public static File createSecretFile(String inputPdf) throws IOException {
        var secretName = getSecretName(inputPdf);
        if(new File(secretName).delete()){
            out.printf("File %s has been deleted! %n",secretName);
        }
        var pdDocument = Loader.loadPDF(new File(inputPdf));
        var accessPermission = new AccessPermission();
        accessPermission.setCanModify(false);
        accessPermission.setCanPrint(false);
        accessPermission.setCanExtractContent(false);
        accessPermission.setReadOnly();
        var standardProtectionPolicy = new StandardProtectionPolicy("owner", "user", accessPermission);
        standardProtectionPolicy.setEncryptionKeyLength(128);
        pdDocument.protect(standardProtectionPolicy);
        var file = new File(secretName);
        pdDocument.save(file);
        pdDocument.close();
        return file;
    }
}