package model.time;

import java.text.DecimalFormat;

public abstract class Timer {

	private boolean running;
	private int secondsCounter;
	private int minutesCounter;
	private int hoursCounter;
	private int dayCounter;
	private int monthCounter;
	private int yearsCounter;
	private DecimalFormat df = new DecimalFormat("00");

	/**
	 * Constructor
	 * 
	 */
	public Timer() {
		running = false;
		secondsCounter = 0;
		minutesCounter = 0;
		hoursCounter = 0;
		dayCounter = 0;
		monthCounter = 0;
		yearsCounter = 0;
	}

	/**
	 * starts the timer
	 */
	public void start() {
		this.running = true;
	}

	/**
	 * stops the timer
	 */
	public void stop() {
		this.running = false;
	}

	/**
	 * true if the timer is running, else false
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * adds one second to the time
	 */
	public void addASecond() {
		secondsCounter++;
		if (secondsCounter > 59) {
			secondsCounter = 0;
			minutesCounter++;
		}

		if (minutesCounter > 59) {
			minutesCounter = 0;
			hoursCounter++;
		}

		if (hoursCounter > 24) {
			hoursCounter = 0;
			dayCounter++;
		}

		if (dayCounter > 30) {
			dayCounter = 0;
			monthCounter++;
		}

		if (monthCounter > 12) {
			monthCounter = 0;
			yearsCounter++;
		}
	}

	/**
	 * substracts a second
	 */
	public void subtractASecond() {
		secondsCounter--;
		if (secondsCounter < 0) {
			secondsCounter = 59;
			minutesCounter--;
		}

		if (minutesCounter < 00) {
			minutesCounter = 59;
			hoursCounter--;
		}

		if (hoursCounter < 0) {
			hoursCounter = 23;
			dayCounter--;
		}

		if (dayCounter < 0) {
			dayCounter = 29;
			monthCounter--;
		}

		if (monthCounter < 0) {
			monthCounter = 12;
			yearsCounter--;
		}
	}

	public String getSeconds() {
		return df.format(secondsCounter);
	}

	public String getMinutes() {
		return df.format(minutesCounter);
	}

	public String getHours() {
		return df.format(hoursCounter);
	}

	public String getDays() {
		return df.format(dayCounter);
	}

	public String getMonths() {
		return df.format(monthCounter);
	}

	public String getYears() {
		return df.format(yearsCounter);
	}

	public int getSecondsCount() {
		// min = 60, hour = 3600, day = 86400, month = 2592000, year = 946080000
		return secondsCounter + (minutesCounter * 60) + (dayCounter * 86400) + (monthCounter * 2592000) + (yearsCounter * 946080000);
	}

	public void setTime(int seconds, int minutes, int hours, int days, int month, int years) {
		this.secondsCounter = seconds;
		this.minutesCounter = minutes;
		this.hoursCounter = hours;
		this.dayCounter = days;
		this.monthCounter = month;
		this.yearsCounter = years;
	}

	public void setTime(int seconds) {
		this.minutesCounter = seconds / 60;
		this.secondsCounter = seconds - 60 * this.minutesCounter;

		this.hoursCounter = this.minutesCounter / 60;
		this.minutesCounter = this.minutesCounter - 60 * this.hoursCounter;

		this.dayCounter = this.hoursCounter / 24;
		this.hoursCounter = this.hoursCounter - 24 * this.dayCounter;

		this.monthCounter = this.dayCounter / 30;
		this.dayCounter = this.dayCounter - 30 * this.monthCounter;

		this.yearsCounter = this.monthCounter / 12;
		this.monthCounter = this.monthCounter - 12 * this.yearsCounter;
	}

	/**
	 * checks if the clock is 0
	 * 
	 * @return true if it is zero, else false
	 */
	public boolean isDone() {
		return secondsCounter == 0 && minutesCounter == 0 && hoursCounter == 0 && dayCounter == 0 && monthCounter == 0 && yearsCounter == 0;
	}

	/**
	 * gets the formatted time
	 * 
	 * @return the formatted time
	 */
	abstract public String getformattedString();
}