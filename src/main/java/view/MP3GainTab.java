package view;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import model.exception.TabInitException;
import model.table.GainTableModel;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class MP3GainTab extends AbstractTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923738221946214668L;

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

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
	private JButton startTrackGainB;
	private JButton startAnalyseB;
	private JButton addFolderB;
	private JCheckBox forceRecalcChB;
	private JCheckBox recursiveChB;
	private JLabel targetVolJL;
	private JLabel dbJL;
	private JPanel gainTypeP;
	private JPanel analyseP;
	private JPanel changeGainP;
	private JPanel filesP;
	private JPanel miscP;
	private JRadioButton analyseAllRB;
	private JRadioButton analyseSelectedRB;
	private JRadioButton changeGainSelectedRB;
	private JRadioButton changeGainAllB;
	private JRadioButton albumGainRB;
	private JRadioButton trackGainRB;
	private JTable tableT;
	private JTextField targetVolTF;

	/**
	 * Constructor
	 */
	public MP3GainTab() {
		mls = new MLS("view/languageFiles/MP3GainTab", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
		this.tableWidth = new int[] { 100, 100, 50, 50, 50, 50 };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see view.interfaces.AbstractTab#init()
	 */
	@Override
	public void init() throws TabInitException {

		gainTypeP = mls.generateTitledBevelPanel("gainTypeP", BevelBorder.LOWERED, true);
		analyseP = mls.generateTitledBevelPanel("analyseP", BevelBorder.LOWERED, true);
		changeGainP = mls.generateTitledBevelPanel("changeGainP", BevelBorder.LOWERED, true);
		filesP = mls.generateTitledBevelPanel("filesP", BevelBorder.LOWERED, true);
		miscP = mls.generateTitledBevelPanel("miscP", BevelBorder.LOWERED, true);
		addFileB = mls.generateJButton("addFileB", true, null);
		deleteAllB = mls.generateJButton("deleteAllB", true, null);
		deleteFileB = mls.generateJButton("deleteFileB", true, null);
		startTrackGainB = mls.generateJButton("startTrackGainB", true, null);
		startAnalyseB = mls.generateJButton("startAnalyseB", true, null);
		addFolderB = mls.generateJButton("addFolderB", true, null);
		targetVolJL = mls.generateJLabel("targetVolJL", true);
		dbJL = mls.generateJLabel("dbJL", true);
		analyseAllRB = mls.generateJRadioButton("analyseAllRB", true, null);
		analyseSelectedRB = mls.generateJRadioButton("analyseSelectedRB", true, null);
		changeGainSelectedRB = mls.generateJRadioButton("changeGainSelectedRB", true, null);
		changeGainAllB = mls.generateJRadioButton("changeGainAllB", true, null);
		albumGainRB = mls.generateJRadioButton("albumGainRB", true, null);
		trackGainRB = mls.generateJRadioButton("trackGainRB", true, null);
		forceRecalcChB = mls.generateJCheckBox("forceRecalcChB", true, false, null);
		recursiveChB = mls.generateJCheckBox("recursiveChB", true, false, null);
		tableT = mls.generateJTable("tableT");
		targetVolTF = mls.generateJTextField("targetVolTF", true, true, 10, Config.getInstance().getMP3GainTarget());

		ButtonGroup b1 = new ButtonGroup();
		b1.add(analyseSelectedRB);
		b1.add(analyseAllRB);
		analyseSelectedRB.setSelected(Config.getInstance().isMP3gainAnalyseSelected());
		analyseAllRB.setSelected(!Config.getInstance().isMP3gainAnalyseSelected());

		ButtonGroup b2 = new ButtonGroup();
		b2.add(changeGainSelectedRB);
		b2.add(changeGainAllB);
		changeGainSelectedRB.setSelected(Config.getInstance().isMP3gainChangeSelected());
		changeGainAllB.setSelected(!Config.getInstance().isMP3gainChangeSelected());

		ButtonGroup b3 = new ButtonGroup();
		b3.add(trackGainRB);
		b3.add(albumGainRB);
		trackGainRB.setSelected(Config.getInstance().isMP3GainTrackType());
		albumGainRB.setSelected(!Config.getInstance().isMP3GainTrackType());

		forceRecalcChB.setSelected(Config.getInstance().isMP3GainForceSelected());
		recursiveChB.setSelected(Config.getInstance().isMP3GainRecursiveSelected());

		// first row
		JPanel firstLineP = new JPanel(new MigLayout("insets 0", "[][][][grow]", ""));

		// analyse Panel
		analyseP.setLayout(new MigLayout());
		analyseP.add(analyseSelectedRB);
		analyseP.add(startAnalyseB, "span 1 2, wrap");
		analyseP.add(analyseAllRB);

		// Gain Panel
		changeGainP.setLayout(new MigLayout());
		changeGainP.add(changeGainSelectedRB);
		changeGainP.add(startTrackGainB, "span 1 2, wrap");
		changeGainP.add(changeGainAllB);

		// gain type Panel
		gainTypeP.setLayout(new MigLayout());
		gainTypeP.add(trackGainRB, "wrap");
		gainTypeP.add(albumGainRB);

		// Target Volume
		miscP.setLayout(new MigLayout("", "[center]", "[center]"));
		miscP.add(targetVolJL);
		miscP.add(targetVolTF, "width :50:");
		miscP.add(dbJL, "wrap");
		miscP.add(forceRecalcChB);

		firstLineP.add(analyseP, "height :95:");
		firstLineP.add(changeGainP, "height :95:");
		firstLineP.add(gainTypeP, "height :95:");
		firstLineP.add(miscP, "grow");

		// Table Panel
		filesP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		JScrollPane scrollpane = new JScrollPane();
		filesP.add(scrollpane, "grow");
		scrollpane.setViewportView(tableT);

		// last add row
		JPanel addP = new JPanel(new MigLayout("insets 5"));
		addP.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		addP.add(addFileB);
		addP.add(addFolderB);
		addP.add(recursiveChB);
		addP.add(deleteFileB);
		addP.add(deleteAllB);

		setLayout(new MigLayout("insets 0", "[grow]", "[shrink][grow][shrink]"));

		add(firstLineP, "wrap");
		add(filesP, "grow, wrap");
		add(addP, "grow");
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		addFileB.addActionListener(l);
		deleteAllB.addActionListener(l);
		deleteFileB.addActionListener(l);
		startTrackGainB.addActionListener(l);
		startAnalyseB.addActionListener(l);
		addFolderB.addActionListener(l);
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
	 * sets a FocusListener
	 * 
	 * @param l
	 *            the FocusListener
	 */
	public void setFocusListener(FocusListener l) {
		targetVolTF.addFocusListener(l);
		;
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
	 * shows a given message
	 * 
	 * @param identifier
	 *            identifier for the mls
	 * @param replacer
	 *            the replacement for {0}
	 */
	public void showMessage(String identifier, String replacer) {
		JOptionPane.showMessageDialog(this, mls.getMessage(identifier).replace("{0}", replacer));
	}

	/**
	 * shows a given message and asks for yes or no
	 * 
	 * @param identifier
	 *            identifier for the translator
	 * 
	 * @return the JOPtionPane.YES or NO answer
	 */
	public int showConfirmationMessage(String identifier) {
		return JOptionPane.showConfirmDialog(this, this.mls.getMessage(identifier));
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

	public void setforceRecalcChB(boolean b0) {
		forceRecalcChB.setSelected(b0);
	}

	public void setrecursiveChB(boolean b0) {
		recursiveChB.setSelected(b0);
	}

	public void settargetVolTF(String s0) {
		targetVolTF.setText(s0);
	}

	public boolean getforceRecalcChB() {
		return forceRecalcChB.isSelected();
	}

	public boolean getrecursiveChB() {
		return recursiveChB.isSelected();
	}

	public String gettargetVolTF() {
		return targetVolTF.getText();
	}

	public int getTargetVolume() throws NumberFormatException {
		return Integer.parseInt(targetVolTF.getText());
	}

	public boolean isAnalyseSelectAll() {
		return this.analyseAllRB.isSelected();
	}

	public boolean isChangeGainSelectAll() {
		return this.changeGainAllB.isSelected();
	}

	public boolean isTrackGainSelected() {
		return this.trackGainRB.isSelected();
	}

	/**
	 * sets the table model and sets the current column width
	 * 
	 * @param model
	 *            the table model
	 */
	public void setTableModel(GainTableModel model) {
		for (int i = 1; i <= 6; i++)
			model.setColName(i, mls.getMessage("Col" + i));

		this.tableT.setModel(model);

		for (int i = 0; i < 6; i++)
			this.tableT.getColumnModel().getColumn(i).setPreferredWidth(this.tableWidth[i]);
	}

	/**
	 * saves the column width
	 */
	public void saveTableWidth() {
		this.tableWidth[0] = this.tableT.getColumnModel().getColumn(0).getWidth();
		this.tableWidth[1] = this.tableT.getColumnModel().getColumn(1).getWidth();
		this.tableWidth[2] = this.tableT.getColumnModel().getColumn(2).getWidth();
		this.tableWidth[3] = this.tableT.getColumnModel().getColumn(3).getWidth();
		this.tableWidth[4] = this.tableT.getColumnModel().getColumn(4).getWidth();
		this.tableWidth[5] = this.tableT.getColumnModel().getColumn(5).getWidth();

		logger.log(Level.FINER, "widths: col1=" + this.tableWidth[0] + " col2=" + this.tableWidth[1] + " col3=" + this.tableWidth[2] + " col4=" + this.tableWidth[3] + " col5="
				+ this.tableWidth[4] + " col6=" + this.tableWidth[5]);
	}

	public int[] getSelectedIndices() {
		return this.tableT.getSelectedRows();
	}

	/**
	 * sets all buttons enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setButtonsEnabled(boolean en) {
		this.targetVolTF.setEnabled(en);
		this.addFileB.setEnabled(en);
		this.addFolderB.setEnabled(en);
		this.deleteFileB.setEnabled(en);
		this.deleteAllB.setEnabled(en);
		this.recursiveChB.setEnabled(en);
		this.startAnalyseB.setEnabled(en);
		this.startTrackGainB.setEnabled(en);
	}
}