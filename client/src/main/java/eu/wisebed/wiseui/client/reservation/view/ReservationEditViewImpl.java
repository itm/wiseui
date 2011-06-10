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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.TreeViewModel;
import com.google.inject.Singleton;
import eu.wisebed.wiseui.widgets.HasWidgetsDialogBox;

import java.util.Date;

/**
 * @author Soenke Nommensen
 */
@Singleton
public class ReservationEditViewImpl extends HasWidgetsDialogBox implements ReservationEditView {

    private static ReservationEditViewImplUiBinder uiBinder = GWT.create(ReservationEditViewImplUiBinder.class);

    interface ReservationEditViewImplUiBinder extends UiBinder<Widget, ReservationEditViewImpl> {
    }

    @UiField
    TextBox whoTextBox;
    @UiField
    DateBox startDateBox;
    @UiField
    DateBox endDateBox;
    // TODO This should be a list, which can show multiple urnPrefixes + reservations.
    @UiField
    TextBox reservationKeyBox;
    @UiField
    SimplePanel treeContainer;
    @UiField
    Button submitButton;
    @UiField
    Button cancelButton;
    @UiField
    Button deleteButton;
    @UiField
    Button createButton;

    private Presenter presenter;

    public ReservationEditViewImpl() {
        uiBinder.createAndBindUi(this);

        setModal(true);
        setGlassEnabled(true);
        setAnimationEnabled(true);
    }

    private CellTree createTree(final TreeViewModel model) {
        final CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
        final CellTree cellTree = new CellTree(model, null, res);
        cellTree.setAnimationEnabled(true);
        return cellTree;
    }

    @Override
    public void setTreeViewModel(final TreeViewModel model) {
        final CellTree tree = createTree(model);
        treeContainer.clear();
        treeContainer.add(tree);
    }

    @UiFactory
    protected ReservationEditViewImpl createDialog() {
        return this;
    }

    @UiFactory
    protected CellList<String> createCellList() {
        return new CellList<String>(new TextCell());
    }

    @UiHandler("submitButton")
    public void onSubmit(final ClickEvent event) {
        presenter.submit();
    }
    
    @UiHandler("createButton")
    public void onCreate(final ClickEvent event) {
        presenter.create();
    }

    @UiHandler("deleteButton")
    public void onDelete(final ClickEvent event) {
        presenter.delete();
    }

    @UiHandler("cancelButton")
    public void onCancel(final ClickEvent event) {
        presenter.cancel();
    }

    @Override
    public void show(final String title) {
        setText(title);
        center();
        show();
    }

    @Override
    public void setPresenter(final Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public HasText getWhoTextBox() {
        return whoTextBox;
    }

    @Override
    public HasValue<Date> getStartDateBox() {
        return startDateBox;
    }

    @Override
    public HasValue<Date> getEndDateBox() {
        return endDateBox;
    }

    @Override
    public HasText getReservationKeyBox() {
        return reservationKeyBox;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        startDateBox.setEnabled(!readOnly);
        endDateBox.setEnabled(!readOnly);
        submitButton.setVisible(!readOnly);
        deleteButton.setVisible(!readOnly);
    }

	@Override
	public void setCreate() {
		submitButton.setVisible(false);
        deleteButton.setVisible(false);
        createButton.setVisible(true);
	}

	@Override
	public void setUpdate() {
		submitButton.setVisible(true);
        deleteButton.setVisible(true);
		createButton.setVisible(false);
	}
}
