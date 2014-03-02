package view.subview.id3;

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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import model.structure.ConvertElement;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class FieldReplacerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1146029435288109999L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;
	private JButton deleteB;
	private JButton startB;
	private JButton cancelB;
	private JButton addB;
	private JCheckBox origArtistChB;
	private JCheckBox albumArtistChB;
	private JCheckBox albumChB;
	private JCheckBox composerChB;
	private JCheckBox artistChB;
	private JCheckBox copyrightChB;
	private JCheckBox yearChB;
	private JCheckBox cdChB;
	private JCheckBox commentChB;
	private JCheckBox urlChB;
	private JCheckBox trackChB;
	private JCheckBox titleChB;
	private JCheckBox selectAllChB;
	private JCheckBox maxCDChB;
	private JCheckBox encoderChB;
	private JCheckBox maxTrackChB;
	private JCheckBox lyricsChB;
	private JCheckBox genreChB;
	private JLabel toJL;
	private JList<ConvertElement> listL;
	private JPanel processP;
	private JPanel fieldsP;
	private JRadioButton removeSpaceRB;
	private JRadioButton firstUppercaseRB;
	private JRadioButton allUppercaseRB;
	private JRadioButton spaceToUnderscoreRB;
	private JRadioButton convertRB;
	private JRadioButton underscoreToSpaceRB;
	private JRadioButton allLowercaseRB;
	private JRadioButton replaceAllRB;
	private JRadioButton replaceSelectedRB;
	private JTextField convertToTF;
	private JTextField convertFromTF;

	/**
	 * Constructor
	 */
	public FieldReplacerView() {
		mls = new MLS("view/languageFiles/FieldReplacerView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {
		mls.addJFrame("window", this);

		processP = mls.generateTitledBevelPanel("processP", BevelBorder.LOWERED);
		fieldsP = mls.generateTitledBevelPanel("fieldsP", BevelBorder.LOWERED);
		deleteB = mls.generateJButton("deleteB", true, null);
		startB = mls.generateJButton("startB", true, null);
		cancelB = mls.generateJButton("cancelB", true, null);
		addB = mls.generateJButton("addB", true, null);
		origArtistChB = mls.generateJCheckBox("origArtistChB", true, false, null);
		albumArtistChB = mls.generateJCheckBox("albumArtistChB", true, false, null);
		albumChB = mls.generateJCheckBox("albumChB", true, false, null);
		composerChB = mls.generateJCheckBox("composerChB", true, false, null);
		artistChB = mls.generateJCheckBox("artistChB", true, false, null);
		copyrightChB = mls.generateJCheckBox("copyrightChB", true, false, null);
		yearChB = mls.generateJCheckBox("yearChB", true, false, null);
		cdChB = mls.generateJCheckBox("cdChB", true, false, null);
		commentChB = mls.generateJCheckBox("commentChB", true, false, null);
		urlChB = mls.generateJCheckBox("urlChB", true, false, null);
		trackChB = mls.generateJCheckBox("trackChB", true, false, null);
		titleChB = mls.generateJCheckBox("titleChB", true, false, null);
		selectAllChB = mls.generateJCheckBox("selectAllChB", true, false, null);
		maxCDChB = mls.generateJCheckBox("maxCDChB", true, false, null);
		encoderChB = mls.generateJCheckBox("encoderChB", true, false, null);
		maxTrackChB = mls.generateJCheckBox("maxTrackChB", true, false, null);
		lyricsChB = mls.generateJCheckBox("lyricsChB", true, false, null);
		genreChB = mls.generateJCheckBox("genreChB", true, false, null);
		toJL = mls.generateJLabel("toJL", true);
		listL = mls.generateJList("listL", new ConvertElement[] {}, -1, true, ListSelectionModel.SINGLE_SELECTION);
		removeSpaceRB = mls.generateJRadioButton("removeSpaceRB", true, null);
		firstUppercaseRB = mls.generateJRadioButton("firstUppercaseRB", true, null);
		allUppercaseRB = mls.generateJRadioButton("allUppercaseRB", true, null);
		spaceToUnderscoreRB = mls.generateJRadioButton("spaceToUnderscoreRB", true, null);
		convertRB = mls.generateJRadioButton("convertRB", true, null);
		underscoreToSpaceRB = mls.generateJRadioButton("underscoreToSpaceRB", true, null);
		allLowercaseRB = mls.generateJRadioButton("allLowercaseRB", true, null);
		replaceAllRB = mls.generateJRadioButton("replaceAllRB", true, null);
		replaceSelectedRB = mls.generateJRadioButton("replaceSelectedRB", true, null);
		convertToTF = mls.generateJTextField("convertToTF", true, true, 10, "");
		convertFromTF = mls.generateJTextField("convertFromTF", true, true, 10, "");

		ButtonGroup bg = new ButtonGroup();
		bg.add(removeSpaceRB);
		bg.add(spaceToUnderscoreRB);
		bg.add(underscoreToSpaceRB);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(allUppercaseRB);
		bg1.add(allLowercaseRB);
		bg1.add(firstUppercaseRB);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(replaceAllRB);
		bg2.add(replaceSelectedRB);
		replaceSelectedRB.setSelected(true);

		contentPane = new JPanel();

		fieldsP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink]", "[shrink][shrink][shrink][shrink][shrink][shrink][shrink]"));
		processP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink][shrink]", "[shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink]"));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(listL);

		contentPane.setLayout(new MigLayout("insets 4", "[grow]", "[grow]"));

		setContentPane(contentPane);

		fieldsP.add(titleChB);
		fieldsP.add(artistChB);
		fieldsP.add(albumArtistChB, "wrap");
		fieldsP.add(albumChB);
		fieldsP.add(yearChB);
		fieldsP.add(trackChB, "wrap");
		fieldsP.add(maxTrackChB);
		fieldsP.add(cdChB);
		fieldsP.add(maxCDChB, "wrap");
		fieldsP.add(genreChB);
		fieldsP.add(commentChB);
		fieldsP.add(composerChB, "wrap");
		fieldsP.add(origArtistChB);
		fieldsP.add(copyrightChB);
		fieldsP.add(urlChB, "wrap");
		fieldsP.add(encoderChB);
		fieldsP.add(lyricsChB, "wrap");
		fieldsP.add(selectAllChB, "gaptop 10");

		processP.add(spaceToUnderscoreRB, "span 4, wrap");
		processP.add(underscoreToSpaceRB, "span 4, wrap");
		processP.add(removeSpaceRB, "span 4, wrap");
		processP.add(allUppercaseRB, "span 4, wrap");
		processP.add(allLowercaseRB, "span 4, wrap");
		processP.add(firstUppercaseRB, "span 4, wrap");
		processP.add(convertRB);
		processP.add(convertFromTF, "width :100:, grow");
		processP.add(toJL);
		processP.add(convertToTF, "width :100:, grow, wrap");

		JPanel listP = new JPanel(new MigLayout("insets 0", "[shrink][grow]", "[grow]"));
		JPanel btnP = new JPanel(new MigLayout("insets 0", "[shrink]", "[shrink][shrink]"));

		btnP.add(addB, "wrap, grow");
		btnP.add(deleteB);

		listP.add(btnP, "grow");
		listP.add(scrollPane, " width :230:, grow");

		processP.add(listP, "span 4, grow");

		JPanel bottomP = new JPanel(new MigLayout("insets 0"));

		JPanel replaceP = new JPanel(new MigLayout("insets 0"));
		replaceP.add(replaceSelectedRB, "wrap");
		replaceP.add(replaceAllRB);

		bottomP.add(replaceP, "right, push");
		bottomP.add(cancelB, "push, right");
		bottomP.add(startB);

		contentPane.add(fieldsP, "wrap, grow");
		contentPane.add(processP, "grow, wrap");
		contentPane.add(bottomP, "grow");

		pack();
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		this.startB.addActionListener(l);
		this.cancelB.addActionListener(l);
		this.selectAllChB.addActionListener(l);
		this.addB.addActionListener(l);
		this.deleteB.addActionListener(l);
	}

	/**
	 * sets the checkboxes selectec
	 * 
	 * @param en
	 *            true if all selected, else false
	 */
	public void setCheckBoxes(boolean en) {
		titleChB.setSelected(en);
		artistChB.setSelected(en);
		albumArtistChB.setSelected(en);
		albumChB.setSelected(en);
		yearChB.setSelected(en);
		trackChB.setSelected(en);
		maxTrackChB.setSelected(en);
		cdChB.setSelected(en);
		maxCDChB.setSelected(en);
		genreChB.setSelected(en);
		commentChB.setSelected(en);
		composerChB.setSelected(en);
		origArtistChB.setSelected(en);
		copyrightChB.setSelected(en);
		urlChB.setSelected(en);
		encoderChB.setSelected(en);
		lyricsChB.setSelected(en);

		selectAllChB.setSelected(en);
	}

	public boolean isAllSelected() {
		return this.selectAllChB.isSelected();
	}

	public int getSelectedIndex() {
		return this.listL.getSelectedIndex();
	}

	/**
	 * adds a convert element to the list
	 */
	public void addConvertElement() {
		if (this.convertFromTF.getText().length() == 0 && this.convertToTF.getText().length() == 0)
			return;

		int size = this.listL.getModel().getSize();
		DefaultListModel<ConvertElement> model = new DefaultListModel<>();

		for (int i = 0; i < size; i++)
			model.addElement(this.listL.getModel().getElementAt(i));

		model.addElement(new ConvertElement(this.convertFromTF.getText(), this.convertToTF.getText()));

		this.listL.setModel(model);
	}

	/**
	 * deletes the convert element with the given index
	 * 
	 * @param index
	 *            the given index
	 */
	public void deleteConvertElement(int index) {
		int size = this.listL.getModel().getSize();
		DefaultListModel<ConvertElement> model = new DefaultListModel<>();

		for (int i = 0; i < size; i++)
			if (i != index)
				model.addElement(this.listL.getModel().getElementAt(i));

		this.listL.setModel(model);
	}

	/**
	 * refreshs the list
	 */
	public void refreshList() {
		int size = this.listL.getModel().getSize();
		DefaultListModel<ConvertElement> model = new DefaultListModel<>();

		ConvertElement ele;
		for (int i = 0; i < size; i++) {
			ele = this.listL.getModel().getElementAt(i);
			ele.setConvert(mls.getMessage("convert"));
			ele.setTo(mls.getMessage("to"));

			model.addElement(ele);
		}

		this.listL.setModel(model);
	}

	/**
	 * gets all checked fields (unchecked will be false)
	 * 
	 * @return the checked array
	 */
	public boolean[] getCheckedFields() {
		return new boolean[] { this.titleChB.isSelected(), this.artistChB.isSelected(), this.albumArtistChB.isSelected(), this.albumChB.isSelected(), this.yearChB.isSelected(),
				this.trackChB.isSelected(), this.maxTrackChB.isSelected(), this.cdChB.isSelected(), this.maxCDChB.isSelected(), this.genreChB.isSelected(),
				this.commentChB.isSelected(), this.composerChB.isSelected(), this.origArtistChB.isSelected(), this.copyrightChB.isSelected(), this.urlChB.isSelected(),
				this.encoderChB.isSelected(), this.lyricsChB.isSelected() };
	}

	/**
	 * gets the checked process aray
	 * 
	 * @return the process array
	 */
	public boolean[] getCheckedProcesses() {
		return new boolean[] { spaceToUnderscoreRB.isSelected(), underscoreToSpaceRB.isSelected(), removeSpaceRB.isSelected(), allUppercaseRB.isSelected(),
				allLowercaseRB.isSelected(), firstUppercaseRB.isSelected(), convertRB.isSelected() };
	}

	/**
	 * gets a list with all convert elements
	 * 
	 * @return the convert elements
	 */
	public List<ConvertElement> getConverts() {
		List<ConvertElement> result = new ArrayList<ConvertElement>();

		int size = this.listL.getModel().getSize();

		for (int i = 0; i < size; i++)
			result.add(this.listL.getModel().getElementAt(i));

		return result;
	}

	public boolean replaceAllSelected() {
		return this.replaceAllRB.isSelected();
	}
}