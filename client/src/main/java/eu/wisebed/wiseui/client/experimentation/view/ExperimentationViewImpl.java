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
package eu.wisebed.wiseui.client.experimentation.view;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Singleton;

import eu.wisebed.wiseui.widgets.CaptionPanel;
import eu.wisebed.wiseui.widgets.loading.HasLoadingIndicator;

@Singleton
public class ExperimentationViewImpl extends Composite implements
        ExperimentationView {

    private static ExperimentationViewImplUiBinder uiBinder = GWT
            .create(ExperimentationViewImplUiBinder.class);

    interface ExperimentationViewImplUiBinder extends
            UiBinder<Widget, ExperimentationViewImpl> {
    }
    
    private Presenter presenter;
        
    @UiField
    CaptionPanel container;
    
    @UiField
    VerticalPanel experimentContainer;
 
    @UiField
    PushButton refreshExperimentsButton;

    @UiField
    DateBox fromDateBox;
    
    @UiField
    DateBox toDateBox;
    
    @UiField
    Button todayButton;
    
    @UiField
    ToggleButton inWeek;
    
    @UiField
    ToggleButton inMonth;
    
    @UiField
    ToggleButton inDay;
    
    
    public ExperimentationViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        fromDateBox.setValue(new Date());
        setFromToday();
        handleWeekClick(null);
    }

    public void setPresenter(final Presenter presenter) {
    	this.presenter = presenter;
    }

	@Override
	public Presenter getPresenter() {
		return presenter;
	}

	@Override
	public HasLoadingIndicator getLoadingIndicator() {
		return container;
	}

	@Override
	public void addExperimentPanel(final ExperimentView experiment){
		experimentContainer.add(experiment);
	}
	
	@Override
	public void clearExperimentationPanel() {
		experimentContainer.clear();		
	}
	
	@Override
	public Date getFromDate() {
		return fromDateBox.getValue();
	}

	@Override
	public Date getToDate() {
		return toDateBox.getValue();
	}
	
    @UiHandler("refreshExperimentsButton")
    public void handleRefreshClick(final ClickEvent event) {
        presenter.refreshUserExperiments();
    }
    
    @UiHandler("todayButton")
    public void handleTodayClick(final ClickEvent event) {
    	fromDateBox.setValue(new Date());
    	
    }
    
    @SuppressWarnings("deprecation")
	@UiHandler("inWeek")
    public void handleWeekClick(final ClickEvent event) {
    	
    	Date today = new Date();
    	int date = today.getDate();
    	date = date + 6;
    	today.setDate(date);  
    	
    	toDateBox.setValue(today);
    	setWeekDayMonth(true,false,false);
    }
    
    @SuppressWarnings("deprecation")
	@UiHandler("inMonth")
    public void handleMonthClick(final ClickEvent event) {
    	
    	Date today = new Date();
    	int month = today.getMonth();
    	month++;
    	today.setMonth((month >= 12)?11:month);
    	
    	toDateBox.setValue(today);
    	setWeekDayMonth(false,true,false);
    }
    
    @SuppressWarnings("deprecation")
	@UiHandler("inDay")
    public void handleDayClick(final ClickEvent event) {
    	
    	Date today = new Date();
    	int date = today.getDate();
    	date++;
    	today.setDate(date);
    	
    	toDateBox.setValue(today);
    	setWeekDayMonth(false,false,true);
    }
    
    private void setWeekDayMonth(final boolean week,final boolean month, final boolean day ){
    	inWeek.setDown(week);
    	inMonth.setDown(month);
    	inDay.setDown(day);
    }
    
    private void setFromToday(){
    	fromDateBox.setValue(new Date());
    }
}
