package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import config.Config;
import config.Constants;
import controller.interfaces.ICommand;
import controller.interfaces.ICommandEnableWindow;
import controller.subcontroller.PreferencesController;
import logging.LogUtil;
import manager.CollectorManager;
import manager.TabManager;
import manager.structure.Controller;
import model.MainModel;
import model.exception.ControllerInitException;
import model.exception.TabInitException;
import model.progressbar.InProgressBar;
import model.progressbar.interfaces.IProgressBar;
import model.util.PathUtil;
import model.util.WindowUtils;
import view.MainWindow;
import view.subview.AboutView;
import view.subview.DebugFrame;
import view.subview.LicenseView;

public class MainController extends Observable implements ActionListener, Observer {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private MainModel model;

	/**
	 * the window
	 */
	private MainWindow window;

	/**
	 * the controllers for the tabs, maps identifier to controller
	 */
	private Map<String, Controller> controller;

	/**
	 * Constructor
	 */
	public MainController() {
		model = new MainModel();
		controller = TabManager.getInstance().getController();
	}

	/**
	 * create window
	 */
	public void createWindow() {
		window = new MainWindow();

		try {
			window.init();
		} catch (TabInitException e) {
			logger.log(Level.SEVERE, "Error while init main window:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("InitError");
		}

		// init tabs
		for (Controller con : this.controller.values()) {
			try {
				// add maincontroller as observer is needed
				if (con.observeMain())
					con.getController().addObserver(this);

				// add enable or disable command
				con.getController().setMainWindowCommand(new ICommandEnableWindow() {

					@Override
					public void setWindowEnabled(boolean en) {
						window.setEnabled(en);
						
						if (en)
							window.toFront();
					}
				});

				// init controller
				con.getController().init(window.getTab(con.getTabIndex()));
			} catch (ControllerInitException e) {
				logger.log(Level.SEVERE, "Error while init Controller:\n" + LogUtil.getStackTrace(e), e);
				window.showMessage("InitError");
			}
		}

		window.setVisible(true);

		window.setActionListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});

		updateMI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		logger.log(Level.FINER, "got message from " + o.getClass().getName());

