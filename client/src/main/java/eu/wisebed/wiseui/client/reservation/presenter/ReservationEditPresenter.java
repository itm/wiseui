package eu.wisebed.wiseui.client.reservation.presenter;

import com.google.common.base.Objects;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import eu.wisebed.wiseui.api.TestbedConfigurationServiceAsync;
import eu.wisebed.wiseui.client.WiseUiGinjector;
import eu.wisebed.wiseui.client.reservation.event.EditReservationEvent;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView;
import eu.wisebed.wiseui.client.reservation.view.ReservationEditView.Presenter;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox;
import eu.wisebed.wiseui.widgets.messagebox.MessageBox.Button;

/**
 * @author Soenke Nommensen
 */
public class ReservationEditPresenter implements Presenter, EditReservationEvent.Handler, ConfigurationSelectedHandler{

    private static final String DEFAULT_NEW_TITLE = "New Reservation";

    private WiseUiGinjector injector;
    
    private final EventBusManager eventBus;

    private final ReservationEditView view;

    private final ListDataProvider<String> urnPrefixProvider = new ListDataProvider<String>();
    
    private final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();

    private TestbedConfiguration configuration;

    private String title = DEFAULT_NEW_TITLE;

    @Inject
    public ReservationEditPresenter(final WiseUiGinjector injector,
    								final EventBus eventBus,
                                    final ReservationEditView view,
                                    final TestbedConfigurationServiceAsync service) {
    	this.injector = injector;
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        
        urnPrefixProvider.addDataDisplay(view.getUrnPrefixHasData());
        view.setUrnPrefixSelectionModel(selectionModel);
        view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
        bind();
    }

    private void bind() {
        eventBus.addHandler(EditReservationEvent.TYPE, this);
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(final SelectionChangeEvent event) {
                view.getUrnPrefixRemoveHasEnabled().setEnabled(null != selectionModel.getSelectedObject());
            }
        });
    }

    @Override
    public void submit() {
        //Appointment appointment;


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
    	final String urn = view.getUrnPrefixHasText().getText();
    	urnPrefixProvider.getList().add(urn);
    	urnPrefixProvider.refresh();
    }

    @Override
    public void remove() {
    	final String urn = selectionModel.getSelectedObject();
    	urnPrefixProvider.getList().remove(urn);
    	urnPrefixProvider.refresh();
    	view.getUrnPrefixRemoveHasEnabled().setEnabled(false);
    }

    @Override
    public void onTestbedSelected(final TestbedSelectedEvent event){
    	this.configuration = event.getConfiguration();
    }

    @Override
    public void onEditReservation(final EditReservationEvent event) {
        final String title = Objects.firstNonNull(configuration.getName(), DEFAULT_NEW_TITLE);
    	view.show(title);
    	view.getStartDateBox().setValue(event.getAppointment().getStart());
    	view.getUrnPrefixHasText().setText(configuration.getUrnPrefixList().get(0));
    	view.getWhoTextBox().setText(injector.getAuthenticationManager().getSecretAuthenticationKeys().get(0).getUsername());
    }
}
