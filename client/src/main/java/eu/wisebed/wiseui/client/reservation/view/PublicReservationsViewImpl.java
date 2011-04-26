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
package eu.wisebed.wiseui.client.reservation.view;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;
import eu.wisebed.wiseui.widgets.loading.LoadingIndicator;

import java.util.Date;
import java.util.List;

/**
 * @author John I. Gakos, Soenke Nommensen
 */
@Singleton
public class PublicReservationsViewImpl extends Composite implements PublicReservationsView, HasLoadingIndicator {

    private static final int WEEK_DAYS = 7;

    private LoadingIndicator loadingIndicator;

    @UiTemplate("PublicReservationsViewImpl.ui.xml")
    interface PublicReservationsViewImplUiBinder extends
            UiBinder<Widget, PublicReservationsViewImpl> {
    }

    private static PublicReservationsViewImplUiBinder uiBinder =
            GWT.create(PublicReservationsViewImplUiBinder.class);

    public void setPresenter(Presenter presenter) {
    }

    @UiField
    SimplePanel reservationContainer;
    @UiField
    Calendar calendarPanel;

    public PublicReservationsViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        initCalendar();
    }

    public Date getFrom() {
        return calendarPanel.getDate();
    }

    public Date getTo() {
        Date to = new Date();
        to.setTime(System.currentTimeMillis() + ((long) WEEK_DAYS) * 24 * 60 * 60 * 1000);
        return to;
    }

    @Override
    public Calendar getCalendar() {
        return calendarPanel;
    }

    /**
     * {@inheritDoc}
     */
    public void renderPublicReservations(final List<PublicReservationData> publicReservations) {
        calendarPanel.clearAppointments();
        calendarPanel.suspendLayout();
        for (PublicReservationData reservation : publicReservations) {
            addReservation(reservation);
        }
        calendarPanel.resumeLayout();
    }

    /**
     * {@inheritDoc}
     */
    public void addReservation(final PublicReservationData reservation) {
        // TODO: Work on a more descriptive representation of reservations
        Appointment appointment = new Appointment();
        appointment.setStart(reservation.getFrom());
        appointment.setEnd(reservation.getTo());
        appointment.setLocation(reservation.getNodeURNs().get(0));
        appointment.setTitle(reservation.getUserData());
        appointment.setCreatedBy(reservation.getUserData());
        appointment.setDescription(concatList(reservation.getNodeURNs()));
        appointment.setStyle(AppointmentStyle.RED);
        calendarPanel.addAppointment(appointment);
    }

    @Override
    public void removeAllReservations() {
        calendarPanel.clearAppointments();
    }

    @Override
    public void showLoading(final String text) {
        loadingIndicator = LoadingIndicator.on(calendarPanel).show(text);
    }

    @Override
    public void hideLoading() {
        if (loadingIndicator != null) {
            loadingIndicator.hide();
        }
    }

    @Override
    public HasLoadingIndicator getLoadingIndicator() {
        return this;
    }

    // TODO Better display and cleaner code
    @Override
    public void showReservationDetails(final Appointment appointment) {
        DecoratedPopupPanel popUp = new DecoratedPopupPanel(true);
        popUp.setAnimationEnabled(true);
        popUp.setAutoHideEnabled(true);
        popUp.setModal(false);

        VerticalPanel panel = new VerticalPanel();
        panel.setPixelSize(275, 400);

        Label reservedBy = new Label("Reserved by: " + appointment.getCreatedBy());
        Label startLabel = new Label("Start: " + appointment.getStart().toString());
        Label endLabel = new Label("End: " + appointment.getEnd().toString());

        TextArea textArea = new TextArea();
        textArea.setEnabled(false);
        textArea.setHeight("375px");
        textArea.setWidth("100%");
        textArea.setText(appointment.getDescription());

        panel.add(reservedBy);
        panel.add(startLabel);
        panel.add(endLabel);
        panel.add(textArea);

        popUp.add(panel);
        popUp.center();
        popUp.show();
    }

    private void initCalendar() {
        calendarPanel.setDate(new Date());
        calendarPanel.setHeight("100%");
        calendarPanel.setView(CalendarViews.DAY, 9);
    }

    private String concatList(final List<String> list) {
        String result = "";
        for (String s : list) {
            result += s + "\n";
        }
        return result;
    }
}