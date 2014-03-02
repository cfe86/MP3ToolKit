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
import model.table.StructureTableModel;
import model.util.Graphics;

import com.cf.mls.MLS;

import config.Config;

public class FolderCreatorTab extends AbstractTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7593038380759160032L;

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
	private JButton showChangesB;
	private JButton makeStructureB;
	private JButton targetFolderOpenB;
	private JCheckBox recursiveChB;
	private JLabel structureJL;
	private JLabel regexJL;
	private JLabel targetJL;
	private JPanel regexHelpIP;
	private JPanel structureHelpIP;
	private JPanel structureP;
	private JPanel filesP;
	private JPanel infoP;
	private JRadioButton filenameRB;
	private JRadioButton id3tagRB;
	private JTable tableT;
	private JComboBox<String> regexCB;
	private JComboBox<String> structureCB;
	private JTextField targetFolderTF;

	/**
	 * Constructor
	 */
	public FolderCreatorTab() {
		mls = new MLS("view/languageFiles/FolderCreatorTab", Config.getInstance().getCurrentLanguage());
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
			regexHelpIP = new ImagePanel(Graphics.readImageFromJar("view/images/general/helpSmall.png"));
			mls.addCustomJPanel(regexHelpIP, "regexHelpIP");
			structureHelpIP = new ImagePanel(Graphics.readImageFromJar("view/images/general/helpSmall.png"));
			mls.addCustomJPanel(structureHelpIP, "structureHelpIP");
			structureP = mls.generateTitledBevelPanel("structureP", BevelBorder.LOWERED, true);
			filesP = mls.generateTitledBevelPanel("filesP", BevelBorder.LOWERED, true);
			infoP = mls.generateTitledBevelPanel("infoP", BevelBorder.LOWERED, true);
			addFileB = mls.generateJButton("addFileB", true, null);
			deleteAllB = mls.generateJButton("deleteAllB", true, null);
			deleteFileB = mls.generateJButton("deleteFileB", true, null);
			addFolderB = mls.generateJButton("addFolderB", true, null);
			showChangesB = mls.generateJButton("showChangesB", true, null);
			makeStructureB = mls.generateJButton("makeStructureB", true, null);
			targetFolderOpenB = mls.generateJButton("targetFolderOpenB", true, null);
			recursiveChB = mls.generateJCheckBox("recursiveChB", true, false, null);
			structureJL = mls.generateJLabel("structureJL", true);
			regexJL = mls.generateJLabel("regexJL", true);
			targetJL = mls.generateJLabel("targetJL", true);
			filenameRB = mls.generateJRadioButton("filenameRB", true, null);
			id3tagRB = mls.generateJRadioButton("id3tagRB", true, null);
			tableT = mls.generateJTable("tableT");
			regexCB = mls.generateJComboBox("regexCB", new ArrayList<String>(), -1, true, true, null);
			structureCB = mls.generateJComboBox("structureCB", new ArrayList<String>(), -1, true, true, null);
			targetFolderTF = mls.generateJTextField("targetFolderTF", true, false, 10, Config.getInstance().getGeneratorTargetFolder());

			ButtonGroup bg = new ButtonGroup();
			bg.add(filenameRB);
			bg.add(id3tagRB);
			filenameRB.setSelected(Config.getInstance().isGeneratorFilenameSelected());
			setRegexEnabled(Config.getInstance().isGeneratorFilenameSelected());
			id3tagRB.setSelected(!Config.getInstance().isGeneratorFilenameSelected());

			recursiveChB.setSelected(Config.getInstance().isGeneratorRecursiveSelected());

			JPanel leftPanel = new JPanel(new MigLayout("insets 0", "[grow]", "[shrink][shrink]"));

			// Structure Panel
			structureP.setLayout(new MigLayout("insets 5", "[shrink][grow][shrink]", "[shrink][shrink]"));
			structureP.add(targetJL);
			structureP.add(targetFolderTF, "grow, gapleft 10");
			int l = (int) targetFolderTF.getPreferredSize().getHeight();
			structureP.add(targetFolderOpenB, "wrap 10, gapleft 10");
			structureP.add(structureJL);
			structureP.add(structureCB, "grow, gapleft 10, height " + l + "!");
			structureP.add(structureHelpIP, "gapleft 10, right, height " + l + "!, width " + l + "!");

			leftPanel.add(structureP, "wrap, grow");

			// info Panel
			infoP.setLayout(new MigLayout("insets 5", "[grow]", "[shrink][shrink]"));
			JPanel rbP = new JPanel(new MigLayout("insets 0", "[grow]", "[shrink][shrink]"));
			rbP.add(id3tagRB, "wrap");
			rbP.add(filenameRB);
			infoP.add(rbP, "wrap, gapbottom 7, grow");
			JPanel regP = new JPanel(new MigLayout("insets 0", "[shrink][grow][shrink]", "[shrink]"));
			regP.add(regexJL);
			regP.add(regexCB, "grow, gapleft 10, height " + l + "!");
			regP.add(regexHelpIP, "gapleft 10, height " + l + "!, width " + l + "!");

			infoP.add(regP, "grow");
			leftPanel.add(infoP, "grow");

			JPanel rightPanel = new JPanel(new MigLayout("insets 0", "[grow]", "[grow][shrink]"));

			// Table Panel
			filesP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
			JScrollPane scrollpane = new JScrollPane();
			filesP.add(scrollpane, "grow, push");
			scrollpane.setViewportView(tableT);

			rightPanel.add(filesP, "grow, wrap");

			// buttons panel
			JPanel btnPanel = new JPanel(new MigLayout("insets 5", "", "[grow]"));
			btnPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			btnPanel.add(addFileB);
			btnPanel.add(addFolderB);
			btnPanel.add(recursiveChB);
			btnPanel.add(deleteFileB);
			btnPanel.add(deleteAllB);
			btnPanel.add(showChangesB, "push, right, gapleft 150");
			btnPanel.add(makeStructureB, "right");

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
		showChangesB.addActionListener(l);
		makeStructureB.addActionListener(l);
		targetFolderOpenB.addActionListener(l);
		recursiveChB.addActionListener(l);
		filenameRB.addActionListener(l);
		id3tagRB.addActionListener(l);

		addFileB.addActionListener(l);
		addFolderB.addActionListener(l);
		deleteFileB.addActionListener(l);
		deleteAllB.addActionListener(l);
	}

	/**
	 * sets the transfer handler to drop files into the table
	 * 
	 * @param handler
	 *            the given transfer handler
	 */
	public void setTableTransferHandler(TransferHandler handler) {
		tableT.setFillsViewportHeight(true);
		tableT.setDragEnabled(true);
		tableT.setTransferHandler(handler);
	}

	/**
	 * sets the mouse listener
	 * 
	 * @param l
	 *            the mouse listener
	 */
	public void setMouseListener(MouseListener l) {
		this.regexHelpIP.addMouseListener(l);
		this.structureHelpIP.addMouseListener(l);
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
	 * shows the message for the given identifier
	 * 
	 * @param identifier
	 *            the given identifier
	 */
	public void showMessage(String identifier) {
		JOptionPane.showMessageDialog(this, mls.getMessage(identifier));
	}

	public void setrecursiveChB(boolean b0) {
		recursiveChB.setSelected(b0);
	}

	public void settargetTF(String s0) {
		targetFolderTF.setText(s0);
	}

	public boolean getrecursiveChB() {
		return recursiveChB.isSelected();
	}

	public String getregexTF() {
		return (String) regexCB.getSelectedItem();
	}

	public String gettargetTF() {
		return targetFolderTF.getText();
	}

	public String getstructureTF() {
		return (String) structureCB.getSelectedItem();
	}

	public boolean getFilenameRB() {
		return this.filenameRB.isSelected();
	}

	public boolean getID3TagRB() {
		return this.id3tagRB.isSelected();
	}

	public JPanel getStructureHelpP() {
		return this.structureHelpIP;
	}

	public JPanel getRegexHelpP() {
		return this.regexHelpIP;
	}

	/**
	 * opens a help dialog for the regex help
	 */
	public void openRegexHelpDialog() {
		HelpDialogView h = new HelpDialogView();
		h.init(this.mls.getMessage("regexHelp"));
		h.setVisible(true);
	}

	/**
	 * opens a help dialog for the structure help
	 */
	public void openStructureHelpDialog() {
		HelpDialogView h = new HelpDialogView();
		h.init(this.mls.getMessage("structureHelp"));
		h.setVisible(true);
	}

	/**
	 * sets the regex label and combobox enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setRegexEnabled(boolean en) {
		this.regexJL.setEnabled(en);
		this.regexCB.setEnabled(en);
	}

	/**
	 * sets the table model and sets the table width
	 * 
	 * @param model
	 *            the table model
	 */
	public void setTableModel(StructureTableModel model) {
		model.setCol1Name(mls.getMessage("Col1"));
		model.setCol2Name(mls.getMessage("Col2"));
		this.tableT.setModel(model);

		this.tableT.getColumnModel().getColumn(0).setPreferredWidth(this.tableWidth[0]);
		this.tableT.getColumnModel().getColumn(1).setPreferredWidth(this.tableWidth[1]);
	}

	/**
	 * saves the current column widths
	 */
	public void saveTableWidth() {
		this.tableWidth[0] = this.tableT.getColumnModel().getColumn(0).getWidth();
		this.tableWidth[1] = this.tableT.getColumnModel().getColumn(1).getWidth();
	}

	public int[] getSelectedIndices() {
		return this.tableT.getSelectedRows();
	}

	/**
	 * sets the masks to regex and structure CB
	 * 
	 * @param masks
	 *            the masks list
	 */
	public void setMasks(List<String> masks) {
		DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
		DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();

		for (String m : masks) {
			model1.addElement(m);
			model2.addElement(m);
		}

		this.regexCB.setModel(model1);
		this.regexCB.setSelectedItem(Config.getInstance().getGeneratorRegex());
		this.structureCB.setModel(model2);
		this.structureCB.setSelectedItem(Config.getInstance().getGeneratorStructure());
	}

	/**
	 * sets all buttons enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setButtonsEnabled(boolean en) {
		this.recursiveChB.setEnabled(en);
		this.makeStructureB.setEnabled(en);
		this.showChangesB.setEnabled(en);
		this.addFileB.setEnabled(en);
		this.addFolderB.setEnabled(en);
		this.deleteFileB.setEnabled(en);
		this.deleteAllB.setEnabled(en);
	}
}