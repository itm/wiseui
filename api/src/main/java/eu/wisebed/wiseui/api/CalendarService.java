package eu.wisebed.wiseui.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Date;

/**
 * @author Soenke Nommensen
 *         <p/>
 *         Make use of the far more intuitive java.util.Calendar for date calculcation.
 */
@RemoteServiceRelativePath("calendar.rpc")
public interface CalendarService extends RemoteService {

    Date addDays(Date date, int days);

    Date subtractDays(Date date, int days);

    Date addMonth(Date date);

    Date subtractMonth(Date date);
}
