package model.progressbar;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;
import model.progressbar.interfaces.MP3GainThread;
import model.util.WindowUtils;

public class ChangeAlbumGainProgressBar extends MP3GainThread {

	/**
	 * the given inputstream
	 */
	private InputStream is;

	/**
	 * number of max files
	 */
	private int files;

	/**
	 * closing delay
	 */
	private int delay;

	/**
	 * Constructor
	 * 
	 * @param files
	 *            number of files
	 * @param delay
	 *            closing delay
	 */
	public ChangeAlbumGainProgressBar(int files, int delay) {
		super();

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
		try {
			// if only less than 3 files, do not open a progress bar
			if (files < 3)
				return;

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

			Point p = WindowUtils.getCenteredWindowCoordinates(frame);
			frame.setLocation(p);

			int currFile = 0;
			pb.setValue(0);
			statusJL.setText(currFile + "/" + files);
			BufferedReader br = new BufferedReader(new InputStreamReader(this.is));
			String line = br.readLine();

			while ((line = br.readLine()) != null) {
				if (line.trim().equalsIgnoreCase("done")) {
					currFile++;
					pb.setValue(currFile);
					statusJL.setText(currFile + "/" + files);
				}
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

	@Override
	public boolean isRunning() {
		return false;
	}
}