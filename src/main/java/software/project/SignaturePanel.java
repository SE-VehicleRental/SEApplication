package software.project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Graphics;

public class SignaturePanel extends JPanel {

	private BufferedImage signatureImage;
	private Graphics2D g2;
	private int previousX;
	private int previousY;
	
	public SignaturePanel() {

	    setPreferredSize(new Dimension(400,120));

	    setBackground(Color.WHITE);

	    setBorder(BorderFactory.createLineBorder(Color.BLACK));

	    signatureImage = new BufferedImage(400,120,BufferedImage.TYPE_INT_ARGB);

	    g2 = signatureImage.createGraphics();

	    g2.setColor(Color.BLACK);

	    g2.setStroke(new BasicStroke(2));

	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                        RenderingHints.VALUE_ANTIALIAS_ON);
	    addMouseListener(new MouseAdapter() {

	        @Override
	        public void mousePressed(MouseEvent e) {

	            previousX = e.getX();
	            previousY = e.getY();

	        }
	        
	        

	    });
	    
	    addMouseMotionListener(new MouseAdapter() {

	        @Override
	        public void mouseDragged(MouseEvent e) {

	            int currentX = e.getX();
	            int currentY = e.getY();

	            g2.drawLine(previousX, previousY, currentX, currentY);

	            previousX = currentX;
	            previousY = currentY;

	            repaint();

	        }

	    });
	    
	}
	
	@Override
	protected void paintComponent(Graphics g) {

	    super.paintComponent(g);

	    g.drawImage(signatureImage, 0, 0, null);

	}
	
	public BufferedImage getSignatureImage() {
	    return signatureImage;
	}

}