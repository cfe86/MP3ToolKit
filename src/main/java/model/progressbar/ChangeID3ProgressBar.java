package model.progressbar;

import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import model.progressbar.interfaces.IProgressBar;
import model.util.WindowUtils;

public class ChangeID3ProgressBar extends IProgressBar {

	/**
	 * number of files
	 */
	private int files;

	/**
	 * closing delay
	 */
	private int delay;

	/**
	 * the current file
	 */
	private int currFile;

	/**
	 * true if progressbar is enabled, else false
	 */
	private boolean en;

	/**
	 * Constructor
	 * 
	 * @param files
	 *            number of files
	 */
	public ChangeID3ProgressBar(int files) {
		super();

		this.files = files;
		this.delay = 300;
	}

	/**
	 * Constructor
	 * 
	 * @param files
	 *            number of files
	 * @param delay
	 *            closing delay
	 */
	public ChangeID3ProgressBar(int files, int delay) {
		this.files = files;
		this.delay = delay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		JProgressBar pb = new JProgressBar(0, files);
		final JFrame frame = new JFrame();
		JLabel statusJL = mls.generateJLabel("statusJL");
		JLabel progressJL = mls.generateJLabel("progressJL");

		frame.setPreferredSize(new Dimension(350, 140));
		frame.setLayout(new MigLayout("insets 5", "[grow][shrink]", "[shrink][grow][shrink]"));
		frame.add(progressJL, "span 2, wrap");
		frame.add(pb, "grow");
		frame.add(cancelB, "gapleft 5, grow, wrap");
		frame.add(statusJL, "span 2");

		frame.pack();
		pb.setStringPainted(true);
		frame.setVisible(true);

		frame.setLocation(WindowUtils.getCenteredWindowCoordinates(frame));

		currFile = 0;
		pb.setValue(0);
		statusJL.setText("file: " + currFile + "/" + files);

		en = true;
		while (en) {
			pb.setValue(currFile);
			statusJL.setText("file: " + currFile + "/" + files);
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				frame.dispose();
			}
		}, delay);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#stopBar()
	 */
	@Override
	public void stopBar() {
		en = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#nextStep()
	 */
	@Override
	public void nextStep() {
		currFile++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#setMax(int)
	 */
	@Override
	public void setMax(int max) {

	}
}