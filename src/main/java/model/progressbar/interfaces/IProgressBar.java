package model.progressbar.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

import com.cf.mls.MLS;

import config.Config;

abstract public class IProgressBar extends Thread {

	/**
	 * the multi language supporter
	 */
	protected MLS mls;

	/**
	 * the cancel button
	 */
	protected JButton cancelB;

	/**
	 * the cancel command
	 */
	private ICancelCommand cancelCommand;

	/**
	 * Constructor
	 */
	public IProgressBar() {
		mls = new MLS("view/languageFiles/Progressbar", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
		cancelB = mls.generateJButton("cancelB");
		cancelB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.getLogger(this.getClass().getName()).log(Level.FINER, "cancel button pressed.");
				if (cancelCommand == null)
					return;

				cancelCommand.call();
			}
		});
	}

	/**
	 * stops the progressbar
	 */
	abstract public void stopBar();

	/**
	 * called when the next step should be used
	 */
	abstract public void nextStep();

	/**
	 * sets the max value of something
	 * 
	 * @param max
	 *            the value
	 */
	abstract public void setMax(int max);

	public void setCancelCommand(ICancelCommand cmd) {
		this.cancelCommand = cmd;
	}
}