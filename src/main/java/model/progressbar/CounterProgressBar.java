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

public class CounterProgressBar extends IProgressBar {

	/**
	 * true if progressbar is running, else false
	 */
	private boolean en;

	/**
	 * closing delay
	 */
	private int closeDelay;

	/**
	 * the current file
	 */
	private int currFile;

	/**
	 * max number of files
	 */
	private int maxFiles;

	/**
	 * Constructor
	 */
	public CounterProgressBar() {
		super();
		this.closeDelay = 200;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// if less than 10 files, dont show a progress bar
		if (maxFiles < 10)
			return;

		JProgressBar pb = new JProgressBar(0, maxFiles);
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
		frame.pack();

		frame.setVisible(true);
		frame.setLocation(WindowUtils.getCenteredWindowCoordinates(frame));

		currFile = 0;
		pb.setValue(0);
		statusJL.setText("file: " + currFile + "/" + maxFiles);

		en = true;
		while (en) {
			pb.setValue(currFile);
			statusJL.setText("file: " + currFile + "/" + maxFiles);
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				frame.dispose();
			}
		}, closeDelay);
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
	 * @see model.progressbar.IProgressBar#stopBar()
	 */
	@Override
	public void stopBar() {
		en = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#setMax(int)
	 */
	@Override
	public void setMax(int max) {
		this.maxFiles = max;
	}
}