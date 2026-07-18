package software.project;

import javax.swing.*;
import java.awt.*;

public class PromissoryNoteForm extends JFrame {

	public PromissoryNoteForm(
	        String customerName,
	        String customerId,
	        String customerPhone,
	        String vehicleType,
	        String vehicleCompany,
	        String VehicleModel,
	        String pricePerDay,
	        String rentalStartDate,
	        String rentalEndDate,
	        String totalCost,
	        Runnable onConfirmed) {   
        setTitle("Promissory Note");
        setSize(850, 1050);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel title = new JLabel("PROMISSORY NOTE");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(220, 20, 300, 30);
        panel.add(title);

        JLabel company = new JLabel("NABLUS RENT COMPANY");
        company.setFont(new Font("Arial", Font.BOLD, 18));
        company.setBounds(210, 60, 300, 25);
        panel.add(company);

        JSeparator line = new JSeparator();
        line.setBounds(40,100,600,5);
        panel.add(line);

        JTextArea noteText = new JTextArea();

        noteText.setBounds(50,130,650,480);
        noteText.setFont(new Font("Serif", Font.PLAIN, 16));
        noteText.setEditable(false);
        noteText.setLineWrap(true);
        noteText.setWrapStyleWord(true);

        noteText.setText(

        		"Date : " + java.time.LocalDate.now() + "\n\n"

        		+ "I, " + customerName
        		+ ", holder of National ID No. "
        		+ customerId + ",\n\n"

        		+ "hereby acknowledge that I have rented the following vehicle from "
        		+ "Nablus Rent Company.\n\n"

        		+ "Vehicle Type : " + vehicleType + "\n\n"

        		+ "Vehicle Company : " + vehicleCompany + "\n\n"

        		+ "Vehicle Model : " + VehicleModel + "\n\n"

        		+ "Customer Phone : " + customerPhone + "\n\n"

        		+ "Rental Start Date : " + rentalStartDate + "\n\n"

        		+ "Rental End Date : " + rentalEndDate + "\n\n"

        		+ "Rental Price Per Day : " + pricePerDay + " ₪\n\n"

        		+ "Total Rental Cost : " + totalCost + "₪ \n\n"

        		+ "Guarantee Amount : 5000 JD\n\n"

        		+ "I undertake to return the vehicle in the same condition in which I received it.\n\n"

        		+ "In case of damage, loss, or failure to return the vehicle,\n"

        		+ "I agree to pay the guarantee amount specified by the company.\n\n"

        		);

        JScrollPane scrollPane = new JScrollPane(noteText);
        scrollPane.setBounds(50, 130, 650, 480);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane);

        JLabel signatureLabel = new JLabel("Customer Signature:");
        signatureLabel.setFont(new Font("Arial", Font.BOLD, 16));
        signatureLabel.setBounds(50,630,200,30);
        panel.add(signatureLabel);

        SignaturePanel signaturePanel = new SignaturePanel();
        signaturePanel.setBounds(50,670,400,120);
        panel.add(signaturePanel);

        JButton pdfButton = new JButton("Generate PDF");
        pdfButton.setBounds(520,700,170,35);
        panel.add(pdfButton);

        pdfButton.addActionListener(e -> {

            if (signaturePanel.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please sign before generating the PDF!",
                        "Signature Required",
                        JOptionPane.WARNING_MESSAGE
                );
                return; 
            }

            PDFGenerator.generatePDF(
                    customerName,
                    customerId,
                    customerPhone,
                    vehicleType,
                    vehicleCompany,
                    VehicleModel,
                    pricePerDay,
                    signaturePanel.getSignatureImage()
            );

            if (onConfirmed != null) {
                onConfirmed.run();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Promissory note created successfully, and the rental has been saved!"
            );

            pdfButton.setEnabled(false); 
        });

        add(panel);

        setVisible(true);
    }

}