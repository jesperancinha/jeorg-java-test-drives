package org.jesperancinha;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SecretDocumentTest {

    public static final String TEST_SECRET_PDF = "/test-secret.pdf";
    public static final String TEST_PDF = "/test.pdf";
    public static final String TEST_COPY_PDF = "./test-copy.pdf";

    @Test
    void readAndCopySecretFile() {
        final var file = Objects.requireNonNull(this.getClass().getResource(TEST_SECRET_PDF)).getFile();
        assertThrowsExactly(InvalidPasswordException.class, () ->
                SecretDocument.readAndCopySecretFile(file)
        );
    }

    @Test
    void readAndCopySecretFileRaw() {
        final var file = Objects.requireNonNull(this.getClass().getResource(TEST_SECRET_PDF)).getFile();
        assertThrowsExactly(InvalidPasswordException.class, () ->
                SecretDocument.readAndCopySecretFileRaw(file)
        );
    }

    @Test
    void createSecretFile() throws IOException {
        final var file = Objects.requireNonNull(this.getClass().getResource(TEST_PDF)).getFile();
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
        final var file = Objects.requireNonNull(this.getClass().getResource(TEST_SECRET_PDF)).getFile();
        final var value = new String(Files.readAllBytes(Path.of(file)), StandardCharsets.UTF_8);
        System.out.println(value);
        assertFalse(value.contains("This is a test"));
    }

    /**
     * Explicitly ignoring warning
     * @throws IOException
     */
    @Test
    void readAndCopyInputStream() throws IOException {
        final var file = Objects.requireNonNull(this.getClass().getResource(TEST_SECRET_PDF)).getFile();
        final var bytes = Files.readAllBytes(Path.of(file));
        final var copyFile = new File(TEST_COPY_PDF);
        copyFile.createNewFile();
        try (final var fos = new FileOutputStream(copyFile)) {
            fos.write(bytes);
        }
        final var newBytes = Files.readAllBytes(Path.of(TEST_COPY_PDF));
        assertEquals(bytes.length, newBytes.length);
        assertThrowsExactly(InvalidPasswordException.class, () ->
                SecretDocument.readAndCopySecretFileRaw(TEST_COPY_PDF)
        );
        copyFile.deleteOnExit();
    }
}