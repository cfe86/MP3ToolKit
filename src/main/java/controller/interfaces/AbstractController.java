package controller.interfaces;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import model.exception.ControllerInitException;
import view.interfaces.AbstractTab;

abstract public class AbstractController extends Observable implements Observer, ActionListener, MouseListener {

	/**
	 * command to dis or enable the main window
	 */
	protected ICommandEnableWindow mainWindow;
	
	/**
	 * initialises the controller with the given new tab, in this method the
	 * listener and so on should be set. The AbstractTab can be casted here.
	 * 
	 * @param tab
	 *            the given tab panel
	 * 
	 * @throws ControllerInitException
	 *             thrown if an error during init occurs
	 */
	abstract public void init(AbstractTab tab) throws ControllerInitException;

	/**
	 * saves all configs if something is to save
	 */
	abstract public void saveConfig();

	/**
	 * sets the command to enable or disable the main window
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setMainWindowCommand(ICommandEnableWindow cmd) {
		this.mainWindow = cmd;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}
}