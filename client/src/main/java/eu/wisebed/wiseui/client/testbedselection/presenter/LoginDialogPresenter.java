package eu.wisebed.wiseui.client.testbedselection.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;

import eu.wisebed.wiseui.api.SNAAServiceAsync;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent;
import eu.wisebed.wiseui.client.testbedlist.event.TestbedSelectedEvent.ConfigurationSelectedHandler;
import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo;
import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo.State;
import eu.wisebed.wiseui.client.testbedselection.event.LoggedInEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ShowLoginDialogEvent;
import eu.wisebed.wiseui.client.testbedselection.event.ShowLoginDialogEvent.ShowLoginDialogHandler;
import eu.wisebed.wiseui.client.testbedselection.util.AuthenticationHelper;
import eu.wisebed.wiseui.client.testbedselection.util.AuthenticationHelper.Callback;
import eu.wisebed.wiseui.client.testbedselection.view.LoginDialogView;
import eu.wisebed.wiseui.client.testbedselection.view.LoginDialogView.Presenter;
import eu.wisebed.wiseui.client.util.AuthenticationManager;
import eu.wisebed.wiseui.client.util.EventBusManager;
import eu.wisebed.wiseui.shared.dto.SecretAuthenticationKey;
import eu.wisebed.wiseui.shared.dto.TestbedConfiguration;

/**
 * The presenter for the {@link LoginDialogView}.
 *
 * @author Malte Legenhausen
 */
public class LoginDialogPresenter implements Presenter, ConfigurationSelectedHandler, ShowLoginDialogHandler {

    private final EventBusManager eventBus;

    private final LoginDialogView view;

    private final SNAAServiceAsync authenticationService;

    private final AuthenticationManager authenticationManager;

    private final ListDataProvider<UrnPrefixInfo> dataProvider = new ListDataProvider<UrnPrefixInfo>();

    private TestbedConfiguration configuration;

    private AuthenticationHelper authenticationHelper;

    @Inject
    public LoginDialogPresenter(final EventBus eventBus,
                                final LoginDialogView view,
                                final SNAAServiceAsync authenticationService,
                                final AuthenticationManager authenticationManager) {
        this.eventBus = new EventBusManager(eventBus);
        this.view = view;
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;

        dataProvider.addDataDisplay(view.getUrnPrefixList());
    }

    @Override
    public void bind() {
        eventBus.addHandler(TestbedSelectedEvent.TYPE, this);
        eventBus.addHandler(ShowLoginDialogEvent.TYPE, this);
    }
    
    @Override
    public void unbind() {
    	eventBus.removeAll();
    }

    @Override
    public void submit() {
        view.getUsernameEnabled().setEnabled(false);
        view.getPasswordEnabled().setEnabled(false);
        view.getSubmitEnabled().setEnabled(false);

        final String endpointUrl = configuration.getSnaaEndpointUrl();
        final String username = view.getUsernameText().getText();
        final String password = view.getPasswordText().getText();

        authenticationHelper.authenticate(dataProvider.getList(), endpointUrl, username, password, new Callback() {

            private boolean hideAfterComplete = true;

            @Override
            public void onStateChanged(final UrnPrefixInfo info, final State state) {
                dataProvider.refresh();
                if (state.equals(State.FAILED) || state.equals(State.SKIPPED)) {
                    hideAfterComplete = false;
                }
            }

            @Override
            public void onSuccess(final SecretAuthenticationKey result) {
                authenticationManager.addSecretAuthenticationKey(result);
                eventBus.fireEventFromSource(new LoggedInEvent(result), this);
            }

            @Override
            public void onFinish() {
                view.getUsernameEnabled().setEnabled(true);
                view.getPasswordEnabled().setEnabled(true);
                view.getSubmitEnabled().setEnabled(true);
                if (hideAfterComplete) {
                    view.hide();
                }
            }
        });
    }

    @Override
    public void cancel() {
        //authenticationProvider.cancel();
        view.hide();
    }

    @Override
    public void onTestbedConfigurationSelected(final TestbedSelectedEvent event) {
        configuration = event.getConfiguration();
        dataProvider.getList().clear();
        for (String urnPrefix : configuration.getUrnPrefixList()) {
            dataProvider.getList().add(new UrnPrefixInfo(urnPrefix));
        }
        authenticationHelper = new AuthenticationHelper(authenticationService);
        dataProvider.refresh();
    }

    @Override
    public void onShowLoginDialog(final ShowLoginDialogEvent event) {
        view.show("Login to " + configuration.getName());
    }
}
