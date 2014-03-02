package view.subview;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.util.WindowUtils;
import net.miginfocom.swing.MigLayout;

import com.cf.mls.MLS;

import config.Config;

public class HelpDialogView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the multi language supporter
	 */
	private MLS mls;

	private JPanel contentPane;

	/**
	 * Constructor
	 */
	public HelpDialogView() {
		this.mls = new MLS("view/languageFiles/HelpDialog", Config.getInstance().getCurrentLanguage());
		this.mls.setToolTipDuration(-1);
	}

	/**
	 * inits the help dialog with the given text
	 * 
	 * @param txt
	 *            given text
	 */
	public void init(String txt) {
		JPanel panelP = mls.generateTitledPanel("panelP");

		contentPane = new JPanel(new MigLayout("insets 0", "[grow]", "[grow]"));
		getContentPane().add(contentPane);

		panelP.setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));

		JScrollPane scrollPane = new JScrollPane();
		panelP.add(scrollPane, "grow");

		JEditorPane helpEP = new JEditorPane();
		scrollPane.setViewportView(helpEP);
		helpEP.setContentType("text/html");
		helpEP.setText(txt);
		helpEP.setEnabled(false);

		contentPane.add(panelP, "grow");

		setMinimumSize(new Dimension(300, 300));
		setPreferredSize(new Dimension(400, 400));

		pack();

		// center frame
		setLocation(WindowUtils.getCenteredWindowCoordinates(this));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
}