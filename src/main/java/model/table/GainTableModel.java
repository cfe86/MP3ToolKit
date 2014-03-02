package model.table;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.audio.GainAudioFile;
import model.util.FileUtil;

public class GainTableModel implements TableModel {

	/**
	 * the list of audio files
	 */
	private List<GainAudioFile> audioFiles;

	/*
	 * column names
	 */
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;

	/**
	 * Constructor
	 * 
	 * @param audioFiles
	 *            the audiofiles which should be shown
	 */
	public GainTableModel(List<GainAudioFile> audioFiles) {
		this.audioFiles = audioFiles;
		this.col1 = "";
		this.col2 = "";
		this.col3 = "";
		this.col4 = "";
		this.col5 = "";
		this.col6 = "";
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

	/**
	 * sets the column names
	 * 
	 * @param i
	 *            column index (starts at 1)
	 * @param name
	 *            the column name
	 */
	public void setColName(int i, String name) {
		switch (i) {
			case 1:
				col1 = name;
				return;
			case 2:
				col2 = name;
				return;
			case 3:
				col3 = name;
				return;
			case 4:
				col4 = name;
				return;
			case 5:
				col5 = name;
				return;
			case 6:
				col6 = name;
				return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 6;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return col1;
			case 1:
				return col2;
			case 2:
				return col3;
			case 3:
				return col4;
			case 4:
				return col5;
			case 5:
				return col6;
		}

		return "";
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
		switch (columnIndex) {
			case 0:
				return FileUtil.getFileNameWithoutExtension(this.audioFiles.get(rowIndex).getPath());
			case 1:
				return this.audioFiles.get(rowIndex).getPath();
			case 2:
				return getValue(this.audioFiles.get(rowIndex).getTrackvolume(), this.audioFiles.get(rowIndex).isValid());
			case 3:
				return getValue(this.audioFiles.get(rowIndex).getTrackGain(), this.audioFiles.get(rowIndex).isValid());
			case 4:
				return getValue(this.audioFiles.get(rowIndex).getAlbumVolume(), this.audioFiles.get(rowIndex).isValid());
			case 5:
				return getValue(this.audioFiles.get(rowIndex).getAlbumGain(), this.audioFiles.get(rowIndex).isValid());
		}

		return "";
	}

	/**
	 * returns the value if valid is true, else an empty string
	 * 
	 * @param value
	 *            the value
	 * @param valid
	 *            valid bit
	 * 
	 * @return the value or an empty string
	 */
	private Object getValue(double value, boolean valid) {
		if (valid)
			return value;
		else
			return "";
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
