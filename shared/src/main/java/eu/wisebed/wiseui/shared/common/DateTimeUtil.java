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
package eu.wisebed.wiseui.shared.common;

import com.google.gwt.core.client.GWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Soenke Nommensen
 *         <p/>
 *         This class provides some utility methods for date calculations.
 *         It will become obsolete as soon as java.util.Calendar emulation is available in GWT.
 */
public class DateTimeUtil {

    public static final int ONE_DAY = 1;
    public static final int THREE_DAYS = 3;
    public static final int WORK_WEEK = 5;
    public static final int WEEK = 7;

    public enum Months {
        JANUARY(1),
        FEBRUARY(2),
        MARCH(3),
        APRIL(4),
        MAY(5),
        JUNE(6),
        JULY(7),
        AUGUST(8),
        SEPTEMBER(9),
        OCTOBER(10),
        NOVEMBER(11),
        DECEMBER(12);

        private int value;

        Months(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Months fromInt(int i) {
            for (Months month : Months.values()) {
                if (month.getValue() == i) {
                    return month;
                }
            }
            return Months.JANUARY;
        }
    }

    private static Map<Months, Integer> daysOfMonth = new HashMap<Months, Integer>(12);

    static {
        daysOfMonth.put(Months.JANUARY, 31);
        daysOfMonth.put(Months.FEBRUARY, 28);
        daysOfMonth.put(Months.MARCH, 31);
        daysOfMonth.put(Months.APRIL, 30);
        daysOfMonth.put(Months.MAY, 31);
        daysOfMonth.put(Months.JUNE, 30);
        daysOfMonth.put(Months.JULY, 31);
        daysOfMonth.put(Months.AUGUST, 31);
        daysOfMonth.put(Months.SEPTEMBER, 30);
        daysOfMonth.put(Months.OCTOBER, 31);
        daysOfMonth.put(Months.NOVEMBER, 30);
        daysOfMonth.put(Months.DECEMBER, 31);
    }

    public static int getDaysOfMonth(final long timeMillis) {
        final Date date = new Date(timeMillis);
        return getDaysOfMonth(date);
    }

    public static int getDaysOfMonth(final Date date) {
        Checks.ifNullArgument(date, "date is null");
        // Deprecated getMonth()-method is used here, because java.util.Calendar is not available.
        final int month = date.getMonth() + 1;
        if (daysOfMonth.containsKey(Months.fromInt(month)))
            return daysOfMonth.get(Months.fromInt(month));
        return 0;
    }

    public static long addDays(final long start, final int days) {
        return start + toTimeMillis(days);
    }

    public static long substractDays(final long start, final int days) {
        return start - toTimeMillis(days);
    }

    private static long toTimeMillis(final int days) {
        return ((long) days) * 24 * 60 * 60 * 1000;
    }
}
