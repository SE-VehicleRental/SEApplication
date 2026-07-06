package software.project;

import javax.swing.*;
import java.awt.*;

public class PromissoryNoteForm extends JFrame {

    public PromissoryNoteForm(String customerName,
            String customerId,
            String customerPhone,
            String vehicleType,
            String vehicleModel,
            String plateNumber,
            String pricePerDay) {

        setTitle("Promissory Note");
        setSize(700, 700);
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

        noteText.setBounds(50,130,600,300);
        noteText.setFont(new Font("Serif", Font.PLAIN, 16));
        noteText.setEditable(false);
        noteText.setLineWrap(true);
        noteText.setWrapStyleWord(true);

        noteText.setText(
                "Date: " + java.time.LocalDate.now() + "\n\n" +

                "I, " + customerName +
                ", holder of National ID No. " + customerId + ",\n\n" +

                "hereby acknowledge that I have rented the following vehicle from Nablus Rent Company.\n\n" +

                "Vehicle Type : " + vehicleType + "\n\n" +

                "Vehicle Model : " + vehicleModel + "\n\n" +

                "Customer Phone : " + customerPhone + "\n\n" +
                "Plate Number : " + plateNumber + "\n\n" +

                "Rental Price Per Day : " + pricePerDay + "\n\n" +
                
                "Guarantee Amount : 5000 JD\n\n" +

                "I undertake to return the vehicle in the same condition in which I received it.\n" +
                "In case of damage, loss, or failure to return the vehicle,\n" +
                "I agree to pay the guarantee amount specified by the company.\n\n\n"
        );

        panel.add(noteText);
        
        JLabel signatureLabel = new JLabel("Customer Signature:");
        signatureLabel.setFont(new Font("Arial", Font.BOLD, 16));
        signatureLabel.setBounds(50, 450, 200, 30);
        panel.add(signatureLabel);
        
        SignaturePanel signaturePanel = new SignaturePanel();
        signaturePanel.setBounds(50, 490, 400, 120);
        panel.add(signaturePanel);
        
        JButton pdfButton = new JButton("Generate PDF");
        pdfButton.setBounds(500, 620, 150, 30);
        panel.add(pdfButton);
        pdfButton.addActionListener(e -> {

            PDFGenerator.generatePDF(
                    customerName,
                    customerId,
                    customerPhone,
                    vehicleType,
                    vehicleModel,
                    plateNumber,
                    pricePerDay,
                    signaturePanel.getSignatureImage()
                    
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Promissory Note PDF has been created successfully!"
            );

        });

        add(panel);

        setVisible(true);
    }

}