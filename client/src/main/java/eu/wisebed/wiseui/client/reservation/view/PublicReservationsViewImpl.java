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
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.shared.common.DateTimeUtil;
import eu.wisebed.wiseui.shared.dto.PublicReservationData;
import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

import java.util.Date;
import java.util.List;

/**
 * @author John I. Gakos, Soenke Nommensen
 *
 * TODO: Add time zone support!
 */
@Singleton
public class PublicReservationsViewImpl extends Composite implements PublicReservationsView {

    @UiTemplate("PublicReservationsViewImpl.ui.xml")
    interface PublicReservationsViewImplUiBinder extends UiBinder<Widget, PublicReservationsViewImpl> {
    }

    private static PublicReservationsViewImplUiBinder uiBinder = GWT.create(PublicReservationsViewImplUiBinder.class);

    private static final Date TODAY = new Date();

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
        dateBox.setValue(TODAY);
    }

    @Override
    public Date getFrom() {
        return calendarPanel.getDate();
    }

    @Override
    public Date getTo() {
        Date to = calendarPanel.getDate();

        if (calendarPanel.getDays() == DateTimeUtil.ONE_DAY) {
            to.setTime(DateTimeUtil.addDays(calendarPanel.getDate().getTime(), DateTimeUtil.ONE_DAY));
        } else if (calendarPanel.getDays() == DateTimeUtil.WEEK) {
            to.setTime(DateTimeUtil.addDays(calendarPanel.getDate().getTime(), DateTimeUtil.WEEK));
        } else {
            to.setTime(DateTimeUtil.addDays(calendarPanel.getDate().getTime(),
                    DateTimeUtil.getDaysOfMonth(calendarPanel.getDate())));
        }

        return to;
    }

    @Override
    public Calendar getCalendar() {
        return calendarPanel;
    }

    @Override
    public DateBox getDateBox() {
        return dateBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderPublicReservations(final List<PublicReservationData> publicReservations) {
        calendarPanel.suspendLayout();
        for (PublicReservationData reservation : publicReservations) {
            addReservation(reservation);
        }
        calendarPanel.resumeLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    public HasLoadingIndicator getLoadingIndicator() {
        return container;
    }

    // TODO Better display and UI binding. An extra ReservationDetails widget would be nice,
    // which can be used for viewing and editing reservation details.
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

    @Override
    public void setPresenter(Presenter presenter) {

    }

    @UiHandler("dayToggleButton")
    public void handleDayClicked(final ClickEvent event) {
        dayToggleButton.setDown(true);
        weekToggleButton.setDown(false);
        monthToggleButton.setDown(false);
        calendarPanel.setView(CalendarViews.DAY, DateTimeUtil.ONE_DAY);
    }

    @UiHandler("weekToggleButton")
    public void handleWeekClicked(final ClickEvent event) {
        dayToggleButton.setDown(false);
        weekToggleButton.setDown(true);
        monthToggleButton.setDown(false);
        calendarPanel.setView(CalendarViews.DAY, DateTimeUtil.WEEK);
    }

    @UiHandler("monthToggleButton")
    public void handleMonthClicked(final ClickEvent event) {
        dayToggleButton.setDown(false);
        weekToggleButton.setDown(false);
        monthToggleButton.setDown(true);
        calendarPanel.setView(CalendarViews.MONTH);
    }

    @UiHandler("backButton")
    public void handleBackClicked(final ClickEvent event) {
        final long currentTimeMillis = calendarPanel.getDate().getTime();
        if (calendarPanel.getDays() == DateTimeUtil.ONE_DAY) {
            calendarPanel.setDate(
                    new Date(DateTimeUtil.substractDays(currentTimeMillis, DateTimeUtil.ONE_DAY)),
                    DateTimeUtil.ONE_DAY);
        } else if (calendarPanel.getDays() == DateTimeUtil.WEEK) {
            calendarPanel.setDate(
                    new Date(DateTimeUtil.substractDays(currentTimeMillis, DateTimeUtil.WEEK)),
                    DateTimeUtil.WEEK);
        } else {
            calendarPanel.setDate(
                    new Date(DateTimeUtil.substractDays(currentTimeMillis,
                            DateTimeUtil.getDaysOfMonth(calendarPanel.getDate()))));
        }
        dateBox.setValue(calendarPanel.getDate());
    }

    @UiHandler("forwardButton")
    public void handleForwardClicked(final ClickEvent event) {
        final long currentTimeMillis = calendarPanel.getDate().getTime();
        if (calendarPanel.getDays() == DateTimeUtil.ONE_DAY) {
            calendarPanel.setDate(
                    new Date(DateTimeUtil.addDays(currentTimeMillis, DateTimeUtil.ONE_DAY)),
                    DateTimeUtil.ONE_DAY);
        } else if (calendarPanel.getDays() == DateTimeUtil.WEEK) {
            calendarPanel.setDate(
                    new Date(DateTimeUtil.addDays(currentTimeMillis, DateTimeUtil.WEEK)),
                    DateTimeUtil.WEEK);
        } else {
            calendarPanel.setDate(
                    new Date(DateTimeUtil.addDays(currentTimeMillis,
                            DateTimeUtil.getDaysOfMonth(calendarPanel.getDate()))));
        }
        dateBox.setValue(calendarPanel.getDate());
    }

    @UiHandler("todayButton")
    public void handleTodayClicked(final ClickEvent event) {
        calendarPanel.setDate(TODAY);
        dateBox.setValue(TODAY);
    }

    private void initCalendar() {
        CalendarSettings settings = new CalendarSettings();
        settings.setOffsetHourLabels(false);
        settings.setTimeBlockClickNumber(CalendarSettings.Click.Double);
        settings.setEnableDragDrop(true);

        calendarPanel.setSettings(settings);
        calendarPanel.setDate(TODAY);
        calendarPanel.setView(CalendarViews.DAY, DateTimeUtil.WEEK);
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