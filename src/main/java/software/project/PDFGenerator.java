package software.project;

import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.lowagie.text.Image;

public class PDFGenerator {

	private static final Logger LOGGER =
	        Logger.getLogger(PDFGenerator.class.getName());
    public static void generatePDF(
    		 String customerName,
    	        String customerId,
    	        String customerPhone,
    	        String vehicleType,
    	        String vehicleModel,
    	        String VehicleNumber,
    	        String pricePerDay,
    	        BufferedImage signatureImage) {

        try {

            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream("Promissory_Note_" + customerName + ".pdf"));

            document.open();

            document.add(new Paragraph("PROMISSORY NOTE"));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("NABLUS RENT COMPANY"));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Customer Name: " + customerName));
            document.add(new Paragraph("National ID: " + customerId));
            document.add(new Paragraph("Phone Number: " + customerPhone));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Vehicle Type: " + vehicleType));
            document.add(new Paragraph("Vehicle company: " + vehicleModel));
            document.add(new Paragraph("Vehicle model: " + VehicleNumber));
            document.add(new Paragraph("Price Per Day: " + pricePerDay));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Guarantee Amount: 5000 ₪"));
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Customer Signature:"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(signatureImage, "png", baos);

            Image signature = Image.getInstance(baos.toByteArray());

            signature.scaleToFit(180, 70);

            document.add(signature);

            document.close();

            LOGGER.info("PDF created successfully.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create PDF.", e);
        }

    }

}
