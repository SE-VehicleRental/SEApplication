package software.project;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class PDFGeneratorTest {

    private final String customerName = "Hala";

    @AfterEach
    void deleteCreatedPdf() {

        File file = new File("Promissory_Note_" + customerName + ".pdf");

        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void pdfShouldBeCreatedSuccessfully() {

        BufferedImage signature =
                new BufferedImage(150, 60, BufferedImage.TYPE_INT_RGB);

        PDFGenerator.generatePDF(
                customerName,
                "123456789",
                "0599999999",
                "Car",
                "BMW",
                "123-456",
                "100",
                signature
        );

        File pdf =
                new File("Promissory_Note_" + customerName + ".pdf");

        assertTrue(pdf.exists());
        assertTrue(pdf.length() > 0);
    }

    @Test
    void pdfShouldBeCreatedWithDifferentCustomer() {

        String otherCustomer = "Ali";

        BufferedImage signature =
                new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);

        PDFGenerator.generatePDF(
                otherCustomer,
                "987654321",
                "0566666666",
                "Bus",
                "Mercedes",
                "555-888",
                "250",
                signature
        );

        File pdf =
                new File("Promissory_Note_" + otherCustomer + ".pdf");

        assertTrue(pdf.exists());
        assertTrue(pdf.length() > 0);

        pdf.delete();
    }
}