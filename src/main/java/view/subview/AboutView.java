package view.subview;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;
import config.Constants;

public class AboutView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2986741046351954091L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;
	/**
	 * the contentPane
	 */
	private JPanel contentPane;

	private JLabel nameJL;
	private JLabel versionJL;
	private JLabel versionTextJL;
	private JLabel softwareUpdateJL;
	private JLabel softwareUpdateTextJL;
	private JLabel developedJL;
	private JLabel developedTextJL;
	private JLabel copyrightJL;

	/**
	 * Constructor
	 */
	public AboutView() {
		mls = new MLS("view/languageFiles/AboutView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 */
	public void init() {

		mls.addJFrame("window", this);

		contentPane = new JPanel();
		nameJL = mls.generateJLabel("nameJL");
		nameJL.setHorizontalAlignment(SwingConstants.CENTER);
		versionJL = mls.generateJLabel("versionJL");
		versionTextJL = mls.generateJLabel("versionTextJL");
		versionTextJL.setText(versionTextJL.getText().replace("{0}", Constants.VERSION));
		softwareUpdateJL = mls.generateJLabel("softwareUpdateJL");
		softwareUpdateTextJL = mls.generateJLabel("softwareUpdateTextJL");
		softwareUpdateTextJL.setText(softwareUpdateTextJL.getText().replace("{0}", Constants.LAST_SOFTWARE_UPDATE));
		developedJL = mls.generateJLabel("developedJL");
		developedTextJL = mls.generateJLabel("developedTextJL");
		copyrightJL = mls.generateJLabel("copyrightJL");

		setContentPane(contentPane);

		JPanel panelP = new JPanel(new MigLayout("insets 5", "[shrink][grow]", "[shrink][shrink][shrink][shrink][shrink]"));
		panelP.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		panelP.add(nameJL, "span 2, grow, wrap, gapbottom 30");

		panelP.add(versionJL);
		panelP.add(versionTextJL, "grow, wrap, gapleft 10");

		panelP.add(developedJL, "gaptop 5");
		panelP.add(developedTextJL, "grow, gaptop 5, wrap, gapleft 10");

		panelP.add(softwareUpdateJL, "gaptop 5");
		panelP.add(softwareUpdateTextJL, "grow, wrap, gaptop 5, gapleft 10");

		panelP.add(copyrightJL, "span 2, grow, gaptop 40");

		contentPane.setLayout(new MigLayout("insets 10", "[grow]", "[grow]"));
		contentPane.add(panelP, "grow");

		setMinimumSize(new Dimension(400, 300));
		pack();
	}
}