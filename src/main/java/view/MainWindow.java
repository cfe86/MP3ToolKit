package view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import view.interfaces.AbstractTab;
import manager.TabManager;
import manager.structure.Tab;
import model.exception.TabInitException;
import model.util.WindowUtils;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;
import com.cf.mls.extension.LanguageMenuExtension;

import config.Config;
import config.Constants;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	/**
	 * all tabs
	 */
	private List<Tab> tabs;

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JMenu languageM;
	private JMenu fileM;
	private JMenu helpM;
	private JMenu skinsM;
	private JMenu settingsM;
	private JMenuItem hifiLookAndFeelMI;
	private JMenuItem acrylLookAndFeelMI;
	private JMenuItem aeroLookAndFeelMI;
	private JMenuItem aluminiumLookAndFeelMI;
	private JMenuItem bernsteinLookAndFeelMI;
	private JMenuItem FastLookAndFeelMI;
	private JMenuItem graphiteLookAndFeelMI;
	private JMenuItem lunaLookAndFeelMI;
	private JMenuItem mcWinLookAndFeelMI;
	private JMenuItem mintLookAndFeelMI;
	private JMenuItem noireLookAndFeelMI;
	private JMenuItem smartLookAndFeelMI;
	private JMenuItem textureLookAndFeelMI;
	private JMenuItem quitMI;
	private JMenuItem settingsMI;
	private JMenuItem updateMI;
	private JMenuItem debugMI;
	private JMenuItem donateMI;
	private JMenuItem licenseMI;
	private JMenuItem aboutMI;

	/**
	 * Constructor
	 */
	public MainWindow() {
		mls = new MLS("view/languageFiles/MainWindow", Config.getInstance().getCurrentLanguage());
		mls.setConfigPath("view/mls.conf");
		mls.setToolTipDuration(-1);

		this.tabs = TabManager.getInstance().getTabs();
	}

	/**
	 * inits the window
	 * 
	 * @throws TabInitException
	 *             thrown if a tab couldn't be initialised
	 */
	public void init() throws TabInitException {
		mls.addJFrame("window", this);

		languageM = mls.generateJMenu("languageM");
		fileM = mls.generateJMenu("fileM");
		helpM = mls.generateJMenu("helpM");
		settingsM = mls.generateJMenu("settingsM");
		skinsM = mls.generateJMenu("skinsM");
		hifiLookAndFeelMI = mls.generateJMenuItem("hifiLookAndFeelMI");
		acrylLookAndFeelMI = mls.generateJMenuItem("acrylLookAndFeelMI");
		aeroLookAndFeelMI = mls.generateJMenuItem("aeroLookAndFeelMI");
		aluminiumLookAndFeelMI = mls.generateJMenuItem("aluminiumLookAndFeelMI");
		bernsteinLookAndFeelMI = mls.generateJMenuItem("bernsteinLookAndFeelMI");
		FastLookAndFeelMI = mls.generateJMenuItem("fastLookAndFeelMI");
		graphiteLookAndFeelMI = mls.generateJMenuItem("graphiteLookAndFeelMI");
		lunaLookAndFeelMI = mls.generateJMenuItem("lunaLookAndFeelMI");
		mcWinLookAndFeelMI = mls.generateJMenuItem("mcWinLookAndFeelMI");
		mintLookAndFeelMI = mls.generateJMenuItem("mintLookAndFeelMI");
		noireLookAndFeelMI = mls.generateJMenuItem("noireLookAndFeelMI");
		smartLookAndFeelMI = mls.generateJMenuItem("smartLookAndFeelMI");
		textureLookAndFeelMI = mls.generateJMenuItem("textureLookAndFeelMI");
		quitMI = mls.generateJMenuItem("quitMI");
		settingsMI = mls.generateJMenuItem("settingsMI");
		updateMI = mls.generateJMenuItem("updateMI");
		debugMI = mls.generateJMenuItem("debugMI");
		donateMI = mls.generateJMenuItem("donateMI");
		licenseMI = mls.generateJMenuItem("licenseMI");
		aboutMI = mls.generateJMenuItem("aboutMI");

		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		menubar.add(fileM);
		menubar.add(skinsM);
		menubar.add(languageM);
		menubar.add(settingsM);
		menubar.add(helpM);

		fileM.add(quitMI);

		skinsM.add(acrylLookAndFeelMI);
		skinsM.add(aeroLookAndFeelMI);
		skinsM.add(aluminiumLookAndFeelMI);
		skinsM.add(bernsteinLookAndFeelMI);
		skinsM.add(FastLookAndFeelMI);
		skinsM.add(graphiteLookAndFeelMI);
		skinsM.add(hifiLookAndFeelMI);
		skinsM.add(lunaLookAndFeelMI);
		skinsM.add(mcWinLookAndFeelMI);
		skinsM.add(mintLookAndFeelMI);
		skinsM.add(noireLookAndFeelMI);
		skinsM.add(smartLookAndFeelMI);
		skinsM.add(textureLookAndFeelMI);

		settingsM.add(settingsMI);
		settingsM.addSeparator();
		settingsM.add(updateMI);
		if (Constants.DEBUG) {
			settingsM.addSeparator();
			settingsM.add(debugMI);
		}

		helpM.add(donateMI);
		helpM.addSeparator();
		helpM.add(licenseMI);
		helpM.addSeparator();
		helpM.add(aboutMI);

		contentPane = new JPanel();
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(contentPane);
		setContentPane(sp);
		// setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));

		tabbedPane = mls.generateJTabbedPane("tabbedPane", true);
		contentPane.add(tabbedPane, "grow");

		// init tabs
		for (Tab t : this.tabs) {
			t.getTab().init();
			mls.addTab(tabbedPane, t.getTab(), t.getIdentifier());
		}

		mls.addCheckBoxLanguageMenuItem(languageM, mls.getLocale(), new LanguageMenuExtension() {

			@Override
			public void changeLanguage(Locale lang) {
				for (Tab t : tabs) {
					t.getTab().changeLanguage(lang);
					t.getTab().revalidate();
				}

				Config.getInstance().setCurrentLanguage(lang);
				revalidate();
				pack();
			}
		});

		setMinimumSize(new Dimension(1000, 500));
		// check resolution
		double width = Config.getInstance().getCurrDimension().getWidth();
		double height = Config.getInstance().getCurrDimension().getHeight();

		if (WindowUtils.getScreenResolution().getWidth() < width)
			width = WindowUtils.getScreenResolution().getWidth();
		if (WindowUtils.getScreenResolution().getHeight() < height)
			height = WindowUtils.getScreenResolution().getHeight() - 50;

		setPreferredSize(new Dimension((int) width, (int) height));
		pack();
	}

	/**
	 * gets the current language
	 * 
	 * @return the current locale
	 */
	public Locale getCurrentLanguage() {
		return mls.getLocale();
	}

	/**
	 * gets the current size of the window
	 * 
	 * @return the dimension
	 */
	public Dimension getCurrentSize() {
		return getSize();
	}

	/**
	 * shows a given message
	 * 
	 * @param identifier
	 *            identifier for the translator
	 */
	public void showMessage(String identifier) {
		JOptionPane.showMessageDialog(this, mls.getMessage(identifier));
	}

	/**
	 * shows a given message and asks for yes or no
	 * 
	 * @param identifier
	 *            identifier for the translator
	 * 
	 * @return the JOPtionPane.YES or NO answer
	 */
	public int showConfirmationMessage(String identifier) {
		return JOptionPane.showConfirmDialog(this, this.mls.getMessage(identifier));
	}

	/**
	 * sets an ActionListener
	 * 
	 * @param l
	 *            the ActionListener
	 */
	public void setActionListener(ActionListener l) {
		hifiLookAndFeelMI.addActionListener(l);
		acrylLookAndFeelMI.addActionListener(l);
		aeroLookAndFeelMI.addActionListener(l);
		aluminiumLookAndFeelMI.addActionListener(l);
		bernsteinLookAndFeelMI.addActionListener(l);
		FastLookAndFeelMI.addActionListener(l);
		graphiteLookAndFeelMI.addActionListener(l);
		lunaLookAndFeelMI.addActionListener(l);
		mcWinLookAndFeelMI.addActionListener(l);
		mintLookAndFeelMI.addActionListener(l);
		noireLookAndFeelMI.addActionListener(l);
		smartLookAndFeelMI.addActionListener(l);
		textureLookAndFeelMI.addActionListener(l);

		quitMI.addActionListener(l);

		settingsMI.addActionListener(l);
		updateMI.addActionListener(l);
		debugMI.addActionListener(l);

		donateMI.addActionListener(l);
		licenseMI.addActionListener(l);
		aboutMI.addActionListener(l);
	}

	/**
	 * gets the tab with the given index
	 * 
	 * @param index
	 *            the given index
	 * 
	 * @return the tab
	 */
	public AbstractTab getTab(int index) {
		return this.tabs.get(index).getTab();
	}
}