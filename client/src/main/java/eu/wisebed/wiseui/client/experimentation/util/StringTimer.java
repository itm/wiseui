/*
 * Copyright 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *              Research Academic Computer Technology Institute (RACTI)
 *
 * ITM and RACTI license this file under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
