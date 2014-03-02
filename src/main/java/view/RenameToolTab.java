package view;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;

import view.interfaces.AbstractTab;
import view.structure.ImagePanel;
import view.subview.HelpDialogView;
import net.miginfocom.swing.MigLayout;
import model.exception.TabInitException;
import model.table.RenameTableModel;
import model.util.Graphics;

import com.cf.mls.MLS;

import config.Config;

public class RenameToolTab extends AbstractTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4775366450281001645L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	/**
	 * current width of each table column
	 */
	private int[] tableWidth;

	/*
	 * components
	 */
	private JButton addFileB;
	private JButton deleteAllB;
	private JButton deleteFileB;
	private JButton addFolderB;
	private JButton renameFilesB;
	private JButton showChangesB;
	private JCheckBox replaceUnderscoreChB;
	private JCheckBox trimChB;
	private JCheckBox replaceSpaceChB;
	private JCheckBox recursiveChB;
	private JCheckBox replaceChB;
	private JLabel sourceJL;
	private JLabel targetJL;
	private JLabel extensionJL;
	private JLabel withJL;
	private JPanel regexP;
	private JPanel helpIP;
	private JPanel filesP;
	private JPanel miscP;
	private JRadioButton lowerCaseRB;
	private JRadioButton upperCaseRB;
	private JRadioButton unchangedRB;
	private JRadioButton filenameRB;
	private JRadioButton id3tagRB;
	private JTable foundT;
	private JComboBox<String> sourceCB;
	private JComboBox<String> targetCB;
	private JTextField replaceWithTF;
	private JTextField replaceTF;

	/**
	 * Constructor
	 */
	public RenameToolTab() {
		mls = new MLS("view/languageFiles/RenameToolTab", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
		this.tableWidth = new int[] { 170, 170 };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see view.interfaces.AbstractTab#init()
	 */
	@Override
	public void init() throws TabInitException {
		try {
			regexP = mls.generateTitledBevelPanel("getInfoP", BevelBorder.LOWERED, true);
			helpIP = new ImagePanel(Graphics.readImageFromJar("view/images/general/help.png"));
			mls.addCustomJPanel(helpIP, "helpIP");
			filesP = mls.generateTitledBevelPanel("filesP", BevelBorder.LOWERED, true);
			miscP = mls.generateTitledBevelPanel("miscP", BevelBorder.LOWERED, true);
			renameFilesB = mls.generateJButton("renameFilesB", true, null);
			addFileB = mls.generateJButton("addFileB", true, null);
			deleteAllB = mls.generateJButton("deleteAllB", true, null);
			deleteFileB = mls.generateJButton("deleteFileB", true, null);
			addFolderB = mls.generateJButton("addFolderB", true, null);
			showChangesB = mls.generateJButton("showChangesB", true, null);
			replaceUnderscoreChB = mls.generateJCheckBox("replaceUnderscoreChB", true, false, null);
			trimChB = mls.generateJCheckBox("trimChB", true, false, null);
			replaceSpaceChB = mls.generateJCheckBox("replaceSpaceChB", true, false, null);
			recursiveChB = mls.generateJCheckBox("recursiveChB", true, false, null);
			replaceChB = mls.generateJCheckBox("replaceChB", true, false, null);
			sourceJL = mls.generateJLabel("sourceJL", true);
			targetJL = mls.generateJLabel("targetJL", true);
			extensionJL = mls.generateJLabel("extensionJL", true);
			withJL = mls.generateJLabel("withJL", true);
			lowerCaseRB = mls.generateJRadioButton("lowerCaseChB", true, null);
			upperCaseRB = mls.generateJRadioButton("upperCaseChB", true, null);
			unchangedRB = mls.generateJRadioButton("unchangedChB", true, null);
			filenameRB = mls.generateJRadioButton("filenameRB", true, null);
			id3tagRB = mls.generateJRadioButton("id3tagRB", true, null);
			foundT = mls.generateJTable("foundT");
			foundT.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			sourceCB = mls.generateJComboBox("sourceCB", new ArrayList<String>(), -1, true, true, null);
			targetCB = mls.generateJComboBox("targetCB", new ArrayList<String>(), -1, true, true, null);
			replaceWithTF = mls.generateJTextField("replaceWithTF", true, true, 10, Config.getInstance().getRenameReplaceWith());
			replaceTF = mls.generateJTextField("replaceTF", true, true, 10, Config.getInstance().getRenameReplace());

			ButtonGroup bg = new ButtonGroup();
			bg.add(lowerCaseRB);
			bg.add(upperCaseRB);
			bg.add(unchangedRB);
			lowerCaseRB.setSelected(Config.getInstance().isRenameExtensionLowercaseSelected());
			upperCaseRB.setSelected(Config.getInstance().isRenameExtensionUppercaseSelected());
			unchangedRB.setSelected(!Config.getInstance().isRenameExtensionLowercaseSelected() && !Config.getInstance().isRenameExtensionUppercaseSelected());

			ButtonGroup bg2 = new ButtonGroup();
			bg2.add(filenameRB);
			bg2.add(id3tagRB);
			filenameRB.setSelected(Config.getInstance().isRenameFilenameSelected());
			id3tagRB.setSelected(!Config.getInstance().isRenameFilenameSelected());
			setSourceRegexEnabled(Config.getInstance().isRenameFilenameSelected());

			replaceSpaceChB.setSelected(Config.getInstance().isRenameSpaceWithUnderscoreSelected());
			replaceUnderscoreChB.setSelected(Config.getInstance().isRenameUnderscoreWithSpaceSelected());

			trimChB.setSelected(Config.getInstance().isRenameTrimFilenameSelected());

			recursiveChB.setSelected(Config.getInstance().isRenameRecursiveSelected());

			JPanel leftPanel = new JPanel(new MigLayout("insets 0", "[shrink]", "[][]"));

			// Regex Panel
			regexP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink]", "[shrink][shrink][shrink][shrink]"));

			regexP.add(id3tagRB, "wrap, span 3");
			regexP.add(filenameRB, "wrap 7, span 3");
			regexP.add(sourceJL);
			regexP.add(sourceCB, "grow, push, gapleft 10");
			int l = (int) (sourceCB.getPreferredSize().getHeight() + sourceCB.getPreferredSize().getHeight());
			regexP.add(helpIP, "span 1 2, gapleft 10, width " + l + "!, height " + l + "!, wrap");
			regexP.add(targetJL);
			regexP.add(targetCB, "grow, push, gapleft 10");

			leftPanel.add(regexP, "grow, wrap");

			// Misc Panel
			miscP.setLayout(new MigLayout("insets 5", "[shrink]", "[shrink][shrink][shrink][shrink][shrink]"));
			JPanel repP = new JPanel(new MigLayout("insets 0"));
			repP.add(replaceSpaceChB, "wrap");
			repP.add(replaceUnderscoreChB);
			miscP.add(repP, "wrap");
			miscP.add(extensionJL, "wrap, gaptop 8");
			JPanel extP = new JPanel(new MigLayout("insets 0"));
			extP.add(lowerCaseRB);
			extP.add(upperCaseRB);
			extP.add(unchangedRB);
			miscP.add(extP, "wrap");
			miscP.add(trimChB, "wrap, gaptop 8");
			JPanel rep2P = new JPanel(new MigLayout("insets 0", "[shrink][grow][shrink][grow]", "[shrink][shrink]"));
			rep2P.add(replaceChB);
			rep2P.add(replaceTF, "grow");
			rep2P.add(withJL);
			rep2P.add(replaceWithTF, "grow");
			miscP.add(rep2P, "gaptop 8, grow");

			leftPanel.add(miscP, "grow");

			JPanel rightPanel = new JPanel(new MigLayout("insets 0", "[grow]", "[grow][shrink]"));

			// Table Panel
			filesP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
			JScrollPane scrollpane = new JScrollPane();
			filesP.add(scrollpane, "grow, push");
			scrollpane.setViewportView(foundT);

			rightPanel.add(filesP, "grow, wrap");

			JPanel btnPanel = new JPanel(new MigLayout("insets 5", "", "[grow]"));
			btnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			btnPanel.add(addFileB);
			btnPanel.add(addFolderB);
			btnPanel.add(recursiveChB);
			btnPanel.add(deleteFileB);
			btnPanel.add(deleteAllB);
			btnPanel.add(showChangesB, "push, right, gapleft 150");
			btnPanel.add(renameFilesB, "right");

			rightPanel.add(btnPanel, "grow");

			setLayout(new MigLayout("insets 0", "[shrink][grow]", "[grow]"));

			add(leftPanel, "grow, width :350:");
			add(rightPanel, "grow");
		} catch (IOException e) {
			throw new TabInitException("couldn't find an image.");
		}
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		renameFilesB.addActionListener(l);
		showChangesB.addActionListener(l);

		addFileB.addActionListener(l);
		addFolderB.addActionListener(l);
		deleteFileB.addActionListener(l);
		deleteAllB.addActionListener(l);

		id3tagRB.addActionListener(l);
		filenameRB.addActionListener(l);
		
		replaceSpaceChB.addActionListener(l);
		replaceUnderscoreChB.addActionListener(l);
	}

	/**
	 * sets the transfer handler to drop files into the table
	 * 
	 * @param handler
	 *            the given transfer handler
	 */
	public void setTableTransferHandler(TransferHandler handler) {
		foundT.setFillsViewportHeight(true);
		foundT.setDragEnabled(true);
		foundT.setTransferHandler(handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see view.interfaces.AbstractTab#changeLanguage(java.util.Locale)
	 */
	@Override
	public void changeLanguage(Locale lang) {
		this.mls.setLocale(lang);
		this.mls.translate();
	}

	/**
	 * sets the mouse listener
	 * 
	 * @param l
	 *            the mouse listener
	 */
	public void setMouseListener(MouseListener l) {
		this.helpIP.addMouseListener(l);
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
	 * sets the table model and sets the current column widths
	 * 
	 * @param model
	 *            the table model
	 */
	public void setTableModel(RenameTableModel model) {
		model.setCol1Name(mls.getMessage("Col1"));
		model.setCol2Name(mls.getMessage("Col2"));

		this.foundT.setModel(model);
		this.foundT.getColumnModel().getColumn(0).setPreferredWidth(this.tableWidth[0]);
		this.foundT.getColumnModel().getColumn(1).setPreferredWidth(this.tableWidth[1]);
	}

	/**
	 * saves the current column widths
	 */
	public void saveTableWidth() {
		this.tableWidth[0] = this.foundT.getColumnModel().getColumn(0).getWidth();
		this.tableWidth[1] = this.foundT.getColumnModel().getColumn(1).getWidth();
	}

	public void setreplaceUnderscoreChB(boolean b0) {
		replaceUnderscoreChB.setSelected(b0);
	}

	public void settrimChB(boolean b0) {
		trimChB.setSelected(b0);
	}

	public void setreplaceSpaceChB(boolean b0) {
		replaceSpaceChB.setSelected(b0);
	}

	public void setrecursiveChB(boolean b0) {
		recursiveChB.setSelected(b0);
	}

	public boolean getreplaceUnderscoreChB() {
		return replaceUnderscoreChB.isSelected();
	}

	public boolean gettrimChB() {
		return trimChB.isSelected();
	}

	public boolean getreplaceSpaceChB() {
		return replaceSpaceChB.isSelected();
	}

	public boolean getrecursiveChB() {
		return recursiveChB.isSelected();
	}

	public boolean getUnchangedChB() {
		return this.unchangedRB.isSelected();
	}

	public boolean getLowerCaseRB() {
		return this.lowerCaseRB.isSelected();
	}

	public boolean getUpperCaseRB() {
		return this.upperCaseRB.isSelected();
	}

	public boolean getreplaceChB() {
		return replaceChB.isSelected();
	}

	public String getsourceTF() {
		return (String) sourceCB.getSelectedItem();
	}

	public String gettargetTF() {
		return (String) targetCB.getSelectedItem();
	}

	public String getreplaceWithTF() {
		return replaceWithTF.getText();
	}

	public String getreplaceTF() {
		return replaceTF.getText();
	}

	public boolean getID3TagRB() {
		return this.id3tagRB.isSelected();
	}

	/**
	 * sets the masks to the source and target CB
	 * 
	 * @param masks
	 *            the given masks
	 */
	public void setMasks(List<String> masks) {
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();

		for (String m : masks) {
			model1.addElement(m);
			model2.addElement(m);
		}

		this.sourceCB.setModel(model1);
		this.sourceCB.setSelectedItem(Config.getInstance().getRenameSource());
		this.targetCB.setModel(model2);
		this.targetCB.setSelectedItem(Config.getInstance().getRenameTarget());
	}

	/**
	 * opens the help dialog
	 */
	public void openHelpDialog() {
		HelpDialogView h = new HelpDialogView();
		h.init(this.mls.getMessage("help"));
		h.setVisible(true);
	}

	public int[] getSelectedIndices() {
		return this.foundT.getSelectedRows();
	}

	/**
	 * sets the buttons enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setButtonsEnabled(boolean en) {
		this.recursiveChB.setEnabled(en);
		this.showChangesB.setEnabled(en);
		this.renameFilesB.setEnabled(en);
		this.addFileB.setEnabled(en);
		this.addFolderB.setEnabled(en);
		this.deleteFileB.setEnabled(en);
		this.deleteAllB.setEnabled(en);
	}

	/**
	 * sets the source regex label and combobox enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setSourceRegexEnabled(boolean en) {
		this.sourceJL.setEnabled(en);
		this.sourceCB.setEnabled(en);
	}
}