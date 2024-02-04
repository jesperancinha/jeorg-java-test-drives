package org.jesperancinha;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SecretDocumentTest {

    public static final String TEST_SECRET_PDF = "/test-secret.pdf";
    public static final String TEST_PDF = "/test.pdf";

    @Test
    void readAndCopySecretFile() {
        var file = Objects.requireNonNull(this.getClass().getResource(TEST_SECRET_PDF)).getFile();
        assertThrowsExactly(InvalidPasswordException.class, () ->
                SecretDocument.readAndCopySecretFile(file)
        );
    }

    @Test
    void createSecretFile() throws IOException {
        var file = Objects.requireNonNull(this.getClass().getResource(TEST_PDF)).getFile();
        File secretFile = SecretDocument.createSecretFile(file);
        System.out.printf("Secret file was created in %s%n", secretFile.getAbsolutePath());
        boolean delete = secretFile.delete();
        assertTrue(delete);
    }

    /**
     * Explicitly ignoring warning
     * @throws IOException
     */
    @Test
    void readInputStream() throws IOException {
        var file = Objects.requireNonNull(this.getClass().getResource(TEST_SECRET_PDF)).getFile();
        String value = new String(Files.readAllBytes(Path.of(file)), StandardCharsets.UTF_8);
        System.out.println(value);
        assertFalse(value.contains("This is a test"));
    }
}