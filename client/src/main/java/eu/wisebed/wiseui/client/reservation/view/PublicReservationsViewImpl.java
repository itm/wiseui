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
package eu.wisebed.wiseui.client.reservation.view;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.bradrydzewski.gwt.calendar.client.AppointmentStyle;
import com.bradrydzewski.gwt.calendar.client.Calendar;
import com.bradrydzewski.gwt.calendar.client.CalendarSettings;
import com.bradrydzewski.gwt.calendar.client.CalendarViews;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.dto.ConfidentialReservationData;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

import java.util.Date;
import java.util.List;

/**
 * @author John I. Gakos
 * @author Soenke Nommensen
 */
@Singleton
public class PublicReservationsViewImpl extends Composite implements PublicReservationsView {

    @UiTemplate("PublicReservationsViewImpl.ui.xml")
    interface PublicReservationsViewImplUiBinder extends UiBinder<Widget, PublicReservationsViewImpl> {
    }

    private static PublicReservationsViewImplUiBinder uiBinder = GWT.create(PublicReservationsViewImplUiBinder.class);

    private Presenter presenter;

    @UiField
    CaptionPanel container;
    @UiField
    SimplePanel calendarContainer;
    @UiField
    Calendar calendarPanel;
    @UiField
    ToggleButton dayToggleButton;
    @UiField
    ToggleButton weekToggleButton;
    @UiField
    ToggleButton monthToggleButton;
    @UiField
    PushButton backButton;
    @UiField
    PushButton forwardButton;
    @UiField
    DateBox dateBox;
    @UiField
    Button todayButton;

    public PublicReservationsViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        initCalendar();
        dateBox.setValue(new Date());
    }

    @Override
    public Calendar getCalendar() {
        return calendarPanel;
    }

    @Override
    public DateBox getDatePicker() {
        return dateBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderPublicReservations(final List<PublicReservationData> publicReservations) {
        calendarPanel.suspendLayout();
        for (PublicReservationData reservation : publicReservations) {
            final Appointment appointment = addReservation(reservation, false);
            presenter.registerPublicReservation(appointment.getId(), reservation);
        }
        calendarPanel.resumeLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderConfidentialReservations(final List<ConfidentialReservationData> confidentialReservations) {
        calendarPanel.suspendLayout();
        for (ConfidentialReservationData reservation : confidentialReservations) {
            final Appointment appointment = addReservation(reservation, true);
            presenter.registerConfidentialReservation(appointment.getId(), reservation);
        }
        calendarPanel.resumeLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Appointment addReservation(final PublicReservationData reservation, boolean confidential) {
        final Appointment appointment = new Appointment();
        // User data and hash code is used as appointment ID
        appointment.setId(reservation.getUserData() + reservation.hashCode());
        appointment.setStart(reservation.getFrom());
        appointment.setEnd(reservation.getTo());
        appointment.setLocation(reservation.getNodeURNs().get(0));
        appointment.setTitle(reservation.getUserData());
        appointment.setCreatedBy(reservation.getUserData());
        appointment.setDescription(concatList(reservation.getNodeURNs()));
        if (confidential) {
            appointment.setStyle(AppointmentStyle.RED);
        } else {
            appointment.setStyle(AppointmentStyle.BLUE_GREY);
        }
        calendarPanel.addAppointment(appointment);
        return appointment;
    }

    @Override
    public void removeAllAppointments() {
        calendarPanel.clearAppointments();
    }

    @Override
    public HasLoadingIndicator getLoadingIndicator() {
        return container;
    }

    @Override
    public void setPresenter(final Presenter presenter) {
        GWT.log("setPresenter( " + presenter.toString() + " )");
        this.presenter = presenter;
    }

    @UiHandler("dayToggleButton")
    public void handleDayClicked(final ClickEvent event) {
        dayToggleButton.setDown(true);
        weekToggleButton.setDown(false);
        monthToggleButton.setDown(false);
        calendarPanel.setView(CalendarViews.DAY, Presenter.ONE_DAY);
        presenter.loadReservations(calendarPanel.getDate());
    }

    @UiHandler("weekToggleButton")
    public void handleWeekClicked(final ClickEvent event) {
        dayToggleButton.setDown(false);
        weekToggleButton.setDown(true);
        monthToggleButton.setDown(false);
        calendarPanel.setView(CalendarViews.DAY, Presenter.WEEK);
        presenter.loadReservations(calendarPanel.getDate());
    }

    @UiHandler("monthToggleButton")
    public void handleMonthClicked(final ClickEvent event) {
        dayToggleButton.setDown(false);
        weekToggleButton.setDown(false);
        monthToggleButton.setDown(true);
        calendarPanel.setView(CalendarViews.MONTH);
        presenter.loadReservations(calendarPanel.getDate());
    }

    @UiHandler("backButton")
    public void handleBackClicked(final ClickEvent event) {
        presenter.handleBackClicked();
    }

    @UiHandler("forwardButton")
    public void handleForwardClicked(final ClickEvent event) {
        presenter.handleForwardClicked();
    }

    @UiHandler("todayButton")
    public void handleTodayClicked(final ClickEvent event) {
        presenter.handleTodayClicked();
    }

    private void initCalendar() {
        CalendarSettings settings = new CalendarSettings();
        settings.setOffsetHourLabels(false);
        settings.setTimeBlockClickNumber(CalendarSettings.Click.Double);
        settings.setEnableDragDrop(true);

        calendarPanel.setSettings(settings);
        calendarPanel.setDate(new Date());
        calendarPanel.setView(CalendarViews.DAY, Presenter.WEEK);
        weekToggleButton.setDown(true);
    }

    private String concatList(final List<String> list) {
        String result = "";
        for (String s : list) {
            result += s + "\n";
        }
        return result;
    }
}
