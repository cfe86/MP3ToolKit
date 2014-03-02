package view;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import view.interfaces.AbstractTab;
import view.subview.id3.ID3RightPanel;
import model.audio.interfaces.IAudioFile;
import model.exception.TabInitException;
import model.structure.ID3TagData;
import model.table.ID3TagTableModel;
import model.util.WindowUtils;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class ID3TagTab extends AbstractTab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923738221946214668L;

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
	private JLabel rootJL;
	private JPanel foldersP;
	private JPanel rootPanelP;
	private JPanel tableP;
	private ID3RightPanel rightPanelP;
	private JButton deleteAllB;
	private JButton deleteFileB;
	private JButton saveTagsB;
	private JButton addFileB;
	private JButton addFolderB;
	private JButton rootOpenB;
	private JCheckBox autoAddChB;
	private JCheckBox recursiveChB;
	private JButton loadB;
	private JRadioButton saveSelectedRB;
	private JRadioButton saveAllChangeRB;
	private JTable tableT;
	private JTextField rootTF;
	private JTree filesTr;

	/**
	 * Constructor
	 */
	public ID3TagTab() {
		mls = new MLS("view/languageFiles/ID3TagTab", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
		this.tableWidth = new int[] { 55, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see view.interfaces.AbstractTab#init()
	 */
	@Override
	public void init() throws TabInitException {

		deleteAllB = mls.generateJButton("deleteAllB", true, null);
		deleteFileB = mls.generateJButton("deleteFileB", true, null);
		saveTagsB = mls.generateJButton("saveTagsB", true, null);
		addFileB = mls.generateJButton("addFileB", true, null);
		addFolderB = mls.generateJButton("addFolderB", true, null);
		rootOpenB = mls.generateJButton("rootOpenB", true, null);
		loadB = mls.generateJButton("loadB", true, null);

		autoAddChB = mls.generateJCheckBox("autoAddChB", true, false, null);
		recursiveChB = mls.generateJCheckBox("recursiveChB", true, false, null);

		rootJL = mls.generateJLabel("rootJL", true);

		foldersP = mls.generateTitledBevelPanel("foldersP", BevelBorder.LOWERED, true);
		rootPanelP = mls.generateTitledBevelPanel("rootPanelP", BevelBorder.LOWERED, true);
		tableP = mls.generateTitledBevelPanel("tableP", BevelBorder.LOWERED, true);

		saveAllChangeRB = mls.generateJRadioButton("saveAllChangeRB", true, null);
		saveSelectedRB = mls.generateJRadioButton("saveSelectedRB", true, null);

		tableT = mls.generateJTable("tableT");

		rootTF = mls.generateJTextField("rootTF", true, false, 10, "");

		filesTr = mls.generateJTree("filesTr", true);

		// set radiobuttons and checkboxes
		setLoadButtonEnabled(!Config.getInstance().isID3AutoAdd());
		this.recursiveChB.setSelected(Config.getInstance().isID3Recursive());

		ButtonGroup bg = new ButtonGroup();
		bg.add(saveAllChangeRB);
		bg.add(saveSelectedRB);
		this.saveAllChangeRB.setSelected(Config.getInstance().isID3AllChanged());
		this.saveSelectedRB.setSelected(!Config.getInstance().isID3AllChanged());

		tableT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane scrollPane2 = new JScrollPane();
		JScrollPane scrollPane = new JScrollPane();

		// left Tree Panel
		JPanel leftPanel = new JPanel(new MigLayout("insets 0", "[grow]", "[shrink][grow][shrink]"));

		// root panel
		rootPanelP.setLayout(new MigLayout("insets 5", "[shrink][grow][shrink]", "[shrink]"));
		rootPanelP.add(rootJL);
		rootPanelP.add(rootTF, "grow, gapleft 10");
		rootPanelP.add(rootOpenB, "gapleft 10");

		leftPanel.add(rootPanelP, "wrap, grow");

		// tree
		foldersP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		scrollPane.setViewportView(filesTr);
		foldersP.add(scrollPane, "grow");

		leftPanel.add(foldersP, "wrap, grow");

		// button panel
		JPanel loadPanel = new JPanel(new MigLayout("insets 5", "[grow][shrink]", "[shrink][shrink]"));
		loadPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		loadPanel.add(recursiveChB, "grow");
		loadPanel.add(loadB, "span 2 2, wrap");
		loadPanel.add(autoAddChB, "grow");

		leftPanel.add(loadPanel, "grow");

		// middle Table Panel
		JPanel middlePanel = new JPanel(new MigLayout("insets 0", "[grow]", "[grow][shrink]"));

		tableP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		scrollPane2.setViewportView(tableT);
		tableP.add(scrollPane2, "grow");

		middlePanel.add(tableP, "grow, wrap");

		// button panel
		JPanel addPanel = new JPanel(new MigLayout("insets 5", "[shrink][shrink][shrink][shrink][shrink][shrink]", "[shrink]"));
		addPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		addPanel.add(addFileB);
		addPanel.add(addFolderB);
		addPanel.add(deleteFileB, "gapleft 10");
		addPanel.add(deleteAllB, "push");
		// addPanel.add(playAudioB, "gapleft 10, push");
		JPanel checkBoxP = new JPanel(new MigLayout("insets 0"));
		checkBoxP.add(saveAllChangeRB, "wrap");
		checkBoxP.add(saveSelectedRB, "wrap");
		addPanel.add(checkBoxP);
		addPanel.add(saveTagsB);

		middlePanel.add(addPanel, "grow");

		// create Splitpane for left and middle panel
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, middlePanel);

		rightPanelP = new ID3RightPanel(mls);
		rightPanelP.init();

		setLayout(new MigLayout("insets 0", "[grow][shrink]", "[grow]"));
		add(splitPane, "grow");
		add(rightPanelP, "grow");
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		rootOpenB.addActionListener(l);

		deleteAllB.addActionListener(l);
		deleteFileB.addActionListener(l);
		addFileB.addActionListener(l);
		addFolderB.addActionListener(l);

		loadB.addActionListener(l);
		saveTagsB.addActionListener(l);

		autoAddChB.addActionListener(l);

		rightPanelP.setActionListener(l);
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
	 * sets the transfer handler to drop files into the table
	 * 
	 * @param handler
	 *            the given transfer handler
	 */
	public void setTreeTransferHandler(TransferHandler handler) {
		filesTr.setDragEnabled(true);
		filesTr.setTransferHandler(handler);
	}

	/**
	 * sets a FocusListener
	 * 
	 * @param l
	 *            the FocusListener
	 */
	public void setFocusListener(FocusListener l) {
		rightPanelP.setFocusListener(l);
	}

	/**
	 * sets a ChangeListener
	 * 
	 * @param l
	 *            the ChangeListener
	 */
	public void setChangeListener(ChangeListener l) {
		rightPanelP.setChangeListener(l);
	}

	/**
	 * sets a TreeSelectionListener
	 * 
	 * @param l
	 *            the TreeSelectionListener
	 */
	public void setTreeSelectionListener(TreeSelectionListener l) {
		this.filesTr.addTreeSelectionListener(l);
	}

	/**
	 * sets a ListSelectionListener
	 * 
	 * @param l
	 *            the ListSelectionListener
	 */
	public void setListSelectionListener(ListSelectionListener l) {
		this.tableT.getSelectionModel().addListSelectionListener(l);
	}

	/**
	 * removes the ListSelectionListener
	 * 
	 * @param l
	 *            the ListSelectionListener
	 */
	public void removeListSelectionListener(ListSelectionListener l) {
		this.tableT.getSelectionModel().removeListSelectionListener(l);
	}

	/**
	 * resets all RadionButtons to unselected
	 */
	public void resetRB() {
		rightPanelP.resetRB();
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

	public void setautoAddChB(boolean b0) {
		autoAddChB.setSelected(b0);
	}

	public void setrecursiveChB(boolean b0) {
		recursiveChB.setSelected(b0);
	}

	public void setrootTF(String s0) {
		rootTF.setText(s0);
	}

	public String getLyricsTA() {
		return rightPanelP.getLyricsTA();
	}

	public void setLyricsTA(String s0) {
		rightPanelP.setLyricsTA(s0);
	}

	public boolean getautoAddChB() {
		return autoAddChB.isSelected();
	}

	public boolean getrecursiveChB() {
		return recursiveChB.isSelected();
	}

	public String getrootTF() {
		return rootTF.getText();
	}

	public boolean getSaveAllChangedRB() {
		return this.saveAllChangeRB.isSelected();
	}

	public String geturlTF() {
		return rightPanelP.geturlTF();
	}

	public String getalbumArtistTF() {
		return rightPanelP.getalbumArtistTF();
	}

	public String getmaxTracksTF() {
		return rightPanelP.getmaxTracksTF();
	}

	public String getcopyrightTF() {
		return rightPanelP.getcopyrightTF();
	}

	public String getcomposerTF() {
		return rightPanelP.getcomposerTF();
	}

	public String getalbumTF() {
		return rightPanelP.getalbumTF();
	}

	public String getcommentTF() {
		return rightPanelP.getcommentTF();
	}

	public String getyearTF() {
		return rightPanelP.getyearTF();
	}

	public String gettitleTF() {
		return rightPanelP.gettitleTF();
	}

	public String getorigArtistTF() {
		return rightPanelP.getorigArtistTF();
	}

	public String getencodedByTF() {
		return rightPanelP.getencodedByTF();
	}

	public String getartistTF() {
		return rightPanelP.getartistTF();
	}

	public String getTrackNr() {
		return rightPanelP.getTrackNr();
	}

	public String getPublisherTF() {
		return rightPanelP.getPublisherTF();
	}

	public String getMaxTracks() {
		return rightPanelP.getMaxTracks();
	}

	public String getCurrCD() {
		return rightPanelP.getCurrCD();
	}

	public String getMaxCD() {
		return rightPanelP.getMaxCD();
	}

	/**
	 * sets the given audio file data to the interface
	 * 
	 * @param audioFile
	 *            given audio file
	 * 
	 * @throws IOException
	 */
	public void setAudioFileData(IAudioFile audioFile) throws IOException {
		rightPanelP.setID3Data(audioFile);
	}

	/**
	 * resets all id3 data fields to its default value
	 */
	public void resetAudioFileData() {
		rightPanelP.resetID3Data();
	}

	/**
	 * gets the ID3Tag Data as shown in the interface
	 * 
	 * @return all ID3Tag Data
	 */
	public ID3TagData getID3TagData() {
		return rightPanelP.getID3TagData();
	}

	/**
	 * sets the genre combobox
	 * 
	 * @param genres
	 *            all genres
	 */
	public void setGenres(String[] genres) {
		rightPanelP.setGenres(genres);
	}

	/**
	 * gets the selected genre index. does NOT start by 0
	 * 
	 * @return the genre
	 */
	public String getGenre() {
		return rightPanelP.getGenre();
	}

	/**
	 * sets the load button enabled or disabled
	 * 
	 * @param en
	 *            enabled true, else false
	 */
	public void setLoadButtonEnabled(boolean en) {
		this.autoAddChB.setSelected(!en);
		this.loadB.setEnabled(en);
	}

	/**
	 * updates the shown tree with the given root
	 * 
	 * @param root
	 *            given root
	 */
	public void updateTree(String root) {
		WindowUtils.updateBrowserTree(this.filesTr, root, true, false);
		this.filesTr.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * gets all selected audio files
	 * 
	 * @return indices of all selected audio files
	 */
	public int[] getSelectedFiles() {
		return this.tableT.getSelectedRows();
	}

	/**
	 * sets the selected audio file
	 * 
	 * @param i
	 *            index of the selected mpe3
	 */
	public void setSelectedFile(int i) {
		this.tableT.getSelectionModel().setSelectionInterval(i, i);
	}

	/**
	 * sets the selected files
	 * 
	 * @param indices
	 *            indices of all selected audio files
	 */
	public void setSelectedFiles(int[] indices) {
		for (int i = 0; i < indices.length; i++)
			this.tableT.getSelectionModel().addSelectionInterval(indices[i], indices[i]);
	}

	/**
	 * gets the Tree object
	 */
	public JTree getFilesTree() {
		return this.filesTr;
	}

	/**
	 * sets the table model to update the table
	 * 
	 * @param model
	 *            table model
	 */
	public void setTableModel(ID3TagTableModel model) {
		// set col names
		for (int i = 0; i < 17; i++) {
			model.setColumnName(i, mls.getMessage("Col" + (i + 1)));
		}
		this.tableT.setModel(model);

		for (int i = 0; i < 17; i++)
			this.tableT.getColumnModel().getColumn(i).setPreferredWidth(this.tableWidth[i]);
	}

	/**
	 * saves the widths of the current table
	 */
	public void saveTableWidth() {
		for (int i = 0; i < 17; i++)
			this.tableWidth[i] = this.tableT.getColumnModel().getColumn(i).getWidth();
	}

	/**
	 * sets the cover image data
	 * 
	 * @param imgBytes
	 *            bytes of the image
	 * @param extension
	 *            image extension
	 * 
	 * @throws IOException
	 */
	public void setCoverImageData(byte[] imgBytes, String extension) throws IOException {
		rightPanelP.setCoverImageData(imgBytes, extension);
	}

	/**
	 * gets the image size
	 * 
	 * @return the image size
	 */
	public int[] getImageSize() {
		return rightPanelP.getImageSize();
	}

	/**
	 * sets all buttons enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setButtonsEnabled(boolean en) {
		this.addFileB.setEnabled(en);
		this.addFolderB.setEnabled(en);
		this.deleteFileB.setEnabled(en);
		this.deleteAllB.setEnabled(en);
		this.recursiveChB.setEnabled(en && !autoAddChB.isSelected());
		
		this.loadB.setEnabled(en);
		this.saveTagsB.setEnabled(en);
		
		this.rightPanelP.setButtonsEnabled(en);
	}
}