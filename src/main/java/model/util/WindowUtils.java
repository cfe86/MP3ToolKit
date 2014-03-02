package model.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class WindowUtils {

	/**
	 * calculates the coordinates of the left top corner of the given window to
	 * center it depending on the current solution
	 * 
	 * @param window
	 *            given window
	 * 
	 * @return centered coordinates
	 */
	public static Point getCenteredWindowCoordinates(JFrame window) {
		Point result = new Point();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();

		result.setLocation(dimension.width / 2 - window.getWidth() / 2, dimension.height / 2 - window.getHeight() / 2);

		return result;
	}

	/**
	 * gets the current screen resolution
	 * 
	 * @return the screen resolution
	 */
	public static Dimension getScreenResolution() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		return dimension;
	}

	/**
	 * updates a JTree to represent the folder structure
	 * 
	 * @param rootPath
	 *            given root of the JTable
	 * @param onlyFolders
	 *            true if only folders should be displayed
	 * @param withPath
	 *            true if the path should be printed, else only the name will be
	 *            printed
	 */
	public static void updateBrowserTree(JTree tree, String rootPath, boolean onlyFolders, boolean withPath) {
		if (!new File(rootPath).exists() || rootPath.trim().length() == 0)
			tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode()));

		if (onlyFolders)
			tree.setCellRenderer(new DirectoryRenderer());
		TreeModel model = new DefaultTreeModel(createBrowserTreeNode(rootPath, onlyFolders, withPath));
		tree.setModel(model);

	}

	/**
	 * creates a browser tree node
	 * 
	 * @param rootPath
	 *            the root of the folder
	 * @param onlyFolders
	 *            true if only folders should be shown
	 * @param withPath
	 *            true if the paths should be given in absolute form
	 * 
	 * @return the tree node
	 */
	private static DefaultMutableTreeNode createBrowserTreeNode(String rootPath, boolean onlyFolders, boolean withPath) {
		DefaultMutableTreeNode root;

		root = getSubfolders(rootPath, onlyFolders, withPath);

		return root;
	}

	/**
	 * gets the tree node of a subfolder
	 * 
	 * @param path
	 *            the root of the folder
	 * @param onlyFolders
	 *            true if only folders should be shown
	 * @param withPath
	 *            true if the paths should be given in absolute form
	 * 
	 * @return the tree node
	 */
	private static DefaultMutableTreeNode getSubfolders(String path, boolean onlyFolders, boolean withPath) {

		if (new File(path).isFile()) {
			if (withPath)
				return new DefaultMutableTreeNode(path);
			else
				return new DefaultMutableTreeNode(FileUtil.getFileName(path));
		}

		DefaultMutableTreeNode result;
		if (withPath)
			result = new DefaultMutableTreeNode(path);
		else
			result = new DefaultMutableTreeNode(FileUtil.getFileNameWithoutExtension(path));

		List<String> files = FileUtil.getFilesFromFolder(path, true);

		for (String file : files) {
			if (onlyFolders) {
				if (new File(file).isDirectory()) {
					result.add(getSubfolders(file, onlyFolders, withPath));
				}
			} else
				result.add(getSubfolders(file, onlyFolders, withPath));
		}

		return result;
	}

	/**
	 * Cell Renderer to render even leafs of the JTree as Directories
	 */
	private static class DirectoryRenderer extends DefaultTreeCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2434390225973150455L;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			setLeafIcon(getDefaultClosedIcon());

			return this;
		}
	}
}