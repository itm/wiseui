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
package eu.wisebed.wiseui.client.experimentation.gin;

import com.google.gwt.inject.client.Ginjector;

import eu.wisebed.wiseui.client.experimentation.ExperimentationActivity;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentOutputPresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentPresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentWiseMLOutputPresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ExperimentationPresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.FlashExperimentImagePresenter;
import eu.wisebed.wiseui.client.experimentation.presenter.ImageUploadWidgetPresenter;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentOutputView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentWiseMLOutputView;
import eu.wisebed.wiseui.client.experimentation.view.ExperimentationView;
import eu.wisebed.wiseui.client.experimentation.view.FlashExperimentImageView;
import eu.wisebed.wiseui.client.experimentation.view.ImageUploadWidget;

public interface ExperimentationGinjector extends Ginjector {

    ExperimentationView getExperimentationView();

    ExperimentationActivity getExperimentationActivity();
    
    ExperimentationPresenter getExperimentationPresenter();
    
    ExperimentPresenter getExperimentPresenter();
    
    ExperimentView getExperimentView();
    
    FlashExperimentImagePresenter getFlashExperimentPresenter();
        
    FlashExperimentImageView getFlashExperimentImageView();
    
    ImageUploadWidgetPresenter getImageUploadWidgetPresenter();
        
    ImageUploadWidget getImageUploadWidget();
    
    ExperimentOutputPresenter getExperimentOutputPresenter();
    
    ExperimentOutputView getExperimentOutputView();
    
    ExperimentWiseMLOutputPresenter getExperimentWiseMLOutputPresenter();
    
    ExperimentWiseMLOutputView getExperimentWiseMLOutputView();
 }
