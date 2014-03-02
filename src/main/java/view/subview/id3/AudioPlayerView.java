package view.subview.id3;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;

import model.time.AudioTimer;
import model.util.Util;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class AudioPlayerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1840146857412526215L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;
	/**
	 * the contentPane
	 */
	private JPanel contentPane;

	private boolean hasHours;
	private final AudioTimer time = new AudioTimer();
	private boolean pauseTimer;

	private JLabel nameJL;
	private JLabel nameTextJL;
	private JLabel timeJL;
	private JLabel currTimeJL;
	private JLabel maxTimeJL;
	private JSlider sliderJS;
	private JButton stopB;
	private JButton startB;
	private JButton pauseB;

	/**
	 * Constructor
	 */
	public AudioPlayerView() {
		mls = new MLS("view/languageFiles/AudioPlayerView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
		pauseTimer = false;
	}

	/**
	 * inits the player window
	 * 
	 * @param shownName
	 *            the name of the file which is played
	 * @param trackLength
	 *            the length of the track in seconds
	 */
	public void init(String shownName, int trackLength) {
		mls.addJFrame("window", this);

		contentPane = new JPanel();
		nameJL = mls.generateJLabel("nameJL");
		nameTextJL = mls.generateJLabel("nameTextJL");
		timeJL = mls.generateJLabel("timeJL");
		currTimeJL = mls.generateJLabel("currTimeJL");
		maxTimeJL = mls.generateJLabel("maxTimeJL");
		sliderJS = mls.generateJSlider("sliderJS", 0, trackLength, 0);
		stopB = mls.generateJButton("stopB");
		startB = mls.generateJButton("startB");
		pauseB = mls.generateJButton("pauseB");

		// set name
		nameTextJL.setText(Util.stripExtraData(shownName));

		// set curr and max times using audio timer to format it
		time.setSongLength(trackLength);
		time.setTime(trackLength);
		this.hasHours = time.hasHours();
		time.withHours(hasHours);
		maxTimeJL.setText(time.getformattedString());

		time.setTime(0);
		currTimeJL.setText(time.getformattedString());

		setContentPane(contentPane);
		JPanel panelP = new JPanel(new MigLayout("insets 5", "[grow]", "[shrink][shrink][shrink]"));
		panelP.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		// name and time
		JPanel infoP = new JPanel(new MigLayout("insets 0", "[shrink][shrink][shrink][shrink]", "[shrink][shrink]"));
		infoP.add(nameJL);
		infoP.add(nameTextJL, "span 3, wrap 10");
		infoP.add(timeJL);
		infoP.add(currTimeJL);
		infoP.add(new JLabel("/"));
		infoP.add(maxTimeJL);

		// button
		JPanel btnP = new JPanel(new MigLayout("insets 0", "[shrink][shrink][shrink]", "[shrink]"));
		btnP.add(stopB, "push, right");
		btnP.add(pauseB, "right");
		btnP.add(startB, "right, gapright 5");

		panelP.add(infoP, "grow, wrap 15");
		panelP.add(sliderJS, "grow, wrap 10");
		panelP.add(btnP, "grow");

		contentPane.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		contentPane.add(panelP, "grow");

		setMinimumSize(new Dimension(310, 220));
		setPreferredSize(new Dimension(380, 220));
		pack();
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		stopB.addActionListener(l);
		pauseB.addActionListener(l);
		startB.addActionListener(l);
	}

	/**
	 * sets a MouseListener
	 * 
	 * @param l
	 *            the MouseListener
	 */
	public void setMouseListener(MouseListener l) {
		sliderJS.addMouseListener(l);
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
	 * startet den Timer
	 */
	public void startTimer() {
		// setze timer on
		time.start();
		pauseTimer = false;
		// ersteller Timer Thread
		final Timer timer = new Timer();
		final TimerTask task = new TimerTask() {
			public void run() {
				if (!time.isRunning() || time.isFinished()) {
					timer.cancel();
					return;
				}
				if (!pauseTimer) {
					// addiere Sekunden 1 und setze in das Label
					time.addASecond();
					setTime(time.getformattedString());
					// increase slider++
					sliderJS.setValue(sliderJS.getValue() + 1);
				}
			}

		};
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	/**
	 * sets the time to the time label
	 * 
	 * @param time
	 *            the time
	 */
	private void setTime(String time) {
		this.currTimeJL.setText(time);
	}

	/**
	 * stops the timer
	 */
	public void stopTimer() {
		this.time.stop();
		this.time.setTime(0);
		setTime(time.getformattedString());
		sliderJS.setValue(0);
		pauseTimer = false;
	}

	public boolean isPaused() {
		return pauseTimer;
	}

	public void setTimer(int sec) {
		this.time.setTime(sec);
	}

	/**
	 * pauses the timer
	 * 
	 * @param en
	 *            true for paused, else false
	 */
	public void setPauseTimer(boolean en) {
		pauseTimer = en;
	}

	public boolean isTimerFinished() {
		return this.time.isFinished();
	}

	public int getSecondCount() {
		return time.getSecondsCount();
	}

	public int getSliderTime() {
		return this.sliderJS.getValue();
	}

	public void setName(String name) {
		this.nameTextJL.setText(name);
	}
}