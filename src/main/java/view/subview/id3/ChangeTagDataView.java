package view.subview.id3;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import view.structure.ImagePanel;
import view.subview.HelpDialogView;
import model.audio.Genres;
import model.collector.Track;
import model.structure.ID3TagData;
import model.util.Graphics;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;
import config.Constants;

public class ChangeTagDataView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;
	private ImagePanel album1IP;
	private ImagePanel album2IP;

	private JButton nextTagB;
	private JCheckBox origArtistChB;
	private JCheckBox copyrightChB;
	private JCheckBox commentChB;
	private JCheckBox urlChB;
	private JCheckBox titleChB;
	private JCheckBox encoderChB;
	private JCheckBox genreChB;
	private JCheckBox albumArtistChB;
	private JCheckBox albumChB;
	private JCheckBox artistChB;
	private JCheckBox composerChB;
	private JCheckBox yearChB;
	private JCheckBox cdChB;
	private JCheckBox trackChB;
	private JCheckBox selectAllChB;
	private JCheckBox maxCDChB;
	private JCheckBox maxTracksChB;
	private JCheckBox repeatChB;
	private JLabel newCDJL;
	private JLabel copyrightJL;
	private JLabel newCommentJL;
	private JLabel albumArtistJL;
	private JLabel newEncoderJL;
	private JLabel currCopyrightJL;
	private JLabel currAlbumJL;
	private JLabel currTrackJL;
	private JLabel newYearJL;
	private JLabel encoderJL;
	private JLabel currGenreJL;
	private JLabel origArtistJL;
	private JLabel currComposerJL;
	private JLabel titleJL;
	private JLabel cdJL;
	private JLabel commentJL;
	private JLabel selectAllJL;
	private JLabel albumJL;
	private JLabel urlJL;
	private JLabel newUrlJL;
	private JLabel newAlbumJL;
	private JLabel currAlbumArtistJL;
	private JLabel currTitleJL;
	private JLabel currartistJL;
	private JLabel currCDJL;
	private JLabel currOrigArtistJL;
	private JLabel currCommentJL;
	private JLabel currUrlJL;
	private JLabel artistJL;
	private JLabel newTrackJL;
	private JLabel newCopyrightJL;
	private JLabel newTitleJL;
	private JLabel newAlbumArtistJL;
	private JLabel newartistJL;
	private JLabel trackJL;
	private JLabel newComposerJL;
	private JLabel newOrigArtistJL;
	private JLabel composerJL;
	private JLabel newGenreJL;
	private JLabel currEncoderJL;
	private JLabel yearJL;
	private JLabel genreJL;
	private JLabel currYearJL;
	private JPanel tagP;
	private JLabel maxTracksJL;
	private JLabel currMaxTracksJL;
	private JLabel newMaxTracksJL;
	private JLabel maxCDJL;
	private JLabel currMaxCDJL;
	private JLabel newMaxCDJL;
	private JLabel counterJL;

	/**
	 * Constructor
	 */
	public ChangeTagDataView() {
		mls = new MLS("view/languageFiles/ChangeTagDataView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {
		mls.addJFrame("window", this);

		contentPane = new JPanel();
		tagP = mls.generateTitledBevelPanel("tagP", BevelBorder.LOWERED);
		album1IP = new ImagePanel(Graphics.readImageFromJarWoExc("view/images/general/help10.png"));
		album2IP = new ImagePanel(Graphics.readImageFromJarWoExc("view/images/general/help10.png"));
		nextTagB = mls.generateJButton("nextTagB", true, null);
		origArtistChB = mls.generateJCheckBox("origArtistChB", true, false, null);
		copyrightChB = mls.generateJCheckBox("copyrightChB", true, false, null);
		commentChB = mls.generateJCheckBox("commentChB", true, false, null);
		urlChB = mls.generateJCheckBox("urlChB", true, false, null);
		titleChB = mls.generateJCheckBox("titleChB", true, false, null);
		encoderChB = mls.generateJCheckBox("encoderChB", true, false, null);
		genreChB = mls.generateJCheckBox("genreChB", true, false, null);
		albumArtistChB = mls.generateJCheckBox("albumArtistChB", true, false, null);
		albumChB = mls.generateJCheckBox("albumChB", true, false, null);
		artistChB = mls.generateJCheckBox("artistChB", true, false, null);
		composerChB = mls.generateJCheckBox("composerChB", true, false, null);
		yearChB = mls.generateJCheckBox("yearChB", true, false, null);
		cdChB = mls.generateJCheckBox("cdChB", true, false, null);
		trackChB = mls.generateJCheckBox("trackChB", true, false, null);
		selectAllChB = mls.generateJCheckBox("selectAllChB", true, false, null);
		maxCDChB = mls.generateJCheckBox("maxCDChB", true, false, null);
		maxTracksChB = mls.generateJCheckBox("maxTracksChB", true, false, null);
		repeatChB = mls.generateJCheckBox("repeatChB", true, false, null);
		newCDJL = mls.generateJLabel("newCdJL", true);
		copyrightJL = mls.generateJLabel("copyrightJL", true);
		newCommentJL = mls.generateJLabel("newCommentJL", true);
		albumArtistJL = mls.generateJLabel("albumArtistJL", true);
		newEncoderJL = mls.generateJLabel("newEncoderJL", true);
		currCopyrightJL = mls.generateJLabel("currCopyrightJL", true);
		currAlbumJL = mls.generateJLabel("currAlbumJL", true);
		currTrackJL = mls.generateJLabel("currTrackJL", true);
		newYearJL = mls.generateJLabel("newYearJL", true);
		encoderJL = mls.generateJLabel("encoderJL", true);
		currGenreJL = mls.generateJLabel("currGenreJL", true);
		origArtistJL = mls.generateJLabel("origArtistJL", true);
		currComposerJL = mls.generateJLabel("currComposerJL", true);
		titleJL = mls.generateJLabel("titleJL", true);
		cdJL = mls.generateJLabel("cdJL", true);
		commentJL = mls.generateJLabel("commentJL", true);
		selectAllJL = mls.generateJLabel("selectAllJL", true);
		albumJL = mls.generateJLabel("albumJL", true);
		urlJL = mls.generateJLabel("urlJL", true);
		newUrlJL = mls.generateJLabel("newUrlJL", true);
		newAlbumJL = mls.generateJLabel("newAlbumJL", true);
		currAlbumArtistJL = mls.generateJLabel("currAlbumArtistJL", true);
		currTitleJL = mls.generateJLabel("currTitleJL", true);
		currartistJL = mls.generateJLabel("currartistJL", true);
		currCDJL = mls.generateJLabel("currCDJL", true);
		currOrigArtistJL = mls.generateJLabel("currOrigArtistJL", true);
		currCommentJL = mls.generateJLabel("currCommentJL", true);
		currUrlJL = mls.generateJLabel("currUrlJL", true);
		artistJL = mls.generateJLabel("artistJL", true);
		newTrackJL = mls.generateJLabel("newTrackJL", true);
		newCopyrightJL = mls.generateJLabel("newCopyrightJL", true);
		newTitleJL = mls.generateJLabel("newTitleJL", true);
		newAlbumArtistJL = mls.generateJLabel("newAlbumArtistJL", true);
		newartistJL = mls.generateJLabel("newartistJL", true);
		trackJL = mls.generateJLabel("trackJL", true);
		newComposerJL = mls.generateJLabel("newCopmposerJL", true);
		newOrigArtistJL = mls.generateJLabel("newOrigArtistJL", true);
		composerJL = mls.generateJLabel("composerJL", true);
		newGenreJL = mls.generateJLabel("newGenreJL", true);
		currEncoderJL = mls.generateJLabel("currEncoderJL", true);
		yearJL = mls.generateJLabel("yearJL", true);
		genreJL = mls.generateJLabel("genreJL", true);
		currYearJL = mls.generateJLabel("currYearJL", true);
		maxTracksJL = mls.generateJLabel("maxTracksJL", true);
		currMaxTracksJL = mls.generateJLabel("currMaxTracksJL", true);
		newMaxTracksJL = mls.generateJLabel("newMaxTracksJL", true);
		maxCDJL = mls.generateJLabel("maxCDJL", true);
		currMaxCDJL = mls.generateJLabel("currMaxCDJL", true);
		newMaxCDJL = mls.generateJLabel("newMaxCDJL", true);
		counterJL = mls.generateJLabel("counterJL", true);

		setContentPane(contentPane);

		tagP.setLayout(new MigLayout("insets 5", "[shrink][shrink][shrink][shrink][shrink]",
				"[shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink][shrink]"));

		JPanel selectAllP = new JPanel(new MigLayout("insets 0"));
		selectAllP.add(selectAllJL, "push, right");

		tagP.add(selectAllP, "span 4, grow");
		tagP.add(selectAllChB, "wrap, gapleft 15");

		BufferedImage rightArrow = Constants.RIGHT_ARROW;
		tagP.add(titleJL);
		tagP.add(currTitleJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newTitleJL, "gapleft 15");
		tagP.add(titleChB, "wrap, gapleft 15");

		tagP.add(artistJL);
		tagP.add(currartistJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newartistJL, "gapleft 15");
		tagP.add(artistChB, "wrap, gapleft 15");

		tagP.add(albumArtistJL);
		tagP.add(currAlbumArtistJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newAlbumArtistJL, "gapleft 15");
		tagP.add(albumArtistChB, "wrap, gapleft 15");

		tagP.add(albumJL);
		tagP.add(currAlbumJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newAlbumJL, "gapleft 15");
		tagP.add(albumChB, "wrap, gapleft 15");

		tagP.add(yearJL);
		tagP.add(currYearJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newYearJL, "gapleft 15");
		tagP.add(yearChB, "wrap, gapleft 15");

		tagP.add(trackJL);
		tagP.add(currTrackJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		JPanel currTrackP = new JPanel(new MigLayout("insets 0", "[shrink][shrink]", "[shrink]"));
		currTrackP.add(newTrackJL);
		currTrackP.add(album1IP, "gapleft 5");
		tagP.add(currTrackP, "gapleft 15");
		tagP.add(trackChB, "wrap, gapleft 15");

		tagP.add(maxTracksJL);
		tagP.add(currMaxTracksJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		JPanel maxTrackP = new JPanel(new MigLayout("insets 0", "[shrink][shrink]", "[shrink]"));
		maxTrackP.add(newMaxTracksJL);
		maxTrackP.add(album2IP, "gapleft 5");
		tagP.add(maxTrackP, "gapleft 15");
		tagP.add(maxTracksChB, "wrap, gapleft 15");

		tagP.add(cdJL);
		tagP.add(currCDJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newCDJL, "gapleft 15");
		tagP.add(cdChB, "wrap, gapleft 15");

		tagP.add(maxCDJL);
		tagP.add(currMaxCDJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newMaxCDJL, "gapleft 15");
		tagP.add(maxCDChB, "wrap, gapleft 15");

		tagP.add(genreJL);
		tagP.add(currGenreJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newGenreJL, "gapleft 15");
		tagP.add(genreChB, "wrap, gapleft 15");

		tagP.add(commentJL);
		tagP.add(currCommentJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newCommentJL, "gapleft 15");
		tagP.add(commentChB, "wrap, gapleft 15");

		tagP.add(composerJL);
		tagP.add(currComposerJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newComposerJL, "gapleft 15");
		tagP.add(composerChB, "wrap, gapleft 15");

		tagP.add(origArtistJL);
		tagP.add(currOrigArtistJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newOrigArtistJL, "gapleft 15");
		tagP.add(origArtistChB, "wrap, gapleft 15");

		tagP.add(copyrightJL);
		tagP.add(currCopyrightJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newCopyrightJL, "gapleft 15");
		tagP.add(copyrightChB, "wrap, gapleft 15");

		tagP.add(urlJL);
		tagP.add(currUrlJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newUrlJL, "gapleft 15");
		tagP.add(urlChB, "wrap, gapleft 15");

		tagP.add(encoderJL);
		tagP.add(currEncoderJL, "gapleft 15");
		tagP.add(new ImagePanel(rightArrow), "height :20:, width :20:, gapleft 15");
		tagP.add(newEncoderJL, "gapleft 15");
		tagP.add(encoderChB, "gapleft 15, wrap");

		tagP.add(counterJL, "push, center, span 5, gaptop 15");

		JPanel btnP = new JPanel(new MigLayout("insets 0"));
		btnP.add(repeatChB, "left");
		btnP.add(nextTagB, "push, right, gapright 2");

		contentPane.setLayout(new MigLayout("insets 7", "[grow]", "[grow][shrink]"));

		contentPane.add(tagP, "grow, wrap");
		contentPane.add(btnP, "gaptop 7, grow");

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
		selectAllChB.addActionListener(l);
	}

	/**
	 * sets the MouseListener
	 * 
	 * @param l
	 *            the MouseListener
	 */
	public void setMouseListener(MouseListener l) {
		this.album1IP.addMouseListener(l);
		this.album2IP.addMouseListener(l);
	}

	/**
	 * sets the current id3 data
	 * 
	 * @param data
	 *            the id3 data
	 */
	public void setCurrentData(ID3TagData data) {
		this.currTitleJL.setText(data.getTitle());
		this.currartistJL.setText(data.getArtist());
		this.currAlbumArtistJL.setText(data.getAlbumArtist());
		this.currAlbumJL.setText(data.getAlbum());
		this.currYearJL.setText(data.getYear());
		this.currTrackJL.setText(data.getCurrTrack());
		this.currMaxTracksJL.setText(data.getMaxTrack());
		this.currCDJL.setText(data.getCurrCD());
		this.currMaxCDJL.setText(data.getMaxCD());
		this.currGenreJL.setText(Genres.getGenre(data.getGenre()));
		this.currCommentJL.setText(data.getComment());
		this.currComposerJL.setText(data.getComposer());
		this.currOrigArtistJL.setText(data.getOrigArtist());
		this.currCopyrightJL.setText(data.getCopyright());
		this.currUrlJL.setText(data.getUrl());
		this.currEncoderJL.setText(data.getEncoder());
	}

	/**
	 * sets the new id3 data
	 * 
	 * @param data
	 *            id3 data
	 */
	public void setNewData(ID3TagData data) {
		this.newTitleJL.setText(data.getTitle());
		this.newartistJL.setText(data.getArtist());
		this.newAlbumArtistJL.setText(data.getAlbumArtist());
		this.newAlbumJL.setText(data.getAlbum());
		this.newYearJL.setText(data.getYear());
		this.newTrackJL.setText(data.getCurrTrack());
		this.newMaxTracksJL.setText(data.getMaxTrack());
		this.newCDJL.setText(data.getCurrCD());
		this.newMaxCDJL.setText(data.getMaxCD());
		this.newGenreJL.setText(Genres.getGenre(data.getGenre()));
		this.newCommentJL.setText(data.getComment());
		this.newComposerJL.setText(data.getComposer());
		this.newOrigArtistJL.setText(data.getOrigArtist());
		this.newCopyrightJL.setText(data.getCopyright());
		this.newUrlJL.setText(data.getUrl());
		this.newEncoderJL.setText(data.getEncoder());

		setAlbumToolTip(data.getAlbum(), data.getAlbumTracks());
	}

	/**
	 * sets the album tooltip
	 * 
	 * @param albumName
	 *            the album name
	 * @param tracks
	 *            the tracks for this album
	 */
	private void setAlbumToolTip(String albumName, List<Track> tracks) {
		String result = "";
		if (tracks == null || tracks.size() == 0)
			result = "<html>no album information available.</html>";
		else {
			// @formatter:off
			result = "<html>" + albumName + "<br /><br />\n\n";

			for (Track t : tracks) {
				result += "#" + t.getTrackNr() + " " + t.getTitle() + "<br />";
			}
			
			if (result.endsWith("<br />"))
				result = result.substring(0, result.length() - 6);
			
			result += "</html>";
			// @formatter:off
		}
		
		// check if track and max tracks is available, if so print the helpdialog, if not make it invisible
		if (this.newTrackJL.getText().trim().equals(""))
			this.album1IP.setVisible(false);
		else {
			this.album1IP.setVisible(true);
			this.album1IP.setToolTipText(result);
		}
		
		if (this.newMaxTracksJL.getText().trim().equals(""))
			this.album2IP.setVisible(false);
		else {
			this.album2IP.setVisible(true);
			this.album2IP.setToolTipText(result);
		}
	}
	
	/**
	 * opens the help dialog with the album information
	 */
	public void openHelpDialog() {
		HelpDialogView h = new HelpDialogView();
		
		// set the tooltip text from album1IP or 2 depending on which has data
		if (this.album1IP.getToolTipText() != null && !this.album1IP.getToolTipText().equals(""))
			h.init(this.album1IP.getToolTipText());
		else h.init(this.album2IP.getToolTipText());
		
		h.setVisible(true);
	}

	public void fixSize() {
		validate();
		pack();
	}

	/**
	 * gets the changed data array
	 * 
	 * @return the array
	 */
	public boolean[] getChangedData() {
		return new boolean[] { this.titleChB.isSelected(), this.artistChB.isSelected(), this.albumArtistChB.isSelected(), this.albumChB.isSelected(), this.yearChB.isSelected(),
				this.trackChB.isSelected(), this.maxTracksChB.isSelected(), this.cdChB.isSelected(), this.maxCDChB.isSelected(), this.genreChB.isSelected(),
				this.commentChB.isSelected(), this.composerChB.isSelected(), this.origArtistChB.isSelected(), this.copyrightChB.isSelected(), this.urlChB.isSelected(),
				this.encoderChB.isSelected() };
	}

	/**
	 * sets all checkboxes selected
	 * 
	 * @param en true if all selected, else false
	 */
	public void setCheckBoxes(boolean en) {
		this.selectAllChB.setSelected(en);

		this.titleChB.setSelected(en);
		this.artistChB.setSelected(en);
		this.albumArtistChB.setSelected(en);
		this.albumChB.setSelected(en);
		this.yearChB.setSelected(en);
		this.trackChB.setSelected(en);
		this.maxTracksChB.setSelected(en);
		this.cdChB.setSelected(en);
		this.maxCDChB.setSelected(en);
		this.genreChB.setSelected(en);
		this.commentChB.setSelected(en);
		this.composerChB.setSelected(en);
		this.origArtistChB.setSelected(en);
		this.copyrightChB.setSelected(en);
		this.urlChB.setSelected(en);
		this.encoderChB.setSelected(en);
	}

	/**
	 * sets the checkboxes using the given boolean array
	 * 
	 * @param data the boolean array
	 */
	public void setCheckBoxes(boolean[] data) {
		this.titleChB.setSelected(data[0]);
		this.artistChB.setSelected(data[1]);
		this.albumArtistChB.setSelected(data[2]);
		this.albumChB.setSelected(data[3]);
		this.yearChB.setSelected(data[4]);
		this.trackChB.setSelected(data[5]);
		this.maxTracksChB.setSelected(data[6]);
		this.cdChB.setSelected(data[7]);
		this.maxCDChB.setSelected(data[8]);
		this.genreChB.setSelected(data[9]);
		this.commentChB.setSelected(data[10]);
		this.composerChB.setSelected(data[11]);
		this.origArtistChB.setSelected(data[12]);
		this.copyrightChB.setSelected(data[13]);
		this.urlChB.setSelected(data[14]);
		this.encoderChB.setSelected(data[13]);

		// check if all selected
		boolean all = true;
		for (int i = 0; i < data.length; i++)
			all = all && data[i];

		this.selectAllChB.setSelected(all);
	}

	public boolean isSelectAll() {
		return this.selectAllChB.isSelected();
	}

	public boolean isRepeatSelected() {
		return this.repeatChB.isSelected();
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
}