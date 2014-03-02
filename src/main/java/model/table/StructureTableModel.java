package model.table;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.audio.FolderAudioFile;

public class StructureTableModel implements TableModel {

	/**
	 * the loaded audio files
	 */
	private List<FolderAudioFile> audioFiles;
	private String col1;
	private String col2;

	/**
	 * Constructor
	 * 
	 * @param audioFiles
	 *            the given audio files
	 */
	public StructureTableModel(List<FolderAudioFile> audioFiles) {
		this.audioFiles = audioFiles;
		col1 = "";
		col2 = "";
	}

	/**
	 * sets the first column name
	 * 
	 * @param name
	 *            the name
	 */
	public void setCol1Name(String name) {
		col1 = name;
	}

	/**
	 * sets the second columns name
	 * 
	 * @param name
	 *            the name
	 */
	public void setCol2Name(String name) {
		col2 = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0)
			return col1;
		else
			return col2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return this.audioFiles.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return this.audioFiles.get(rowIndex).getFilePath();
		else
			return this.audioFiles.get(rowIndex).getNewPath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event
	 * .TableModelListener)
	 */
	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
	 * TableModelListener)
	 */
	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
}