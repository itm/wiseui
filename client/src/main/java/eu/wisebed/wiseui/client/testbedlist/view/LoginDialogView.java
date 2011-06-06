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
package eu.wisebed.wiseui.client.testbedlist.view;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.view.client.HasData;
import com.google.inject.ImplementedBy;

import eu.wisebed.wiseui.client.testbedselection.common.UrnPrefixInfo;

@ImplementedBy(LoginDialogViewImpl.class)
public interface LoginDialogView {

    HasText getUsernameText();

    HasText getPasswordText();

    HasEnabled getUsernameEnabled();

    HasEnabled getPasswordEnabled();

    HasEnabled getSubmitEnabled();

    HasEnabled getCancelEnabled();

    HasData<UrnPrefixInfo> getUrnPrefixList();

    void setPresenter(Presenter presenter);

    void show(String title);

    void hide();

    public interface Presenter {

        void submit();

        void cancel();
    }
}
