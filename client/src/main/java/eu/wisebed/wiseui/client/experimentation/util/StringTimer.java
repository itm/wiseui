package eu.wisebed.wiseui.client.experimentation.util;

public abstract class StringTimer {

	/**
	 * Returns the elapsed time(from now to the experiment initiation) in a string format.
	 * 
	 * @param diffInMillis difference between two <code>Date</code> objects in milliseconds
	 * @return a string containing the remain time in "%D days %H hours %M minutes %S seconds"
	 */
	public static final String elapsedTimeToString(final long diffInMillis) {
		
		long diff = diffInMillis;
		
		final long secondInMillis = 1000;
		final long minuteInMillis = secondInMillis * 60;
		final long hourInMillis = minuteInMillis * 60;
		final long dayInMillis = hourInMillis * 24;
		final long yearInMillis = dayInMillis * 365;

		diff = diff % yearInMillis;
		long elapsedDays = diff / dayInMillis;
		diff = diff % dayInMillis;
		long elapsedHours = diff / hourInMillis;
		diff = diff % hourInMillis;
		long elapsedMinutes = diff / minuteInMillis;
		diff = diff % minuteInMillis;
		long elapsedSeconds = diff / secondInMillis;
	
		
		return	elapsedDays  + " days "  +
			elapsedHours + " hours " +
			elapsedMinutes + " minutes " +
			elapsedSeconds + " seconds ";
	}

}
