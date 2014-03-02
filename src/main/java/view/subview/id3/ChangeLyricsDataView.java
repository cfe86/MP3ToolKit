package view.subview.id3;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import view.structure.ImagePanel;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;
import config.Constants;

public class ChangeLyricsDataView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1834695803862743635L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;
	private JButton nextTagB;
	private JCheckBox repeatChB;
	private JCheckBox lyricsChB;
	private JLabel counterJL;
	private JPanel lyricsP;
	private JTextArea newLyrics;
	private JTextArea currLyrics;
	private JLabel artistJL;

	/**
	 * Constructor
	 */
	public ChangeLyricsDataView() {
		mls = new MLS("view/languageFiles/ChangeLyricsDataView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {
		mls.addJFrame("window", this);

		contentPane = new JPanel();
		lyricsP = mls.generateTitledBevelPanel("lyricsP", BevelBorder.LOWERED, true);
		nextTagB = mls.generateJButton("nextTagB", true, null);
		repeatChB = mls.generateJCheckBox("repeatChB", true, false, null);
		lyricsChB = mls.generateJCheckBox("lyricsChB", true, false, null);
		counterJL = mls.generateJLabel("counterJL", true);
		artistJL = mls.generateJLabel("artistJL", true);
		newLyrics = mls.generateJTextArea("newLyrics", true, false, 10, 10, "");
		currLyrics = mls.generateJTextArea("currLyrics", true, false, 10, 10, "");

		setContentPane(contentPane);

		lyricsP.setLayout(new MigLayout("insets 5", "[grow][shrink][grow][shrink]", "[shrink][grow][shrink]"));

		JScrollPane sp1 = new JScrollPane();
		JScrollPane sp2 = new JScrollPane();
		sp1.setViewportView(currLyrics);
		sp2.setViewportView(newLyrics);

		lyricsP.add(artistJL, "push, center, span 4, gapbottom 15, wrap");
		lyricsP.add(sp1, "height :300:, width :300:, grow");
		lyricsP.add(new ImagePanel(Constants.RIGHT_ARROW), "height 20!, width 20!, gapleft 15");
		lyricsP.add(sp2, "gapleft 15, height :300:, width :300:, grow");
		lyricsP.add(lyricsChB, "gapleft 15, wrap");

		lyricsP.add(counterJL, "push, center, span 4, gaptop 15");

		JPanel btnP = new JPanel(new MigLayout("insets 0"));
		btnP.add(repeatChB, "left");
		btnP.add(nextTagB, "push, right, gapright 2");

		contentPane.setLayout(new MigLayout("insets 7", "[grow]", "[grow][shrink]"));
		contentPane.add(lyricsP, "grow, wrap");

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
		nextTagB.addActionListener(l);
	}

	public boolean isRepeatChecked() {
		return this.repeatChB.isSelected();
	}

	public boolean isLyricsChecked() {
		return this.lyricsChB.isSelected();
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

	/**
	 * sets the current lyrics
	 * 
	 * @param lyrics
	 *            the lyrics
	 */
	public void setCurrLyrics(String lyrics) {
		this.currLyrics.setText(lyrics);
	}

	/**
	 * sets the new lyrics
	 * 
	 * @param lyrics
	 *            the new lyrics
	 */
	public void setNewLyrics(String lyrics) {
		this.newLyrics.setText(lyrics);
		this.lyricsChB.setSelected(lyrics == null ? false : lyrics.trim().length() != 0);
	}

	/**
	 * sets the artist and title label
	 * 
	 * @param artist
	 *            the artist
	 * @param album
	 *            the album
	 */
	public void setArtist(String artist, String album) {
		this.artistJL.setText(artist + " - " + album);
	}
}