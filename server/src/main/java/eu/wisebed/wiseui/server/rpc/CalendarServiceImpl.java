/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.rpc;

import static eu.wisebed.wiseui.shared.common.Checks.ifNullArgument;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.api.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Soenke Nommensen
 */
@Singleton
public class CalendarServiceImpl extends RemoteServiceServlet implements CalendarService {

	private static final long serialVersionUID = -5507469730313618472L;

	private static final Logger LOG = LoggerFactory.getLogger(CalendarServiceImpl.class);

    private static final Calendar CALENDAR = Calendar.getInstance();

    @Override
    public Date addDays(final Date date, final int days) {
        ifNullArgument(date, "date not set!");
        LOG.info("addDays( date: " + date + ", days: " + days + " )");
        CALENDAR.setTime(date);
        CALENDAR.add(Calendar.DAY_OF_MONTH, days);
        return CALENDAR.getTime();
    }

    @Override
    public Date subtractDays(final Date date, final int days) {
        ifNullArgument(date, "date not set!");
        LOG.info("subtractDays( date: " + date + ", days: " + days + " )");
        CALENDAR.setTime(date);
        CALENDAR.add(Calendar.DAY_OF_MONTH, -days);
        return CALENDAR.getTime();
    }

    @Override
    public Date addMonth(final Date date) {
        ifNullArgument(date, "date not set!");
        LOG.info("addMonth( date: " + date + " )");
        CALENDAR.setTime(date);
        CALENDAR.add(Calendar.MONTH, 1);
        return CALENDAR.getTime();
    }

    @Override
    public Date subtractMonth(final Date date) {
        ifNullArgument(date, "date not set!");
        LOG.info("subtractMonth( date: " + date + " )");
        CALENDAR.setTime(date);
        CALENDAR.add(Calendar.MONTH, -1);
        return CALENDAR.getTime();
    }
}
