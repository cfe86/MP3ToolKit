package view.subview.id3;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import view.structure.ImagePanel;
import model.structure.ID3ImageData;
import model.util.Graphics;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;
import config.Constants;

public class ChangeImageDataView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6895063590922886791L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;
	private ImagePanel currIP;
	private ImagePanel newIP;
	private JCheckBox imageChB;
	private JCheckBox repeatChB;
	private JLabel newExtensionJL;
	private JLabel sizeCurrJL;
	private JLabel sizeNewJL;
	private JLabel currSizeJL;
	private JLabel extNewJL;
	private JLabel newSizeJL;
	private JLabel currDimJL;
	private JLabel extCurrJL;
	private JLabel currExtensionJL;
	private JLabel dimNewJL;
	private JLabel newDimJL;
	private JLabel dimCurrJL;
	private JLabel counterJL;
	private JLabel artistJL;
	private JPanel tagP;
	private JButton nextTagB;

	/**
	 * Constructor
	 */
	public ChangeImageDataView() {
		mls = new MLS("view/languageFiles/ChangeImageDataView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {
		mls.addJFrame("window", this);

		contentPane = new JPanel();
		tagP = mls.generateTitledBevelPanel("tagP", BevelBorder.LOWERED, true);
		currIP = new ImagePanel(null);
		mls.addCustomJPanel(currIP, "currIP");
		newIP = new ImagePanel(null);
		mls.addCustomJPanel(newIP, "newIP");
		imageChB = mls.generateJCheckBox("imageChB", true, false, null);
		repeatChB = mls.generateJCheckBox("repeatChB", true, false, null);
		newExtensionJL = mls.generateJLabel("newExtensionJL", true);
		sizeCurrJL = mls.generateJLabel("sizeCurrJL", true);
		sizeNewJL = mls.generateJLabel("sizeNewJL", true);
		currSizeJL = mls.generateJLabel("currSizeJL", true);
		extNewJL = mls.generateJLabel("extNewJL", true);
		newSizeJL = mls.generateJLabel("newSizeJL", true);
		currDimJL = mls.generateJLabel("currDimJL", true);
		extCurrJL = mls.generateJLabel("extCurrJL", true);
		currExtensionJL = mls.generateJLabel("currExtensionJL", true);
		dimNewJL = mls.generateJLabel("dimNewJL", true);
		newDimJL = mls.generateJLabel("newDimJL", true);
		dimCurrJL = mls.generateJLabel("dimCurrJL", true);
		counterJL = mls.generateJLabel("counterJL", true);
		artistJL = mls.generateJLabel("artistJL", true);
		nextTagB = mls.generateJButton("nextTagB", true, null);

		setContentPane(contentPane);

		tagP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink][shrink]", "[shrink][shrink]"));

		tagP.add(artistJL, "push, center, span 4, gapbottom 15, wrap");
		tagP.add(currIP, "height :300:, width :300:");
		currIP.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		tagP.add(new ImagePanel(Constants.RIGHT_ARROW), "height 20!, width 20!, gapleft 15");
		tagP.add(newIP, "gapleft 15, height :300:, width :300:");
		newIP.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		tagP.add(imageChB, "wrap, gapleft 15");

		JPanel currDataP = new JPanel(new MigLayout("insets 0"));
		currDataP.add(dimCurrJL);
		currDataP.add(currDimJL, "wrap");
		currDataP.add(sizeCurrJL);
		currDataP.add(currSizeJL, "wrap");
		currDataP.add(extCurrJL);
		currDataP.add(currExtensionJL);

		JPanel newDataP = new JPanel(new MigLayout("insets 0"));
		newDataP.add(dimNewJL);
		newDataP.add(newDimJL, "wrap");
		newDataP.add(sizeNewJL);
		newDataP.add(newSizeJL, "wrap");
		newDataP.add(extNewJL);
		newDataP.add(newExtensionJL);

		tagP.add(currDataP, "gaptop 7");
		tagP.add(new JLabel());
		tagP.add(newDataP, "gapleft 15, gaptop 7, wrap");

		tagP.add(counterJL, "push, center, span 4, gaptop 15");

		JPanel btnP = new JPanel(new MigLayout("insets 0"));
		btnP.add(repeatChB, "left");
		btnP.add(nextTagB, "push, right, gapright 2");

		contentPane.setLayout(new MigLayout("insets 7", "[grow]", "[grow][shrink]"));

		contentPane.add(tagP, "wrap");
		contentPane.add(btnP, "grow");

		pack();
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		nextTagB.addActionListener(l);
	}

	public boolean isRepeatSelected() {
		return this.repeatChB.isSelected();
	}

	/**
	 * shows a given message
	 * 
	 * @param identifier
	 *            identifier for the mls
	 */
	public void showMessage(String identifier) {
		JOptionPane.showMessageDialog(this, mls.getMessage(identifier));
	}

	/**
	 * converts the given number of bytes to KB
	 * 
	 * @param bytes
	 *            number of bytes
	 * 
	 * @return given bytes in KB
	 */
	private String convertSizeInKB(long bytes) {

		// to KB
		double result = ((double) bytes) / 1024.0;

		return Double.toString(Math.floor((result * 100)) / 100);
	}

	/**
	 * sets the current image
	 * 
	 * @param data
	 *            the current image data
	 * 
	 * @throws IOException
	 *             thrown if image couldn't be loaded
	 */
	public void setCurrImage(ID3ImageData data) throws IOException {
		// convert byte array back to BufferedImage
		BufferedImage img;
		if (data.getImage() == null || data.getImage().length == 0) {
			img = Constants.EMPTY;
			// set dimension + extension
			this.currDimJL.setText(mls.getMessage("currDimJL"));
			this.currExtensionJL.setText(mls.getMessage("currExtensionJL"));
			this.currSizeJL.setText(mls.getMessage("currSizeJL"));
		} else {
			InputStream in = new ByteArrayInputStream(data.getImage());
			img = ImageIO.read(in);
			in.close();
			// set dimension + extension
			this.currDimJL.setText(img.getWidth() + "x" + img.getHeight());
			this.currExtensionJL.setText("*." + data.getExtension());

			long size = data.getImage().length;

			this.currSizeJL.setText(convertSizeInKB(size) + " KB");
		}

		img = Graphics.scale(img, this.currIP.getWidth(), this.currIP.getHeight());
		this.currIP.setImage(img);
	}

	/**
	 * sets the new image
	 * 
	 * @param data
	 *            the new image data
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be loaded
	 */
	public void setNewImage(ID3ImageData data) throws IOException {
		// convert byte array back to BufferedImage
		BufferedImage img;
		if (data.getImage() == null || data.getImage().length == 0) {
			img = Constants.EMPTY;
			// set dimension + extension
			this.newDimJL.setText(mls.getMessage("newDimJL"));
			this.newExtensionJL.setText(mls.getMessage("newExtensionJL"));
			this.newSizeJL.setText(mls.getMessage("newSizeJL"));

			this.imageChB.setSelected(false);
		} else {
			InputStream in = new ByteArrayInputStream(data.getImage());
			img = ImageIO.read(in);
			in.close();
			// set dimension + extension
			this.newDimJL.setText(img.getWidth() + "x" + img.getHeight());
			this.newExtensionJL.setText("*." + data.getExtension());

			long size = data.getImage().length;

			this.newSizeJL.setText(convertSizeInKB(size) + " KB");

			this.imageChB.setSelected(true);
		}

		img = Graphics.scale(img, this.newIP.getWidth(), this.newIP.getHeight());
		this.newIP.setImage(img);
	}

	/**
	 * sets the current counter
	 * 
	 * @param curr
	 *            current count
	 * @param of
	 *            max count
	 */
	public void setCounter(int curr, int of) {
		if (curr < 0 && of < 1)
			return;

		this.counterJL.setText(curr + "/" + of);
	}

	public boolean getImageChB() {
		return this.imageChB.isSelected();
	}

	/**
	 * sets the artist and title label
	 * 
	 * @param artist
	 *            the artist
	 * @param title
	 *            the title
	 */
	public void setArtist(String artist, String title) {
		this.artistJL.setText(artist + " - " + title);
	}
}