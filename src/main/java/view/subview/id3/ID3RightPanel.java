package view.subview.id3;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;

import view.structure.ImagePanel;
import model.audio.interfaces.IAudioFile;
import model.structure.ID3TagData;
import model.util.FileUtil;
import model.util.Graphics;
import model.util.Util;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Constants;

public class ID3RightPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7604574750879411829L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel audioFileDataP;
	private JPanel id3TagOptP;
	private JPanel id3TagBorderP;
	private JPanel fileOptP;
	private JLabel cdJL;
	private JLabel audioFileNameJL;
	private JLabel audioFileCurrentNameJL;
	private JLabel frequenceJL;
	private JLabel layerJL;
	private JLabel currDimension;
	private JLabel commentJL;
	private JLabel lengthJL;
	private JLabel urlJL;
	private JLabel dimensionJL;
	private JLabel currBitrateJL;
	private JLabel composerJL;
	private JLabel genreJL;
	private JLabel publisherJL;
	private JLabel copyrightJL;
	private JLabel albumArtistJL;
	private JLabel origArtistJL;
	private JLabel modeJL;
	private JLabel currModeJL;
	private JLabel currLengthJL;
	private JLabel titleJL;
	private JLabel albumJL;
	private JLabel currSizeJL;
	private JLabel imageJL;
	private JLabel sizeJL;
	private JLabel artistJL;
	private JLabel encodedByJL;
	private JLabel trackJL;
	private JLabel currFrequenceJL;
	private JLabel bitrateJL;
	private JLabel yearJL;
	private JLabel extJL;
	private JLabel currExtJL;
	private JLabel imageSizeJL;
	private JLabel currImageSizeJL;
	private JLabel lyricsJL;
	private JRadioButton urlRB;
	private JRadioButton maxTracksRB;
	private JRadioButton copyrightRB;
	private JRadioButton encodedByRB;
	private JRadioButton composerRB;
	private JRadioButton publisherRB;
	private JRadioButton commentRB;
	private JRadioButton albumRB;
	private JRadioButton albumArtistRB;
	private JRadioButton genreRB;
	private JRadioButton titleRB;
	private JRadioButton artistRB;
	private JRadioButton origArtistRB;
	private JRadioButton lyricsRB;
	private JRadioButton imageAllRB;
	private JRadioButton trackRB;
	private JRadioButton yearRB;
	private JRadioButton cdRB;
	private JRadioButton maxCDRB;
	private ImagePanel imageIP;
	private JButton fieldReplacerB;
	private JButton searchOnlineB;
	private JButton generateByNameB;
	private JButton ImageSettingsB;
	private JButton addImageB;
	private JButton deleteTagB;
	private JButton undoB;
	private JButton loadLyricsB;
	private JButton saveLyricsB;
	private JButton deleteLyricsB;
	private JButton searchLyricsB;
	private JButton searchImageB;
	private JButton saveImageB;
	private JButton playAudioB;
	private JButton deleteFileHDDB;
	private JComboBox<String> genreCB;
	private JTextArea lyricsTA;
	private JTabbedPane tabbedPane;
	private JTextField urlTF;
	private JTextField albumArtistTF;
	private JTextField maxTracksTF;
	private JTextField copyrightTF;
	private JTextField composerTF;
	private JTextField albumTF;
	private JTextField publisherTF;
	private JTextField commentTF;
	private JTextField yearTF;
	private JTextField titleTF;
	private JTextField origArtistTF;
	private JTextField encodedByTF;
	private JTextField artistTF;
	private JTextField maxCDTF;
	private JTextField trackTF;
	private JTextField cdTF;

	/**
	 * Constructor
	 * 
	 * @param mls
	 *            the given MLS
	 */
	public ID3RightPanel(MLS mls) {
		this.mls = mls;
	}

	/**
	 * inits the panel
	 */
	public void init() {
		audioFileDataP = mls.generateTitledBevelPanel("mp3dataP", BevelBorder.LOWERED, true);
		id3TagOptP = mls.generateTitledBevelPanel("id3TagOptP", BevelBorder.LOWERED, true);
		id3TagBorderP = mls.generateTitledBevelPanel("id3TagBorderP", BevelBorder.LOWERED, true);
		fileOptP = mls.generateTitledBevelPanel("fileOptP", BevelBorder.LOWERED, true);
		imageIP = new ImagePanel(null);
		mls.addCustomJPanel(imageIP, "imageIP");

		cdJL = mls.generateJLabel("cdJL", true);
		audioFileNameJL = mls.generateJLabel("mp3NameJL", true);
		audioFileCurrentNameJL = mls.generateJLabel("mp3CurrentNameJL", true);
		frequenceJL = mls.generateJLabel("frequenceJL", true);
		layerJL = mls.generateJLabel("layerJL", true);
		currDimension = mls.generateJLabel("currDimension", true);
		commentJL = mls.generateJLabel("commentJL", true);
		lengthJL = mls.generateJLabel("lengthJL", true);
		urlJL = mls.generateJLabel("urlJL", true);
		dimensionJL = mls.generateJLabel("dimensionJL", true);
		currBitrateJL = mls.generateJLabel("currBitrateJL", true);
		composerJL = mls.generateJLabel("composerJL", true);
		genreJL = mls.generateJLabel("genreJL", true);
		copyrightJL = mls.generateJLabel("copyrightJL", true);
		albumArtistJL = mls.generateJLabel("albumArtistJL", true);
		origArtistJL = mls.generateJLabel("origArtistJL", true);
		publisherJL = mls.generateJLabel("publisherJL", true);
		modeJL = mls.generateJLabel("modeJL", true);
		currModeJL = mls.generateJLabel("currModeJL", true);
		currLengthJL = mls.generateJLabel("currLengthJL", true);
		titleJL = mls.generateJLabel("titleJL", true);
		albumJL = mls.generateJLabel("albumJL", true);
		currSizeJL = mls.generateJLabel("currSizeJL", true);
		imageJL = mls.generateJLabel("imageJL", true);
		sizeJL = mls.generateJLabel("sizeJL", true);
		artistJL = mls.generateJLabel("artistJL", true);
		encodedByJL = mls.generateJLabel("encodedByJL", true);
		trackJL = mls.generateJLabel("trackJL", true);
		currFrequenceJL = mls.generateJLabel("currFrequenceJL", true);
		bitrateJL = mls.generateJLabel("bitrateJL", true);
		yearJL = mls.generateJLabel("yearJL", true);
		lyricsJL = mls.generateJLabel("lyricsJL", true);
		extJL = mls.generateJLabel("extJL", true);
		currExtJL = mls.generateJLabel("currExtJL", true);
		imageSizeJL = mls.generateJLabel("imageSizeJL", true);
		currImageSizeJL = mls.generateJLabel("currImageSizeJL", true);

		lyricsTA = mls.generateJTextArea("lyricsTA", true, true, 10, 10, "");

		urlRB = mls.generateJRadioButton("urlRB", true, null);
		maxTracksRB = mls.generateJRadioButton("maxTracksRB", true, null);
		copyrightRB = mls.generateJRadioButton("copyrightRB", true, null);
		encodedByRB = mls.generateJRadioButton("encodedByRB", true, null);
		composerRB = mls.generateJRadioButton("composerRB", true, null);
		commentRB = mls.generateJRadioButton("commentRB", true, null);
		albumRB = mls.generateJRadioButton("albumRB", true, null);
		albumArtistRB = mls.generateJRadioButton("albumArtistRB", true, null);
		genreRB = mls.generateJRadioButton("genreRB", true, null);
		titleRB = mls.generateJRadioButton("titleRB", true, null);
		artistRB = mls.generateJRadioButton("artistRB", true, null);
		origArtistRB = mls.generateJRadioButton("origArtistRB", true, null);
		lyricsRB = mls.generateJRadioButton("lyricsRB", true, null);
		imageAllRB = mls.generateJRadioButton("imageAllRB", true, null);
		trackRB = mls.generateJRadioButton("trackRB", true, null);
		yearRB = mls.generateJRadioButton("yearRB", true, null);
		cdRB = mls.generateJRadioButton("cdRB", true, null);
		maxCDRB = mls.generateJRadioButton("maxCDRB", true, null);
		publisherRB = mls.generateJRadioButton("publisherRB", true, null);

		publisherTF = mls.generateJTextField("publisherTF", true, true, 10, "");
		urlTF = mls.generateJTextField("urlTF", true, true, 10, "");
		albumArtistTF = mls.generateJTextField("albumArtistTF", true, true, 10, "");
		maxTracksTF = mls.generateJTextField("maxTracksTF", true, true, 10, "");
		copyrightTF = mls.generateJTextField("copyrightTF", true, true, 10, "");
		composerTF = mls.generateJTextField("composerTF", true, true, 10, "");
		albumTF = mls.generateJTextField("albumTF", true, true, 10, "");
		commentTF = mls.generateJTextField("commentTF", true, true, 10, "");
		yearTF = mls.generateJTextField("yearTF", true, true, 10, "");
		titleTF = mls.generateJTextField("titleTF", true, true, 10, "");
		origArtistTF = mls.generateJTextField("origArtistTF", true, true, 10, "");
		encodedByTF = mls.generateJTextField("encodedByTF", true, true, 10, "");
		artistTF = mls.generateJTextField("artistTF", true, true, 10, "");
		maxCDTF = mls.generateJTextField("maxCDTF", true, true, 10, "");
		trackTF = mls.generateJTextField("trackTF", true, true, 10, "");
		cdTF = mls.generateJTextField("cdTF", true, true, 10, "");

		fieldReplacerB = mls.generateJButton("fieldReplacerB", true, null);
		searchOnlineB = mls.generateJButton("searchOnlineB", true, null);
		generateByNameB = mls.generateJButton("generateByNameB", true, null);
		ImageSettingsB = mls.generateJButton("ImageSettingsB", true, null);
		addImageB = mls.generateJButton("addImageB", true, null);
		deleteTagB = mls.generateJButton("deleteTagB", true, null);
		undoB = mls.generateJButton("undoB", true, null);
		loadLyricsB = mls.generateJButton("loadLyricsB", true, null);
		saveLyricsB = mls.generateJButton("saveLyricsB", true, null);
		deleteLyricsB = mls.generateJButton("deleteLyricsB", true, null);
		searchLyricsB = mls.generateJButton("searchLyricsB", true, null);
		searchImageB = mls.generateJButton("searchImageB", true, null);
		saveImageB = mls.generateJButton("saveImageB", true, null);
		playAudioB = mls.generateJButton("playAudioB", true, null);
		deleteFileHDDB = mls.generateJButton("deleteFileHDDB", true, null);

		tabbedPane = mls.generateJTabbedPane("tabbedPane", true);
		genreCB = mls.generateJComboBox("genreCB", new String[] {}, -1, true, false);

		JScrollPane scrollPane3 = new JScrollPane();
		setLayout(new MigLayout("insets 0", "[shrink]", "[shrink][shrink][shrink][shrink]"));
		// audio file data
		audioFileDataP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink][shrink]", "[shrink][shrink][shrink][shrink]"));
		audioFileDataP.add(audioFileNameJL);
		audioFileDataP.add(audioFileCurrentNameJL, "wrap, gapleft 10, span 3");
		audioFileDataP.add(layerJL, "span 2");
		audioFileDataP.add(modeJL, "gapleft 30");
		audioFileDataP.add(currModeJL, "wrap, gapleft 10");
		audioFileDataP.add(bitrateJL);
		audioFileDataP.add(currBitrateJL, "gapleft 10");
		audioFileDataP.add(frequenceJL, "gapleft 30");
		audioFileDataP.add(currFrequenceJL, "wrap, gapleft 10");
		audioFileDataP.add(sizeJL);
		audioFileDataP.add(currSizeJL, "gapleft 10");
		audioFileDataP.add(lengthJL, "gapleft 30");
		audioFileDataP.add(currLengthJL, "gapleft 10");

		add(audioFileDataP, "grow, wrap");

		// tabbed pane + border
		id3TagBorderP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));

		// image Panel
		JPanel imageP = new JPanel(new MigLayout("insets 5", "[grow][shrink]", "[shrink][grow][shrink]"));
		imageIP.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		imageP.add(imageJL, "wrap");
		imageP.add(imageIP, "grow");
		imageP.add(imageAllRB, "wrap");
		JPanel imageButtonP = new JPanel(new MigLayout("insets 0", "[shrink][shrink][shrink][shrink]", "[shrink]"));
		imageButtonP.add(addImageB);
		imageButtonP.add(saveImageB);
		imageButtonP.add(ImageSettingsB, "push");
		JPanel extDimP = new JPanel(new MigLayout("insets 0"));
		extDimP.add(dimensionJL);
		extDimP.add(currDimension, "wrap");
		extDimP.add(extJL);
		extDimP.add(currExtJL, "wrap");
		extDimP.add(imageSizeJL);
		extDimP.add(currImageSizeJL);
		imageButtonP.add(extDimP);

		imageP.add(imageButtonP, "span 2, grow");

		// id3 Panel
		JPanel id3P = new JPanel(new MigLayout("insets 5", "[shrink][grow][shrink]",
				"[shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink]"));
		id3P.add(titleJL);
		id3P.add(titleTF, "grow, gapleft 10");
		id3P.add(titleRB, "wrap");

		id3P.add(artistJL);
		id3P.add(artistTF, "grow, gapleft 10");
		id3P.add(artistRB, "wrap");

		id3P.add(albumArtistJL);
		id3P.add(albumArtistTF, "grow, gapleft 10");
		id3P.add(albumArtistRB, "wrap");

		id3P.add(albumJL);
		id3P.add(albumTF, "grow, gapleft 10");
		id3P.add(albumRB, "wrap");

		JPanel yearTrackP = new JPanel(new MigLayout("insets 0", "[shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink]", "[shrink][shrink]"));
		yearTrackP.add(yearJL);
		yearTrackP.add(yearTF, "grow, width :50:");
		yearTrackP.add(yearRB);

		yearTrackP.add(trackJL);
		yearTrackP.add(trackTF, "grow, width :50:");
		yearTrackP.add(trackRB);

		yearTrackP.add(new JLabel("/"));
		yearTrackP.add(maxTracksTF, "width :50:, grow, wrap");

		yearTrackP.add(new JLabel());
		yearTrackP.add(new JLabel());
		yearTrackP.add(new JLabel());

		yearTrackP.add(cdJL);
		yearTrackP.add(cdTF, "grow, width :50:");
		yearTrackP.add(cdRB);

		yearTrackP.add(new JLabel("/"));
		yearTrackP.add(maxCDTF, "grow, width :50:");

		id3P.add(yearTrackP, "span 2 2, grow");
		id3P.add(maxTracksRB, "wrap");
		id3P.add(maxCDRB, "wrap");

		id3P.add(genreJL);
		id3P.add(genreCB, "grow, gapleft 10");
		id3P.add(genreRB, "wrap");

		id3P.add(commentJL);
		id3P.add(commentTF, "grow, gapleft 10");
		id3P.add(commentRB, "wrap");

		id3P.add(publisherJL);
		id3P.add(publisherTF, "grow, gapleft 10");
		id3P.add(publisherRB, "wrap");

		id3P.add(composerJL);
		id3P.add(composerTF, "grow, gapleft 10");
		id3P.add(composerRB, "wrap");

		id3P.add(origArtistJL);
		id3P.add(origArtistTF, "grow, gapleft 10");
		id3P.add(origArtistRB, "wrap");

		id3P.add(copyrightJL);
		id3P.add(copyrightTF, "grow, gapleft 10");
		id3P.add(copyrightRB, "wrap");

		id3P.add(urlJL);
		id3P.add(urlTF, "grow, gapleft 10");
		id3P.add(urlRB, "wrap");

		id3P.add(encodedByJL);
		id3P.add(encodedByTF, "grow, gapleft 10");
		id3P.add(encodedByRB);

		// lyrics Panel
		JPanel lyricsP = new JPanel(new MigLayout("insets 5", "[grow][shrink]", "[shrink][grow][shrink]"));
		JPanel lyricsButtonP = new JPanel(new MigLayout("insets 0"));
		scrollPane3.setViewportView(lyricsTA);

		lyricsP.add(lyricsJL, "wrap, span 2");
		lyricsP.add(scrollPane3, "grow");
		lyricsP.add(lyricsRB, "wrap");
		lyricsButtonP.add(loadLyricsB);
		lyricsButtonP.add(saveLyricsB);
		lyricsButtonP.add(deleteLyricsB);
		lyricsP.add(lyricsButtonP, "span 2");

		mls.addTab(tabbedPane, id3P, "id3TagTab");
		mls.addTab(tabbedPane, lyricsP, "lyricsTab");
		mls.addTab(tabbedPane, imageP, "ImageTab");

		id3TagBorderP.add(tabbedPane, "grow");

		add(id3TagBorderP, "width :370:, grow, wrap");

		// id3tag options
		id3TagOptP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink][shrink]", "[shrink][shrink]"));
		id3TagOptP.add(searchOnlineB);
		id3TagOptP.add(searchLyricsB);
		id3TagOptP.add(searchImageB, "push");
		id3TagOptP.add(deleteTagB, "wrap");

		id3TagOptP.add(fieldReplacerB);
		id3TagOptP.add(generateByNameB);
		id3TagOptP.add(new JLabel());
		id3TagOptP.add(undoB);

		add(id3TagOptP, "grow, wrap");

		// fileoptions
		fileOptP.setLayout(new MigLayout("insets 5", "[shrink][shrink]", "[shrink]"));
		fileOptP.add(playAudioB, "push");
		fileOptP.add(deleteFileHDDB, "right");

		add(fileOptP, "grow");
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		ImageSettingsB.addActionListener(l);
		addImageB.addActionListener(l);
		saveImageB.addActionListener(l);

		searchImageB.addActionListener(l);
		searchLyricsB.addActionListener(l);
		fieldReplacerB.addActionListener(l);
		searchOnlineB.addActionListener(l);
		generateByNameB.addActionListener(l);
		deleteTagB.addActionListener(l);
		undoB.addActionListener(l);

		loadLyricsB.addActionListener(l);
		saveLyricsB.addActionListener(l);
		deleteLyricsB.addActionListener(l);

		playAudioB.addActionListener(l);
		deleteFileHDDB.addActionListener(l);

		urlRB.addActionListener(l);
		maxTracksRB.addActionListener(l);
		copyrightRB.addActionListener(l);
		encodedByRB.addActionListener(l);
		composerRB.addActionListener(l);
		commentRB.addActionListener(l);
		albumRB.addActionListener(l);
		albumArtistRB.addActionListener(l);
		genreRB.addActionListener(l);
		titleRB.addActionListener(l);
		artistRB.addActionListener(l);
		origArtistRB.addActionListener(l);
		imageAllRB.addActionListener(l);
		lyricsRB.addActionListener(l);
		trackRB.addActionListener(l);
		yearRB.addActionListener(l);
		cdRB.addActionListener(l);
		maxCDRB.addActionListener(l);
		publisherRB.addActionListener(l);
	}

	/**
	 * sets a FocusListener
	 * 
	 * @param l
	 *            the FocusListener
	 */
	public void setFocusListener(FocusListener l) {
		this.titleTF.addFocusListener(l);
		this.artistTF.addFocusListener(l);
		this.albumArtistTF.addFocusListener(l);
		this.albumTF.addFocusListener(l);
		this.yearTF.addFocusListener(l);
		this.maxTracksTF.addFocusListener(l);
		this.maxCDTF.addFocusListener(l);
		this.commentTF.addFocusListener(l);
		this.composerTF.addFocusListener(l);
		this.origArtistTF.addFocusListener(l);
		this.copyrightTF.addFocusListener(l);
		this.urlTF.addFocusListener(l);
		this.encodedByTF.addFocusListener(l);
		this.trackTF.addFocusListener(l);
		this.cdTF.addFocusListener(l);
		this.publisherTF.addFocusListener(l);
		this.genreCB.addFocusListener(l);
		this.lyricsTA.addFocusListener(l);
	}

	/**
	 * sets a ChangeListener
	 * 
	 * @param l
	 *            the ChangeListener
	 */
	public void setChangeListener(ChangeListener l) {
		this.tabbedPane.addChangeListener(l);
	}

	/**
	 * resets all RadionButtons to unselected
	 */
	public void resetRB() {
		urlRB.setSelected(false);
		maxTracksRB.setSelected(false);
		copyrightRB.setSelected(false);
		encodedByRB.setSelected(false);
		composerRB.setSelected(false);
		commentRB.setSelected(false);
		albumRB.setSelected(false);
		albumArtistRB.setSelected(false);
		genreRB.setSelected(false);
		titleRB.setSelected(false);
		artistRB.setSelected(false);
		publisherRB.setSelected(false);
		origArtistRB.setSelected(false);
		imageAllRB.setSelected(false);
		lyricsRB.setSelected(false);
		trackRB.setSelected(false);
		yearRB.setSelected(false);
		cdRB.setSelected(false);
		maxCDRB.setSelected(false);
	}

	/**
	 * sets the given audio file data to the interface
	 * 
	 * @param audioFile
	 *            given audio file
	 * 
	 * @throws IOException
	 */
	public void setID3Data(IAudioFile audioFile) throws IOException {
		this.audioFileCurrentNameJL.setText(Util.stripExtraData(FileUtil.getFileName(audioFile.getFilePath())));
		this.layerJL.setText("MPEG " + (audioFile.getVersion() == null ? "?" : audioFile.getVersion().charAt(0)) + " Layer " + audioFile.getLayer());
		this.currBitrateJL.setText(audioFile.getBitrate() + " kb/s");
		this.currModeJL.setText(audioFile.getMode());
		this.currFrequenceJL.setText(audioFile.getFrequence() + " Hz");
		this.currSizeJL.setText(convertSizeInMB(audioFile.getFileSize()) + " MB");
		this.currLengthJL.setText(convertTime(audioFile.getAudioLength()) + " min");

		this.titleTF.setText(audioFile.getTitle());
		this.artistTF.setText(audioFile.getArtist());
		this.albumArtistTF.setText(audioFile.getAlbumArtist());
		this.albumTF.setText(audioFile.getAlbum());
		this.yearTF.setText(audioFile.getYear());
		this.maxTracksTF.setText(audioFile.getMaxTrack());
		this.trackTF.setText(audioFile.getCurrTrack());
		this.maxCDTF.setText(audioFile.getMaxCD());
		this.cdTF.setText(audioFile.getCurrCD());
		this.genreCB.setSelectedIndex((audioFile.getGenre() < 0 || audioFile.getGenre() > 148) ? 12 : audioFile.getGenre());
		this.commentTF.setText(audioFile.getComment());
		this.composerTF.setText(audioFile.getComposer());
		this.origArtistTF.setText(audioFile.getOriginalArtist());
		this.copyrightTF.setText(audioFile.getCopyright());
		this.urlTF.setText(audioFile.getURL());
		this.encodedByTF.setText(audioFile.getEncoder());

		this.lyricsTA.setText(audioFile.getLyrics());

		setCoverImageData(audioFile.getAlbumImage(), audioFile.getAlbumImageFormat());
	}

	/**
	 * resets all audio file data fields to its default value
	 */
	public void resetID3Data() {
		this.audioFileCurrentNameJL.setText(mls.getMessage("mp3CurrentNameJL"));
		this.layerJL.setText(mls.getMessage("layerJL"));
		this.currBitrateJL.setText(mls.getMessage("currBitrateJL"));
		this.currModeJL.setText(mls.getMessage("currModeJL"));
		this.currFrequenceJL.setText(mls.getMessage("currFrequenceJL"));
		this.currSizeJL.setText(mls.getMessage("currSizeJL"));
		this.currLengthJL.setText(mls.getMessage("currLengthJL"));

		this.titleTF.setText(mls.getMessage("titleTF"));
		this.artistTF.setText(mls.getMessage("artistTF"));
		this.albumArtistTF.setText(mls.getMessage("albumArtistTF"));
		this.albumTF.setText(mls.getMessage("albumTF"));
		this.yearTF.setText(mls.getMessage("yearTF"));
		this.maxTracksTF.setText(mls.getMessage("maxTracksTF"));
		this.trackTF.setText(mls.getMessage("trackTF"));
		this.maxCDTF.setText(mls.getMessage("maxCDTF"));
		this.cdTF.setText(mls.getMessage("cdTF"));
		this.genreCB.setSelectedIndex(12);
		this.commentTF.setText(mls.getMessage("commentTF"));
		this.composerTF.setText(mls.getMessage("composerTF"));
		this.origArtistTF.setText(mls.getMessage("origArtistTF"));
		this.copyrightTF.setText(mls.getMessage("copyrightTF"));
		this.urlTF.setText(mls.getMessage("urlTF"));
		this.encodedByTF.setText(mls.getMessage("encodedByTF"));

		this.lyricsTA.setText("");

		this.currExtJL.setText(mls.getMessage("currExtJL"));
		this.currDimension.setText(mls.getMessage("currDimension"));

		try {
			setCoverImageData(null, null);
		} catch (IOException e) {
			// do nothing
		}
	}

	/**
	 * gets the ID3Tag Data as shown in the interface
	 * 
	 * @return all ID3Tag Data
	 */
	public ID3TagData getID3TagData() {
		ID3TagData result = new ID3TagData();
		result.setTitle(this.titleTF.getText());
		result.setArtist(this.artistTF.getText());
		result.setAlbumArtist(this.albumArtistTF.getText());
		result.setAlbum(this.albumTF.getText());
		result.setYear(this.yearTF.getText());
		result.setCurrTrack(this.trackTF.getText());
		result.setMaxTrack(this.maxTracksTF.getText());
		result.setCurrCD(this.cdTF.getText());
		result.setMaxCD(this.maxCDTF.getText());
		// result.setTrack(this.trackTF.getText(), this.maxTracksTF.getText());
		// result.setCd(this.cdTF.getText(), this.maxCDTF.getText());
		result.setGenre(this.genreCB.getSelectedIndex());
		result.setComment(this.commentTF.getText());
		result.setComposer(this.composerTF.getText());
		result.setOrigArtist(this.origArtistTF.getText());
		result.setCopyright(this.copyrightTF.getText());
		result.setUrl(this.urlTF.getText());
		result.setEncoder(this.encodedByTF.getText());
		result.setLyrics(this.lyricsTA.getText());

		return result;
	}

	/**
	 * sets the genre combobox
	 * 
	 * @param genres
	 *            all genres
	 */
	public void setGenres(String[] genres) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

		for (int i = 0; i < genres.length; i++)
			model.addElement(genres[i]);

		this.genreCB.setModel(model);
		this.genreCB.setSelectedIndex(12);
	}

	/**
	 * gets the selected genre index. does NOT start by 0
	 * 
	 * @return the genre
	 */
	public String getGenre() {
		return Integer.toString(this.genreCB.getSelectedIndex());
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
		// convert byte array back to BufferedImage
		BufferedImage img;
		if (imgBytes == null || imgBytes.length == 0) {
			img = Constants.EMPTY;
			// set dimension + extension
			this.currDimension.setText(mls.getMessage("currDimension"));
			this.currExtJL.setText(mls.getMessage("currExtJL"));
			this.currImageSizeJL.setText(mls.getMessage("currImageSizeJL"));
		} else {
			InputStream in = new ByteArrayInputStream(imgBytes);
			img = ImageIO.read(in);
			in.close();
			// set dimension + extension
			this.currDimension.setText(img.getWidth() + "x" + img.getHeight());
			this.currExtJL.setText("*." + extension);

			long size = imgBytes.length;

			this.currImageSizeJL.setText(convertSizeInKB(size) + " KB");
		}

		img = Graphics.scale(img, this.imageIP.getWidth(), this.imageIP.getHeight());
		this.imageIP.setImage(img);
	}

	public int[] getImageSize() {
		String[] tmp = this.currDimension.getText().split("x");

		if (tmp.length != 2)
			return null;

		return new int[] { Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]) };
	}

	public String geturlTF() {
		return urlTF.getText();
	}

	public String getalbumArtistTF() {
		return albumArtistTF.getText();
	}

	public String getmaxTracksTF() {
		return maxTracksTF.getText();
	}

	public String getcopyrightTF() {
		return copyrightTF.getText();
	}

	public String getcomposerTF() {
		return composerTF.getText();
	}

	public String getalbumTF() {
		return albumTF.getText();
	}

	public String getcommentTF() {
		return commentTF.getText();
	}

	public String getyearTF() {
		return yearTF.getText();
	}

	public String gettitleTF() {
		return titleTF.getText();
	}

	public String getPublisherTF() {
		return publisherTF.getText();
	}

	public String getorigArtistTF() {
		return origArtistTF.getText();
	}

	public String getencodedByTF() {
		return encodedByTF.getText();
	}

	public String getartistTF() {
		return artistTF.getText();
	}

	public String getTrackNr() {
		return this.trackTF.getText();
	}

	public String getMaxTracks() {
		return this.maxTracksTF.getText();
	}

	public String getCurrCD() {
		return this.cdTF.getText();
	}

	public String getMaxCD() {
		return this.maxCDTF.getText();
	}

	public String getLyricsTA() {
		return this.lyricsTA.getText();
	}

	public void setLyricsTA(String lyrics) {
		this.lyricsTA.setText(lyrics);
	}

	/**
	 * converts the given number of bytes to MB
	 * 
	 * @param bytes
	 *            number of bytes
	 * 
	 * @return given bytes in MB
	 */
	private String convertSizeInMB(long bytes) {

		// to KB
		double result = ((double) bytes) / 1024.0;
		// to MB
		result = result / 1024.0;

		return Double.toString(Math.floor((result * 100)) / 100);
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
	 * converts the given secs in the format min:sec
	 * 
	 * @param secs
	 *            given secs
	 * 
	 * @return formatted to min:sec
	 */
	private String convertTime(long secs) {
		double result = ((double) secs) / 60.0;

		String[] tmp = Double.toString(result).split("\\.");
		String min = tmp[0];

		String sec = Double.toString(Math.floor(Double.parseDouble("0." + tmp[1]) * 60)).split("\\.")[0];

		if (sec.length() == 1)
			sec = "0" + sec;

		return min + ":" + sec;
	}

	/**
	 * sets all buttons enabled
	 * 
	 * @param en
	 *            true for enabled, else false
	 */
	public void setButtonsEnabled(boolean en) {
		searchImageB.setEnabled(en);
		searchLyricsB.setEnabled(en);
		searchOnlineB.setEnabled(en);
		playAudioB.setEnabled(en);
		deleteFileHDDB.setEnabled(en);
		fieldReplacerB.setEnabled(en);
		generateByNameB.setEnabled(en);
		undoB.setEnabled(en);
		deleteTagB.setEnabled(en);

		loadLyricsB.setEnabled(en);
		deleteLyricsB.setEnabled(en);
		saveLyricsB.setEnabled(en);

		saveImageB.setEnabled(en);
		ImageSettingsB.setEnabled(en);
		addImageB.setEnabled(en);
	}
}