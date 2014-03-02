package view.subview;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import manager.CollectorManager;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class PreferencesView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3745753667177169713L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;
	/**
	 * the contentPane
	 */
	private JPanel contentPane;
	private JTabbedPane tabP;
	private JPanel miscP;
	private JPanel audioPlayerP;
	private JPanel id3TagsP;
	private JPanel collectorsP;
	private JPanel id3dataCollectorsP;
	private JPanel confirmMsgP;
	private JPanel coverCollectorsP;
	private JPanel lyricsCollectorsP;
	private JPanel masksP;
	private JLabel maskJL;
	private JRadioButton defaultPlayerRB;
	private JRadioButton customPlayerRB;
	private JCheckBox maxImageLengthChB;
	private JCheckBox deleteId3v1TagChB;
	private JCheckBox setId3v1TagChB;
	private JCheckBox writeTagsChB;
	private JCheckBox renameFilesChB;
	private JCheckBox generateFolderChB;
	private JCheckBox changeGainChB;
	private JTextField playerCmdTF;
	private JTextField maxImageLengthTF;
	private JTextField maskTF;
	private JList<String> trackListL;
	private JList<String> albumListL;
	private JList<String> lyricsListL;
	private JList<String> masksL;
	private JButton openPlayerB;
	private JButton id3dataUpB;
	private JButton id3dataDownB;
	private JButton id3dataEnableB;
	private JButton coverUpB;
	private JButton coverDownB;
	private JButton coverEnableB;
	private JButton lyricsUpB;
	private JButton lyricsDownB;
	private JButton lyricsEnableB;
	private JButton addMaskB;
	private JButton deleteMaskB;
	private JButton cancelB;
	private JButton okB;

	/**
	 * Constructor
	 */
	public PreferencesView() {
		mls = new MLS("view/languageFiles/PreferencesView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {
		contentPane = new JPanel();

		tabP = mls.generateJTabbedPane("tabP", true);
		miscP = mls.generateJPanel("miscP");
		audioPlayerP = mls.generateTitledBevelPanel("audioPlayerP", BevelBorder.LOWERED);
		id3TagsP = mls.generateTitledBevelPanel("id3TagsP" , BevelBorder.LOWERED);
		confirmMsgP = mls.generateTitledBevelPanel("confirmMsgP" , BevelBorder.LOWERED);
		collectorsP = mls.generateJPanel("collectorsP");
		id3dataCollectorsP = mls.generateTitledBevelPanel("id3dataCollectorsP", BevelBorder.LOWERED);
		coverCollectorsP = mls.generateTitledBevelPanel("coverCollectorsP", BevelBorder.LOWERED);
		lyricsCollectorsP = mls.generateTitledBevelPanel("lyricsCollectorsP", BevelBorder.LOWERED);
		masksP = mls.generateTitledBevelPanel("masksP", BevelBorder.LOWERED);
		maskJL = mls.generateJLabel("maskJL");
		defaultPlayerRB = mls.generateJRadioButton("defaultPlayerRB", true, null);
		customPlayerRB = mls.generateJRadioButton("customPlayerRB", true, null);
		maxImageLengthChB = mls.generateJCheckBox("maxImageLengthChB", Config.getInstance().isUseMaxImageLength());
		deleteId3v1TagChB = mls.generateJCheckBox("deleteId3v1TagChB", Config.getInstance().isDeleteID3v1Tag());
		setId3v1TagChB = mls.generateJCheckBox("setId3v1TagChB", Config.getInstance().isSetID3v1Tag());
		writeTagsChB = mls.generateJCheckBox("writeTagsChB", Config.getInstance().isConfirmWriteTags());
		changeGainChB = mls.generateJCheckBox("changeGainChB", Config.getInstance().isConfirmChangeGain());		
		renameFilesChB = mls.generateJCheckBox("renameFilesChB", Config.getInstance().isConfirmRenameFiles());
		generateFolderChB = mls.generateJCheckBox("generateFolderChB", Config.getInstance().isConfirmFolderGeneration());
		playerCmdTF = mls.generateJTextField("playerCmdTF", true, true, 10, Config.getInstance().getCustomPlayerCmd());
		maxImageLengthTF = mls.generateJTextField("maxImageLengthTF", true, true, 10, Integer.toString(Config.getInstance().getMaxImageLength()));
		maskTF = mls.generateJTextField("maskTF");
		trackListL = mls.generateJList("trackListL", new ArrayList<String>(), -1, true, ListSelectionModel.SINGLE_SELECTION);
		albumListL = mls.generateJList("albumListL", new ArrayList<String>(), -1, true, ListSelectionModel.SINGLE_SELECTION);
		lyricsListL = mls.generateJList("lyricsListL", new ArrayList<String>(), -1, true, ListSelectionModel.SINGLE_SELECTION);
		masksL = mls.generateJList("masksL", new ArrayList<String>(), -1, true, ListSelectionModel.SINGLE_SELECTION);
		openPlayerB = mls.generateJButton("openPlayerB");
		id3dataUpB = mls.generateJButton("id3dataUpB");
		id3dataDownB = mls.generateJButton("id3dataDownB");
		id3dataEnableB = mls.generateJButton("id3dataEnableB");
		coverUpB = mls.generateJButton("coverUpB");
		coverDownB = mls.generateJButton("coverDownB");
		coverEnableB = mls.generateJButton("coverEnableB");
		lyricsUpB = mls.generateJButton("lyricsUpB");
		lyricsDownB = mls.generateJButton("lyricsDownB");
		lyricsEnableB = mls.generateJButton("lyricsEnableB");
		addMaskB = mls.generateJButton("addMaskB");
		deleteMaskB = mls.generateJButton("deleteMaskB");
		cancelB = mls.generateJButton("cancelB");
		okB = mls.generateJButton("okB");

		// set button groups
		ButtonGroup bg = new ButtonGroup();
		bg.add(defaultPlayerRB);
		bg.add(customPlayerRB);
		defaultPlayerRB.setSelected(!Config.getInstance().isUseCustomPlayer());
		customPlayerRB.setSelected(Config.getInstance().isUseCustomPlayer());

		setContentPane(contentPane);

		// Misc Panel
		miscP.setLayout(new MigLayout("insets 5", "[grow]", "[shrink][shrink][grow]"));
		// // audio panel
		audioPlayerP.setLayout(new MigLayout("insets 5", "[shrink][grow][shrink]", "[shrink][shrink]"));
		audioPlayerP.add(defaultPlayerRB, "span 3, wrap");
		audioPlayerP.add(customPlayerRB);
		audioPlayerP.add(playerCmdTF, "grow");
		audioPlayerP.add(openPlayerB);
		// // id3tags panel
		id3TagsP.setLayout(new MigLayout("insets 5", "[shrink][grow]", "[shrink][shrink][shrink]"));
		id3TagsP.add(maxImageLengthChB);
		id3TagsP.add(maxImageLengthTF, "grow, wrap");
		id3TagsP.add(deleteId3v1TagChB, "wrap, span 2");
		id3TagsP.add(setId3v1TagChB, "span 2");
		// // confirmation messages
		confirmMsgP.setLayout(new MigLayout("insets 5", "[grow]", "[shrink][shrink][shrink][shrink]"));
		confirmMsgP.add(writeTagsChB, "wrap");
		confirmMsgP.add(renameFilesChB, "wrap");
		confirmMsgP.add(generateFolderChB, "wrap");
		confirmMsgP.add(changeGainChB);

		miscP.add(audioPlayerP, "wrap, grow");
		miscP.add(id3TagsP, "grow, wrap");
		miscP.add(confirmMsgP, "grow");

		// Collectors
		collectorsP.setLayout(new MigLayout("insets 0", "[grow]", "[grow][grow][grow]"));
		// // track collector
		id3dataCollectorsP.setLayout(new MigLayout("insets 5", "[grow][shrink]", "[grow]"));
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(trackListL);
		JPanel trackBtnP = new JPanel(new MigLayout("insets 0", "[shrink]", "[shrink][shrink][shrink]"));
		trackBtnP.add(id3dataUpB, "wrap");
		trackBtnP.add(id3dataDownB, "wrap");
		trackBtnP.add(id3dataEnableB);
		id3dataCollectorsP.add(sp, "grow, height :70:");
		id3dataCollectorsP.add(trackBtnP, "grow");
		// // album collector
		coverCollectorsP.setLayout(new MigLayout("insets 5", "[grow][shrink]", "[grow]"));
		JScrollPane sp2 = new JScrollPane();
		sp2.setViewportView(albumListL);
		JPanel albumBtnP = new JPanel(new MigLayout("insets 0", "[shrink]", "[shrink][shrink][shrink]"));
		albumBtnP.add(coverUpB, "wrap");
		albumBtnP.add(coverDownB, "wrap");
		albumBtnP.add(coverEnableB);
		coverCollectorsP.add(sp2, "grow, height :70:");
		coverCollectorsP.add(albumBtnP, "grow");
		// // lyrics collector
		lyricsCollectorsP.setLayout(new MigLayout("insets 5", "[grow][shrink]", "[grow]"));
		JScrollPane sp3 = new JScrollPane();
		sp3.setViewportView(lyricsListL);
		JPanel lyricsBtnP = new JPanel(new MigLayout("insets 0", "[shrink]", "[shrink][shrink][shrink]"));
		lyricsBtnP.add(lyricsUpB, "wrap");
		lyricsBtnP.add(lyricsDownB, "wrap");
		lyricsBtnP.add(lyricsEnableB);
		lyricsCollectorsP.add(sp3, "grow, height :70:");
		lyricsCollectorsP.add(lyricsBtnP, "grow");

		collectorsP.add(id3dataCollectorsP, "grow, wrap");
		collectorsP.add(coverCollectorsP, "grow, wrap");
		collectorsP.add(lyricsCollectorsP, "grow");

		// Masks
		// // Masks
		masksP.setLayout(new MigLayout("insets 5", "[shrink][grow]", "[grow]"));
		JPanel maskInputP = new JPanel(new MigLayout("insets 0", "[shrink][grow]", "[shrink][shrink][shrink]"));
		maskInputP.add(maskJL);
		maskInputP.add(maskTF, "grow, wrap");
		maskInputP.add(new JLabel());
		maskInputP.add(addMaskB, "wrap, grow");
		maskInputP.add(new JLabel());
		maskInputP.add(deleteMaskB, "grow");

		JScrollPane sp4 = new JScrollPane();
		sp4.setViewportView(masksL);
		masksP.add(maskInputP, "grow");
		masksP.add(sp4, "grow");

		// ok and cancel btn
		JPanel btnP = new JPanel(new MigLayout("insets 0", "[shrink][shrink]", "[shrink]"));
		btnP.add(cancelB, "push, right");
		btnP.add(okB, "right");

		mls.addTab(tabP, miscP, "miscTab");
		mls.addTab(tabP, collectorsP, "collectorsTab");
		mls.addTab(tabP, masksP, "masksTab");

		contentPane.setLayout(new MigLayout("insets 5", "[grow]", "[grow][shrink]"));
		contentPane.add(tabP, "grow, wrap");
		contentPane.add(btnP, "grow");

		pack();
	}

	/**
	 * shows a given message
	 * 
	 * @param identifier
	 *            identifier for the translator
	 */
	public void showMessage(String identifier) {
		JOptionPane.showMessageDialog(this, mls.getMessage(identifier));
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		openPlayerB.addActionListener(l);
		id3dataUpB.addActionListener(l);
		id3dataDownB.addActionListener(l);
		id3dataEnableB.addActionListener(l);
		coverUpB.addActionListener(l);
		coverDownB.addActionListener(l);
		coverEnableB.addActionListener(l);
		lyricsUpB.addActionListener(l);
		lyricsDownB.addActionListener(l);
		lyricsEnableB.addActionListener(l);
		addMaskB.addActionListener(l);
		deleteMaskB.addActionListener(l);
		cancelB.addActionListener(l);
		okB.addActionListener(l);

		setId3v1TagChB.addActionListener(l);
		deleteId3v1TagChB.addActionListener(l);
	}

	/**
	 * saves the made settings
	 */
	public void saveConfig() {
		Config.getInstance().setUseCustomPlayer(customPlayerRB.isSelected());
		Config.getInstance().setCustomPlayerCmd(playerCmdTF.getText());
		Config.getInstance().setUseMaxImageLength(maxImageLengthChB.isSelected());
		Config.getInstance().setMaxImageLength(maxImageLengthTF.getText());
		Config.getInstance().setDeleteID3v1Tag(deleteId3v1TagChB.isSelected());
		Config.getInstance().setSetID3v1Tag(setId3v1TagChB.isSelected());
		Config.getInstance().setConfirmWriteTags(writeTagsChB.isSelected());
		Config.getInstance().setConfirmRenameFiles(renameFilesChB.isSelected());
		Config.getInstance().setConfirmFolderGeneration(generateFolderChB.isSelected());
		Config.getInstance().setConfirmChangeGain(changeGainChB.isSelected());
	}

	public boolean isDeleteId3v1Tag() {
		return this.deleteId3v1TagChB.isSelected();
	}

	public void setDeleteId3v1Tag(boolean set) {
		this.deleteId3v1TagChB.setSelected(set);
	}

	public boolean isSetId3v1Tag() {
		return this.setId3v1TagChB.isSelected();
	}

	public void setSetId3v1Tag(boolean set) {
		this.setId3v1TagChB.setSelected(set);
	}

	public void setPlayerCmd(String cmd) {
		this.playerCmdTF.setText(cmd);
	}

	/**
	 * sets the collectors
	 * 
	 * @param collectors
	 *            the collectors
	 * @param type
	 *            the collector ID
	 */
	public void setCollectors(List<String> collectors, int type) {
		DefaultListModel<String> model = new DefaultListModel<>();

		for (String e : collectors)
			model.addElement(e);

		if (type == CollectorManager.ID3DATA_COLLECTOR)
			this.trackListL.setModel(model);
		else if (type == CollectorManager.COVER_COLLECTOR)
			this.albumListL.setModel(model);
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			this.lyricsListL.setModel(model);
	}

	/**
	 * gets the selected collector
	 * 
	 * @param type
	 *            collector ID
	 * 
	 * @return the selected collector Index
	 */
	public int getSelectedCollectorIndex(int type) {
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			return this.trackListL.getSelectedIndex();
		else if (type == CollectorManager.COVER_COLLECTOR)
			return this.albumListL.getSelectedIndex();
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			return this.lyricsListL.getSelectedIndex();

		return -1;
	}

	/**
	 * sets the selectec collector
	 * 
	 * @param index
	 *            the selected index
	 * @param type
	 *            the collector ID
	 */
	public void setSelectedCollectorIndex(int index, int type) {
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			this.trackListL.setSelectedIndex(index);
		else if (type == CollectorManager.COVER_COLLECTOR)
			this.albumListL.setSelectedIndex(index);
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			this.lyricsListL.setSelectedIndex(index);
	}

	/**
	 * sets the masks
	 * 
	 * @param masks
	 *            the masks
	 */
	public void setMasks(List<String> masks) {
		DefaultListModel<String> model = new DefaultListModel<>();

		for (String m : masks)
			model.addElement(m);

		this.masksL.setModel(model);
	}

	public int getSelectedMaskIndex() {
		return this.masksL.getSelectedIndex();
	}

	public String getMaskTF() {
		return this.maskTF.getText();
	}

	public void setMaskTF(String txt) {
		this.maskTF.setText(txt);
	}
}