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

public class AnalyseAlbumGainProgressBar extends MP3GainThread {

	/**
	 * the given inputstream
	 */
	private InputStream is;

	/**
	 * max number of files
	 */
	private int files;

	/**
	 * close delay
	 */
	private int delay;

	/**
	 * used regex for the inputstream lines
	 */
	private Regex r;

	/**
	 * Constructor
	 * 
	 * @param files
	 *            number of files
	 * @param delay
	 *            closing delay
	 */
	public AnalyseAlbumGainProgressBar(int files, int delay) {
		super();

		this.files = files;
		this.delay = delay;

		if (files == 1) { // 92% of 8346050 bytes analyzed
			r = new Regex("((\\d*)%.*)", Pattern.DOTALL);
		} else { // [2/2] 92% of 8346050 bytes analyzed
			r = new Regex("\\[(\\d*)/\\d*\\] (\\d*)%.*", Pattern.DOTALL);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
			String line = br.readLine();
			line = br.readLine();
			// 2nd line is null or empty -> nothing to analyse -> dont create
			// window
			if (line == null || line.trim().length() == 0)
				return;

			// generate window
			JProgressBar pb = new JProgressBar(0, files * 100);
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

			int currFile = 0;
			pb.setValue(0);

			while ((line = br.readLine()) != null) {
				if (r.matches(line.trim())) {
					if (this.files > 1) {
						currFile = Integer.parseInt(r.getGroup(1)) - 1;
						pb.setValue(Integer.parseInt(r.getGroup(2)) + currFile * 100);
						statusJL.setText(line.trim());
					} else {
						pb.setValue(Integer.parseInt(r.getGroup(2)));
						statusJL.setText(line.trim());
					}
				}
			}

			pb.setValue(files * 100);

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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.progressbar.interfaces.MP3GainThread#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return false;
	}
}