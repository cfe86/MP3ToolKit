package view.subview;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import logging.LogUtil;
import logging.TextAreaHandler;
import net.miginfocom.swing.MigLayout;
import config.Constants;

public class DebugFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3297680578793436535L;

	/**
	 * true if open, else false
	 */
	private static boolean isOpen = false;

	private JTextArea ta;
	private JPanel contentPane;
	private JButton saveButton;

	/**
	 * Constructor
	 * 
	 * @throws RuntimeException
	 *             thrown if already one debug frame is open
	 */
	public DebugFrame() throws RuntimeException {
		if (isOpen)
			throw new RuntimeException("");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
	}

	/**
	 * closes the window
	 */
	private void closeWindow() {
		isOpen = false;
		TextAreaHandler.setTextArea(null);
	}

	/**
	 * inits the window
	 */
	public void init() {
		setTitle("Debug");
		contentPane = new JPanel();
		this.ta = new JTextArea();
		// TA should always follow text
		((DefaultCaret) this.ta.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		this.saveButton = new JButton("save log");

		setContentPane(contentPane);

		JScrollPane sp = new JScrollPane();
		sp.setViewportView(ta);

		contentPane.setLayout(new MigLayout("insets 5", "[grow]", "[grow][shrink]"));
		contentPane.add(sp, "grow, wrap");
		contentPane.add(saveButton, "grow");

		setMinimumSize(new Dimension(500, 500));
		pack();

		this.saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				File file = null;
				// create JFileChooser
				JFileChooser fc = new JFileChooser();

				int returnVal = fc.showOpenDialog(null);

				// get File and parse it
				if (returnVal == JFileChooser.APPROVE_OPTION)
					file = fc.getSelectedFile();

				if (file == null) {
					JOptionPane.showMessageDialog(null, "no file chosen.");
					return;
				}

				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write(ta.getText());
					bw.close();

					JOptionPane.showMessageDialog(null, "log written!");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error while writing log:\n" + LogUtil.getStackTrace(e));
					e.printStackTrace();
				}
			}
		});

		this.ta.setEditable(false);
		if (!Constants.DEBUG)
			this.ta.setText("Debug is turned off completely. There are no debug logs available.");

		TextAreaHandler.setTextArea(ta);
		isOpen = true;
	}
}