package model.progressbar;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import model.progressbar.interfaces.MP3GainThread;
import model.regex.Regex;
import model.util.WindowUtils;

public class AnalyseTrackGainProgressBar extends MP3GainThread {

	/**
	 * the given inputstream
	 */
	private InputStream is;

	/**
	 * the closing delay
	 */
	private int delay;

	/**
	 * the used regex for inputstream lines
	 */
	private Regex r;

	/**
	 * true if progressbar is enabled
	 */
	private boolean enabled;

	/**
	 * the progressbar
	 */
	private JProgressBar pb;

	/**
	 * Constructor
	 * 
	 * @param delay
	 *            the closing delay
	 */
	public AnalyseTrackGainProgressBar(int delay) {
		super();

		this.delay = delay;
		this.enabled = false;
		r = new Regex("((\\d*)%.*)", Pattern.DOTALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			// generate window
			pb = new JProgressBar(0, 100);
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

			Point p = WindowUtils.getCenteredWindowCoordinates(frame);
			frame.setLocation(p);

			pb.setValue(0);
			this.enabled = true;
			while (enabled) {
				BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
				String line = br.readLine();
				while ((line = br.readLine()) != null) {
					if (r.matches(line.trim())) {
						pb.setValue(Integer.parseInt(r.getGroup(2)));
						statusJL.setText(line.trim());
					}
				}

				pb.setValue(100);
			}

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					frame.dispose();
				}
			}, delay);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * model.progressbar.interfaces.MP3GainThread#setInputStream(java.io.InputStream
	 * )
	 */
	@Override
	public void setInputStream(InputStream is) {
		// unused;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * model.progressbar.interfaces.MP3GainThread#setErrorStream(java.io.InputStream
	 * )
	 */
	@Override
	public void setErrorStream(InputStream es) {
		this.is = es;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#stopBar()
	 */
	@Override
	public void stopBar() {
		this.enabled = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#nextStep()
	 */
	@Override
	public void nextStep() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.IProgressBar#setMax(int)
	 */
	@Override
	public void setMax(int max) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.MP3GainThread#reset()
	 */
	@Override
	public void reset() {
		pb.setValue(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.MP3GainThread#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return enabled;
	}
}