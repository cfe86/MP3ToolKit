package view.subview.id3;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import view.structure.ImagePanel;
import model.util.Graphics;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class GenByNameView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;
	private ImagePanel helpIP;
	private JLabel regexJL;
	private JPanel panelP;
	private JComboBox<String> regexCB;
	private JButton cancelB;
	private JButton startB;
	private JRadioButton replaceSelectedRB;
	private JRadioButton replaceAllRB;

	/**
	 * Constructor
	 */
	public GenByNameView() {
		mls = new MLS("view/languageFiles/GenByNameView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 * 
	 * @throws IOException
	 *             thrown if an image couldn't be loaded
	 */
	public void init() throws IOException {
		mls.addJFrame("window", this);

		panelP = mls.generateTitledBevelPanel("panelP", BevelBorder.LOWERED);
		helpIP = new ImagePanel(Graphics.readImageFromJar("view/images/general/helpSmall.png"));
		mls.addCustomJPanel(helpIP, "helpIP");
		regexJL = mls.generateJLabel("regexJL", true);
		regexCB = mls.generateJComboBox("regexCB", new ArrayList<String>(), -1, true, true, null);
		cancelB = mls.generateJButton("cancelB");
		startB = mls.generateJButton("startB");
		replaceAllRB = mls.generateJRadioButton("replaceAllRB", true, null);
		replaceSelectedRB = mls.generateJRadioButton("replaceSelectedRB", true, null);

		ButtonGroup bg = new ButtonGroup();
		bg.add(replaceAllRB);
		bg.add(replaceSelectedRB);
		replaceSelectedRB.setSelected(true);

		contentPane = new JPanel(new MigLayout("insets 4", "[grow]", "[grow]"));
		setContentPane(contentPane);
		panelP.setLayout(new MigLayout("insets 5", "[shrink][grow][shrink]", "[shrink][shrink]"));
		JPanel btnP = new JPanel(new MigLayout("insets 0"));
		JPanel rbP = new JPanel(new MigLayout("insets 0"));
		rbP.add(replaceAllRB, "wrap");
		rbP.add(replaceSelectedRB);

		btnP.add(rbP, "right, push");
		btnP.add(cancelB, "right");
		btnP.add(startB, "right");

		panelP.add(regexJL, "gapbottom 5");
		panelP.add(regexCB, "grow, gapleft 15, width :250:, gapbottom 5");

		// scale help panel
		int l = (int) regexCB.getPreferredSize().getHeight();
		panelP.add(helpIP, "height :" + l + ":, width :" + l + ":, gapleft 10, gapbottom 5, wrap");

		panelP.add(btnP, "grow, span 3, gaptop 10");

		contentPane.add(panelP, "grow");
		pack();
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		this.cancelB.addActionListener(l);
		this.startB.addActionListener(l);
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

	public String getRegex() {
		return (String) this.regexCB.getSelectedItem();
	}

	/**
	 * sets the masks to the regex combobox
	 * 
	 * @param masks
	 *            the masks
	 */
	public void setMasks(List<String> masks) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

		for (String m : masks)
			model.addElement(m);

		this.regexCB.setModel(model);
		this.regexCB.setSelectedItem("");
	}

	public boolean isReplaceAll() {
		return this.replaceAllRB.isSelected();
	}
}