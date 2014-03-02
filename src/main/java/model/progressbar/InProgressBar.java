package model.progressbar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import model.progressbar.interfaces.IProgressBar;
import model.util.WindowUtils;

public class InProgressBar extends IProgressBar {

	/**
	 * true if enabled, else false
	 */
	private boolean enable;
	/**
	 * delay between every dot
	 */
	private int dotDelay;
	/**
	 * delay after progressbar is stopped when it fades
	 */
	private int closeDelay;
	/**
	 * the step
	 */
	private int step;
	/**
	 * strings which should be displayed at each step
	 */
	private String[] steps;

	/**
	 * Constructor, creates a bar with 400ms dot delay and 200ms close delay
	 */
	public InProgressBar() {
		this.dotDelay = 400;
		this.closeDelay = 200;
		step = 0;
		steps = new String[] { "in progress" };
	}

	/**
	 * creates a bar
	 * 
	 * @param dotDelay
	 *            delay between the dots in inprogress ... <- dots
	 * @param closeDelay
	 *            the delay the bar should be closed after stopbar is called
	 */
	public InProgressBar(int dotDelay, int closeDelay) {
		this.dotDelay = dotDelay;
		this.closeDelay = closeDelay;

		step = 0;
		steps = new String[] { "in progress" };
	}

	/**
	 * creates a bar
	 * 
	 * @param steps
	 *            the strings which should be shown at each steap
	 * @param dotDelay
	 *            delay between the dots in inprogress ... <- dots
	 * @param closeDelay
	 *            the delay the bar should be closed after stopbar is called
	 */
	public InProgressBar(String[] steps, int dotDelay, int closeDelay) {
		this.dotDelay = dotDelay;
		this.closeDelay = closeDelay;

		step = 0;
		this.steps = steps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		enable = true;
		final JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(250, 150));
		JLabel label = new JLabel(steps[step]);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add(label, BorderLayout.CENTER);

		frame.setLocation(WindowUtils.getCenteredWindowCoordinates(frame));

		frame.pack();
		frame.setVisible(true);

		int dots = 0;
		while (enable) {
			label.setText(steps[step] + " " + makeDots(dots));
			dots++;
			dots = dots % 4;
			try {
				Thread.sleep(dotDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		step++;
	}

	/**
	 * depending on the given number prints nothing, ., .. or ...
	 * 
	 * @param num
	 *            number of dots 0-3
	 * 
	 * @return the dot string
	 */
	private String makeDots(int num) {
		switch (num) {
			case 0:
				return "";
			case 1:
				return ".";
			case 2:
				return "..";
			case 3:
				return "...";
		}

		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.IProgressBar#stopBar()
	 */
	@Override
	public void stopBar() {
		enable = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#setMax(int)
	 */
	@Override
	public void setMax(int max) {
		// unused
	}
}