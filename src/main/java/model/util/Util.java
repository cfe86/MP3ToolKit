package model.util;

import javax.swing.tree.DefaultMutableTreeNode;

public class Util {

	/**
	 * sets the application to sleep for a given time
	 * 
	 * @param time
	 *            given time to sleep in ms
	 */
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * strips the (...) at the end of a string
	 * 
	 * @param str
	 *            given string
	 * 
	 * @return the stripped string
	 */
	public static String stripExtraData(String str) {
		str = str.trim();
		str = str.replaceAll("\\(.*?\\)", "");

		return str.trim();
	}

	/**
	 * gets the Path from the current node to the root in a given JTree. The
	 * JTree elements toString() method will be used to identify the names.
	 * 
	 * @param node
	 *            given selected node
	 * 
	 * @return the path to the root e.g. root/node/given_node
	 */
	public static String getPathFromJTree(Object node) {
		DefaultMutableTreeNode current = (DefaultMutableTreeNode) node;
		String result = current.getUserObject().toString();

		// current = (DefaultMutableTreeNode) current.getParent();
		while (!current.isRoot()) {
			current = (DefaultMutableTreeNode) current.getParent();
			result = ((String) current.getUserObject()) + "/" + result;
		}

		return result;
	}
}