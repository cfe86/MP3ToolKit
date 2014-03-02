package main;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logging.LogUtil;
import config.Config;
import config.Constants;
import controller.MainController;

public class MP3ToolKitMain {

	/**
	 * @param args
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SecurityException, IOException {

//		currWidth=1193
//		currHeight=923
		
		if (Constants.DEBUG)
			LogUtil.initLogging();
		else
			LogUtil.disableLogging();

		Config.init();

		UIManager.setLookAndFeel(Config.getInstance().getCurrentSkin());

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainController con = new MainController();
				con.createWindow();
			}
		});
	}
}