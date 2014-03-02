package view.subview.id3;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class ImageSettingsView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3129547359061131758L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;
	private JButton okB;
	private JButton linkB;
	private JButton cancelB;
	private JPanel optionP;
	private JRadioButton resizeRB;
	private JRadioButton deleteRB;
	private JTextField heightTF;
	private JTextField widthTF;

	/**
	 * Constructor
	 */
	public ImageSettingsView() {
		mls = new MLS("view/languageFiles/ImageSettingsView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {
		mls.addJFrame("window", this);

		contentPane = new JPanel();
		okB = mls.generateJButton("okB", true, null);
		linkB = mls.generateJButton("linkB", true, null);
		cancelB = mls.generateJButton("cancelB", true, null);
		optionP = mls.generateTitledPanel("optionP", true);
		resizeRB = mls.generateJRadioButton("resizeRB", true, null);
		deleteRB = mls.generateJRadioButton("deleteRB", true, null);
		heightTF = mls.generateJTextField("heightTF", true, true, 10, "");
		widthTF = mls.generateJTextField("widthTF", true, true, 10, "");

		ButtonGroup bg = new ButtonGroup();
		bg.add(resizeRB);
		bg.add(deleteRB);
		resizeRB.setSelected(true);

		setContentPane(contentPane);

		optionP.setLayout(new MigLayout("insets 5", "[grow]", "[shrink][shrink][shrink]"));

		JPanel btnP = new JPanel(new MigLayout("insets 0"));
		btnP.add(cancelB, "push, right");
		btnP.add(okB, "right, gapright 2");

		optionP.add(deleteRB, "wrap");
		optionP.add(resizeRB, "wrap");

		JPanel sizeP = new JPanel(new MigLayout("insets 0", "[shrink][shrink][shrink][shrink][shrink]", "[shrink]"));
		sizeP.add(widthTF);
		sizeP.add(new JLabel("x"));
		sizeP.add(heightTF);
		sizeP.add(new JLabel("px"));

		int l = (int) widthTF.getPreferredSize().getHeight();
		sizeP.add(linkB, "height " + l + "!, width " + l + "!");

		optionP.add(sizeP, "grow, gaptop 5");

		contentPane.setLayout(new MigLayout("insets 7", "", ""));
		contentPane.add(optionP, "wrap");
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
		okB.addActionListener(l);
		linkB.addActionListener(l);
		cancelB.addActionListener(l);
		resizeRB.addActionListener(l);
		deleteRB.addActionListener(l);
	}

	/**
	 * sets a FocusListener
	 * 
	 * @param l
	 *            the FocusListener
	 */
	public void setFocusListener(FocusListener l) {
		this.widthTF.addFocusListener(l);
		this.heightTF.addFocusListener(l);
	}

	/**
	 * sets the image size textfields
	 * 
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setImageSize(int width, int height) {
		this.widthTF.setText(Integer.toString(width));
		this.heightTF.setText(Integer.toString(height));
	}

	public int getImageWidth() {
		return Integer.parseInt(this.widthTF.getText());
	}

	public int getImageHeight() {
		return Integer.parseInt(this.heightTF.getText());
	}

	/**
	 * sets the resize textfields and label enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setResizeEnabled(boolean en) {
		this.heightTF.setEnabled(en);
		this.widthTF.setEnabled(en);
		this.linkB.setEnabled(en);
	}

	public boolean isDelete() {
		return this.deleteRB.isSelected();
	}

	public JTextField getWidthField() {
		return this.widthTF;
	}

	/**
	 * sets the link image (could be linked or unlinked)
	 * 
	 * @param img
	 *            the icon
	 */
	public void setLinkImage(BufferedImage img) {
		this.linkB.setIcon(new ImageIcon(img));
	}
}