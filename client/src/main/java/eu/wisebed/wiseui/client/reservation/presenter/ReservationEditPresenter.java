package eu.wisebed.wiseui.client.reservation.presenter;

import com.bradrydzewski.gwt.calendar.client.Appointment;
import com.google.common.base.Objects;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.testbedlist.event.EditTestbedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.RefreshTestbedListEvent;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView;
import eu.wisebed.wiseui.client.testbedlist.view.TestbedEditView.Presenter;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Soenke Nommensen
 */
public class ReservationEditPresenter implements Presenter, EditReservationEvent.Handler {

    private static final String DEFAULT_NEW_TITLE = "New Reservation";

    private final EventBusManager eventBus;

    private final ReservationEditView view;

    private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

    private TestbedConfiguration configuration;

    private String title = DEFAULT_NEW_TITLE;

    @Inject
    public ReservationEditPresenter(final EventBus eventBus,
                                    final ReservationEditView view,
                                    final TestbedConfigurationServiceAsync service) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;

        view.setUrnPrefixSelectionModel(selectionModel);
        view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
        bind();
    }

    private void bind() {
        eventBus.addHandler(EditReservationEvent.TYPE, this);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                view.getUrnPrefixRemoveHasEnabled().setEnabled(null != selectionModel.getSelectedObject());
            }
        });
    }

    @Override
    public void submit() {
        Appointment appointment;


    }

    @Override
    public void cancel() {
        MessageBox.warning(title, "Do you want to discard your changes?", new MessageBox.Callback() {

            @Override
            public void onButtonClicked(final Button button) {
                if (Button.OK.equals(button)) {
                    view.hide();
                }
            }
        });
    }

    @Override
    public void add() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void onEditReservation(final EditReservationEvent event) {
//        configuration = Objects.firstNonNull(event.getAppointment(), new TestbedConfiguration());
//        title = Objects.firstNonNull(configuration.getName(), DEFAULT_NEW_TITLE);
//        view.show(title);
    }
}
