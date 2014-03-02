package model.structure;

public class UpdateData {

	/**
	 * the date of the last update
	 */
	private String updateDate;

	/**
	 * the link where to find the update
	 */
	private String updateLink;

	/**
	 * the size of the update in KB
	 */
	private String sizeKB;

	/**
	 * the link where to find the update for the *.exe
	 */
	private String updateLinkExe;

	/**
	 * the size of the update in KB for *.exe
	 */
	private String sizeKBExe;

	/**
	 * should a message should be shown
	 */
	private boolean showMsg;

	/**
	 * the message
	 */
	private String msg;

	/**
	 * Constructor
	 * 
	 * @param updateDate
	 *            the update date
	 * @param updateLink
	 *            the update link
	 * @param sizeKB
	 *            the size in KB
	 * @param updateLinkExe
	 *            the update link to *.exe
	 * @param sizeKBExe
	 *            the size in KB for *.exe
	 * @param showMsg
	 *            should a message be shown
	 * @param msg
	 *            the message
	 */
	public UpdateData(String updateDate, String updateLink, String sizeKB, String updateLinkExe, String sizeKBExe, boolean showMsg, String msg) {
		this.updateDate = updateDate;
		this.updateLink = updateLink;
		this.sizeKB = sizeKB;
		this.updateLinkExe = updateLinkExe;
		this.sizeKBExe = sizeKBExe;
		this.showMsg = showMsg;
		this.msg = msg;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public String getUpdateLink() {
		return updateLink;
	}

	public String getSizeKB() {
		return sizeKB;
	}

	public String getUpdateLinkExe() {
		return updateLinkExe;
	}

	public String getSizeKBExe() {
		return sizeKBExe;
	}

	public boolean isShowMsg() {
		return showMsg;
	}

	public String getMsg() {
		return msg;
	}
}