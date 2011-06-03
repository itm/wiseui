/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM), Research Academic Computer
 *                             Technology Institute (RACTI)
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
package eu.wisebed.wiseui.server.rpc;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import eu.wisebed.wiseui.api.PersistenceService;
import eu.wisebed.wiseui.persistence.PersistenceServiceProvider;
import eu.wisebed.wiseui.shared.dto.BinaryImage;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

@Singleton
public class ImageUploadServiceImpl extends UploadAction{


	private static final long serialVersionUID = 8765045952525138205L;

	private final Logger LOGGER = 
		LoggerFactory.getLogger(ImageUploadServiceImpl.class.getName());
	
	private final PersistenceService persistenceService = PersistenceServiceProvider.newPersistenceService();
	/**
	 * Override executeAction to save the received files in a custom place
	 * and delete these items from session
	 */
	@Override
	public String executeAction(HttpServletRequest request, 
			List<FileItem> sessionFiles) throws UploadActionException{
		String response = "";
		int cont = 0;
		
		for (FileItem item: sessionFiles){
			if (false == item.isFormField()){
				cont++;
				try {
					// Save a temporary file in the default system temp directory
					File uploadedFile = File.createTempFile("upload-", ".bin");

					// write item to a file
					item.write(uploadedFile);

					// open and read content from file stream
					FileInputStream in = new FileInputStream(uploadedFile);
				    byte fileContent[] = new byte[(int)uploadedFile.length()];
				    in.read(fileContent);

				    // store content in persistance
					BinaryImage image = new BinaryImage();
					image.setFileName(item.getName());
					image.setFileSize(item.getSize());
					image.setContent(fileContent);
					image.setContentType(item.getContentType());
					LOGGER.info("Storing image: [Filename-> "+ item.getName() + " ]");
					persistenceService.storeBinaryImage(image);

					// Compose an xml message with the full file information
					// which can be parsed in client side
					response += "<file-" + cont + "-field>" +
						item.getFieldName() + "</file-" + cont + "-field>\n";
					response += "<file-" + cont + "-name>" +
						item.getName() + "</file-" + cont + "-name>\n";
					response += "<file-" + cont + "-size>" +
						item.getSize() + "</file-" + cont + "-size>\n";
					response += "<file-" + cont + "-type>" +
						item.getContentType() + "</file-" + cont + "-type>\n";

				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}
		
		// Remove files from session because we have a copy of them
		removeSessionFileItems(request);
		
		// set the count of files
		response += "<file-count>" + cont + "</file-count>\n";
		
		// Send information of the received files to the client
		return "<response>\n" + response + "</response>\n";
	}
	
	public void removeItem(HttpServletRequest request, String fieldName) 
		throws UploadActionException{
		// TODO implement this when required
	}
}
