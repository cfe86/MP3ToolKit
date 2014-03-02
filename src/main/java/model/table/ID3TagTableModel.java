package model.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.audio.interfaces.IAudioFile;
import model.util.FileUtil;

public class ID3TagTableModel implements TableModel {

	/**
	 * list of audio files
	 */
	private List<IAudioFile> audioFiles;

	/**
	 * the column names
	 */
	private String[] col;

	/**
	 * Constructor
	 * 
	 * @param audioFiles
	 *            the audio files
	 */
	public ID3TagTableModel(List<IAudioFile> audioFiles) {
		if (audioFiles == null)
			this.audioFiles = new ArrayList<>();
		else
			this.audioFiles = audioFiles;

		col = new String[17];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0)
			return Boolean.class;
		else
			return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 17;
	}

	/**
	 * sets the colum names
	 * 
	 * @param i
	 *            the index of the column
	 * @param name
	 *            the column name
	 */
	public void setColumnName(int i, String name) {
		col[i] = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int columnIndex) {
		return col[columnIndex];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return audioFiles.size();
	}

	// changed, filename, path, title, artist, album artist, album, year, track,
	// cd, genre, comment, composer, origArtist, copyright, url, encoded
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IAudioFile audioFile = this.audioFiles.get(rowIndex);

		switch (columnIndex) {
			case 0:
				return audioFile.hasChanged();
			case 1:
				return FileUtil.getFileNameWithoutExtension(audioFile.getFilePath());
			case 2:
				return FileUtil.getFilePath(audioFile.getFilePath());
			case 3:
				return audioFile.getTitle();
			case 4:
				return audioFile.getArtist();
			case 5:
				return audioFile.getAlbumArtist();
			case 6:
				return audioFile.getAlbum();
			case 7:
				return audioFile.getYear();
			case 8:
				return audioFile.getTrack();
			case 9:
				return audioFile.getCD();
			case 10:
				return audioFile.getGenreDescription();
			case 11:
				return audioFile.getComment();
			case 12:
				return audioFile.getComposer();
			case 13:
				return audioFile.getOriginalArtist();
			case 14:
				return audioFile.getCopyright();
			case 15:
				return audioFile.getURL();
			case 16:
				return audioFile.getEncoder();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
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
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
}