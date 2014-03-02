package view.subview;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import model.util.FileUtil;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class LicenseView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8623941845716021761L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;
	/**
	 * the contentPane
	 */
	private JPanel contentPane;

	/**
	 * Constructor
	 */
	public LicenseView() {
		mls = new MLS("view/languageFiles/LicenseView", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	/**
	 * inits the window
	 * 
	 * @throws IOException
	 *             thrown if an image couldn't be found
	 */
	public void init() throws IOException {
		mls.addJFrame("window", this);

		contentPane = new JPanel();
		JTabbedPane tabP = mls.generateJTabbedPane("tabP", true);
		setContentPane(contentPane);

		JPanel softwareP = generatePanel(mls.getMessage("thisSoftwareAuthor"), mls.getMessage("thisSoftwareProject"), mls.getMessage("thisSoftwareProjectURL"),
				mls.getMessage("thisSoftwareLicensePath"), mls.getMessage("thisSoftwareLicense"));
		JPanel jtattoP = generatePanel(mls.getMessage("jtattooAuthor"), mls.getMessage("jtattooProject"), mls.getMessage("jtattooProjectURL"),
				mls.getMessage("jtattooLicensePath"), mls.getMessage("jtattooLicense"));
		JPanel mp3gainP = generatePanel(mls.getMessage("mp3gainAuthor"), mls.getMessage("mp3gainProject"), mls.getMessage("mp3gainProjectURL"),
				mls.getMessage("mp3gainLicensePath"), mls.getMessage("mp3gainLicense"));
		JPanel miglayoutP = generatePanel(mls.getMessage("miglayoutAuthor"), mls.getMessage("miglayoutProject"), mls.getMessage("miglayoutProjectURL"),
				mls.getMessage("miglayoutLicensePath"), mls.getMessage("miglayoutLicense"));
		JPanel jlayerP = generatePanel(mls.getMessage("jLayerAuthor"), mls.getMessage("jLayerProject"), mls.getMessage("jLayerProjectURL"), mls.getMessage("jLayerLicensePath"),
				mls.getMessage("jLayerLicense"));
		JPanel humano2P = generatePanel(mls.getMessage("humano2Author"), mls.getMessage("humano2Project"), mls.getMessage("humano2ProjectURL"),
				mls.getMessage("humano2LicensePath"), mls.getMessage("humano2License"));
		JPanel shimmerP = generatePanel(mls.getMessage("shimmerAuthor"), mls.getMessage("shimmerProject"), mls.getMessage("shimmerProjectURL"),
				mls.getMessage("shimmerLicensePath"), mls.getMessage("shimmerLicense"));
		JPanel oxygenP = generatePanel(mls.getMessage("oxygenAuthor"), mls.getMessage("oxygenProject"), mls.getMessage("oxygenProjectURL"), mls.getMessage("oxygenLicensePath"),
				mls.getMessage("oxygenLicense"));
		JPanel visualpharmP = generatePanel(mls.getMessage("visualpharmAuthor"), mls.getMessage("visualpharmProject"), mls.getMessage("visualpharmProjectURL"),
				mls.getMessage("visualpharmLicensePath"), mls.getMessage("visualpharmLicense"));
		JPanel vistaicoP = generatePanel(mls.getMessage("vistaicoAuthor"), mls.getMessage("vistaicoProject"), mls.getMessage("vistaicoProjectURL"),
				mls.getMessage("vistaicoLicensePath"), mls.getMessage("vistaicoLicense"));
		JPanel fatcowP = generatePanel(mls.getMessage("fatcowAuthor"), mls.getMessage("fatcowProject"), mls.getMessage("fatcowProjectURL"), mls.getMessage("fatcowLicensePath"),
				mls.getMessage("fatcowLicense"));
		JPanel flagiconsP = generatePanel(mls.getMessage("flagiconsAuthor"), mls.getMessage("flagiconsProject"), mls.getMessage("flagiconsProjectURL"),
				mls.getMessage("flagiconsLicensePath"), mls.getMessage("flagiconsLicense"));

		// add tabs
		mls.addTab(tabP, softwareP, "softwareTab");
		mls.addTab(tabP, mp3gainP, "mp3gainTab");
		mls.addTab(tabP, jtattoP, "jtattooTab");
		mls.addTab(tabP, miglayoutP, "miglayoutTab");
		mls.addTab(tabP, jlayerP, "jlayerTab");
		mls.addTab(tabP, humano2P, "humano2Tab");
		mls.addTab(tabP, shimmerP, "shimmerTab");
		mls.addTab(tabP, oxygenP, "oxygenTab");
		mls.addTab(tabP, visualpharmP, "visualPharmTab");
		mls.addTab(tabP, vistaicoP, "vistaicoTab");
		mls.addTab(tabP, fatcowP, "fatcowTab");
		mls.addTab(tabP, flagiconsP, "flagiconsTab");

		contentPane.setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
		contentPane.add(tabP, "grow");

		setMinimumSize(new Dimension(650, 700));
		setPreferredSize(new Dimension(650, 700));
		pack();
	}

	/**
	 * generates a new Tab Panel with the given licence information
	 * 
	 * @param author
	 *            author of the project
	 * @param project
	 *            project name
	 * @param projectLink
	 *            project link
	 * @param licensePath
	 *            path to the license inside the jar file of the form
	 *            path/to/license
	 * @param licenseTag
	 *            the tag e.g. GPL, CreativeCommons etc. which is defined in the
	 *            langaugefile
	 * 
	 * @return the created panel
	 * 
	 * @throws IOException
	 *             thrown if the license couldn't be found
	 */
	private JPanel generatePanel(String author, String project, String projectLink, String licensePath, String licenseTag) throws IOException {

		JPanel resultP = new JPanel();
		resultP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "License:"));
		JPanel infoP = new JPanel();
		infoP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "Information:"));
		JPanel licenseP = new JPanel();
		licenseP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), "License Text:"));

		JTextArea licenseTA = new JTextArea(licensePath != null ? FileUtil.readFileFromJar(licensePath) : "");

		JLabel authorJL = new JLabel("Author:");
		JLabel authorTextJL = new JLabel(author);
		JLabel projectJL = new JLabel("Project:");
		JLabel projectTextJL = new JLabel(project.replace("{0}", projectLink));
		setLink(projectTextJL, projectLink);
		JLabel licenceJL = new JLabel("License:");
		JLabel licenceTextJL = new JLabel("<html>" + licenseTag + " <a href=\"" + mls.getMessage(licenseTag) + "\">(link)</a></html></html>");
		setLink(licenceTextJL, mls.getMessage(licenseTag));

		// own software
		resultP.setLayout(new MigLayout("insets 5", "[grow]", "[shrink][grow]"));

		// // information
		infoP.setLayout(new MigLayout("insets 5", "[shrink][grow]", "[shrink][shrink][shrink]"));
		infoP.add(authorJL);
		infoP.add(authorTextJL, "grow, gapleft 10, wrap");
		infoP.add(projectJL, "gaptop 10");
		infoP.add(projectTextJL, "grow, gapleft 10, gaptop 10, wrap");
		infoP.add(licenceJL, "gaptop 10");
		infoP.add(licenceTextJL, "grow, gapleft 10, gaptop 10");

		resultP.add(infoP, "grow, wrap");

		// // license
		licenseP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		JScrollPane sp = new JScrollPane();
		sp.setViewportView(licenseTA);
		licenseP.add(sp, "grow");
		resultP.add(licenseP, "grow");

		return resultP;
	}

	/**
	 * sets the given url as link to the given label
	 * 
	 * @param label
	 *            given label
	 * @param url
	 *            given url to set to the label
	 */
	private void setLink(JLabel label, final String url) {
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (URISyntaxException | IOException ex) {
					// It looks like there's a problem
				}
			}
		});
	}
}