		// preferences
		if (o.getClass().getName().equals(controller.subcontroller.PreferencesController.class.getName())) {
			window.setEnabled(true);
		}
	}

	/**
	 * closes the window
	 */
	public void closeWindow() {
		// save controller settings
		for (Controller con : this.controller.values()) {
			con.getController().saveConfig();
		}

		// save collector
		CollectorManager.getInstance().saveConfig();

		try {
			Config.getInstance().setCurrDimension(window.getCurrentSize());
			Config.getInstance().writeConfig();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while writing config:\n" + LogUtil.getStackTrace(e), e);
			int r = window.showConfirmationMessage("writeError");
			if (r != JOptionPane.YES_OPTION)
				return;
		}

		System.exit(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("hifiLookAndFeelMI"))
			hifiMI();
		else if (e.getActionCommand().equals("acrylLookAndFeelMI"))
			acrylMI();
		else if (e.getActionCommand().equals("aeroLookAndFeelMI"))
			aeroMI();
		else if (e.getActionCommand().equals("aluminiumLookAndFeelMI"))
			aluminiumMI();
		else if (e.getActionCommand().equals("bernsteinLookAndFeelMI"))
			bernsteinMI();
		else if (e.getActionCommand().equals("fastLookAndFeelMI"))
			fastMI();
		else if (e.getActionCommand().equals("graphiteLookAndFeelMI"))
			graphiteMI();
		else if (e.getActionCommand().equals("lunaLookAndFeelMI"))
			lunaMI();
		else if (e.getActionCommand().equals("mcWinLookAndFeelMI"))
			mcWinMI();
		else if (e.getActionCommand().equals("mintLookAndFeelMI"))
			mintMI();
		else if (e.getActionCommand().equals("noireLookAndFeelMI"))
			noireMI();
		else if (e.getActionCommand().equals("smartLookAndFeelMI"))
			smartMI();
		else if (e.getActionCommand().equals("textureLookAndFeelMI"))
			textureMI();
		else if (e.getActionCommand().equals("quitMI"))
			quitMI();
		else if (e.getActionCommand().equals("settingsMI"))
			settingsMI();
		else if (e.getActionCommand().equals("updateMI"))
			updateMI();
		else if (e.getActionCommand().equals("debugMI"))
			debugMI();
		else if (e.getActionCommand().equals("donateMI"))
			donateMI();
		else if (e.getActionCommand().equals("licenseMI"))
			licenseMI();
		else if (e.getActionCommand().equals("aboutMI"))
			aboutMI();
	}

	/**
	 * selects hifi skin
	 */
	private void hifiMI() {
		logger.log(Level.FINER, "changed Skin to Hifi.");
		com.jtattoo.plaf.hifi.HiFiLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
	}

	/**
	 * selects acryl skin
	 */
	private void acrylMI() {
		logger.log(Level.FINER, "changed Skin to Acryl.");
		com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
	}

	/**
	 * selects aero skin
	 */
	private void aeroMI() {
		logger.log(Level.FINER, "changed Skin to Aero.");
		com.jtattoo.plaf.aero.AeroLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.aero.AeroLookAndFeel");
	}

	/**
	 * selects aluminium skin
	 */
	private void aluminiumMI() {
		logger.log(Level.FINER, "changed Skin to Aluminium.");
		com.jtattoo.plaf.aluminium.AluminiumLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
	}

	/**
	 * selects bernstein skin
	 */
	private void bernsteinMI() {
		logger.log(Level.FINER, "changed Skin to Bernstein.");
		com.jtattoo.plaf.bernstein.BernsteinLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
	}

	/**
	 * selects fast skin
	 */
	private void fastMI() {
		logger.log(Level.FINER, "changed Skin to Fast.");
		com.jtattoo.plaf.fast.FastLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.fast.FastLookAndFeel");
	}

	/**
	 * selects graphite skin
	 */
	private void graphiteMI() {
		logger.log(Level.FINER, "changed Skin to Graphite.");
		com.jtattoo.plaf.graphite.GraphiteLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
	}

	/**
	 * selects luna skin
	 */
	private void lunaMI() {
		logger.log(Level.FINER, "changed Skin to Luna.");
		com.jtattoo.plaf.luna.LunaLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.luna.LunaLookAndFeel");
	}

	/**
	 * selects mcWin skin
	 */
	private void mcWinMI() {
		logger.log(Level.FINER, "changed Skin to McWin.");
		com.jtattoo.plaf.mcwin.McWinLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
	}

	/**
	 * selects mint skin
	 */
	private void mintMI() {
		logger.log(Level.FINER, "changed Skin to Mint.");
		com.jtattoo.plaf.mint.MintLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.mint.MintLookAndFeel");
	}

	/**
	 * selects noire skin
	 */
	private void noireMI() {
		logger.log(Level.FINER, "changed Skin to Noire.");
		com.jtattoo.plaf.noire.NoireLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.noire.NoireLookAndFeel");
	}

	/**
	 * selects smart skin
	 */
	private void smartMI() {
		logger.log(Level.FINER, "changed Skin to Smart.");
		com.jtattoo.plaf.smart.SmartLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.smart.SmartLookAndFeel");
	}

	/**
	 * selects texture skin
	 */
	private void textureMI() {
		logger.log(Level.FINER, "changed Skin to Texture.");
		com.jtattoo.plaf.texture.TextureLookAndFeel.setTheme("Default");
		changeSkin("com.jtattoo.plaf.texture.TextureLookAndFeel");
	}

	/**
	 * quit pressed
	 */
	private void quitMI() {
		logger.log(Level.FINER, "Quit pressed.");
		closeWindow();
	}

	/**
	 * settings pressed
	 */
	private void settingsMI() {
		logger.log(Level.FINER, "Settings pressed.");

		window.setEnabled(false);
		PreferencesController con = new PreferencesController();
		con.setCloseCommand(new ICommand() {

			@Override
			public void call() {
				window.setEnabled(true);
			}
		});
		con.createWindow();
	}

	/**
	 * update pressed
	 */
	private void updateMI() {
		logger.log(Level.FINER, "Update pressed.");
		// check for updates
		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar pb = new InProgressBar(new String[] { "checking for updates" }, 400, 200);
				pb.start();
				try {
					model.getUpdateInfo();
					boolean isUpdate = model.isUpdateAvailable();
					pb.stopBar();
					if (!isUpdate)
						return;

					int r = window.showConfirmationMessage("softwareUpdateAvaiable");

					if (r == JOptionPane.YES_OPTION) {
						model.writeUpdater();
						model.updateSoftware(PathUtil.getJarPath(MainController.class), window.getCurrentLanguage().toString());
						System.exit(1);
					}
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Error while updating:\n" + LogUtil.getStackTrace(e), e);
					pb.stopBar();
					window.showMessage("noInternet");
				}
			}
		}).start();
	}

	/**
	 * debug pressed, only available if debug is enabled
	 */
	private void debugMI() {
		logger.log(Level.FINER, "Debug pressed.");
		try {
			DebugFrame w = new DebugFrame();
			w.init();
			w.setVisible(true);
		} catch (RuntimeException e) {
			logger.log(Level.SEVERE, "tried to open debug console twice.");
		}
	}

	/**
	 * donate pressed
	 */
	private void donateMI() {
		logger.log(Level.FINER, "Donate pressed");
		try {
			int r = window.showConfirmationMessage("openDonationURL");
			if (r != JOptionPane.YES_OPTION)
				return;

			java.awt.Desktop.getDesktop().browse(java.net.URI.create(Constants.donateURL));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while opening url:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("urlopenError");
		}
	}

	/**
	 * license pressed
	 */
	private void licenseMI() {
		logger.log(Level.FINER, "License pressed.");
		try {
			LicenseView v = new LicenseView();
			v.init();
			v.setLocation(WindowUtils.getCenteredWindowCoordinates(v));
			v.setVisible(true);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while opening license window:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("licenseError");
		}
	}

	/**
	 * about pressed
	 */
	private void aboutMI() {
		logger.log(Level.FINER, "About pressed.");

		AboutView v = new AboutView();
		v.init();
		v.setLocation(WindowUtils.getCenteredWindowCoordinates(v));
		v.setVisible(true);
	}

	/**
	 * changes the skin to the given skin
	 * 
	 * @param skin
	 *            the given look and feel string
	 */
	private void changeSkin(String skin) {
		logger.log(Level.FINER, "changed Skin: " + skin);
		try {
			Config.getInstance().setcurrentSkin(skin);
			UIManager.setLookAndFeel(skin);
			SwingUtilities.updateComponentTreeUI(window);
			window.pack();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

		}
	}
}