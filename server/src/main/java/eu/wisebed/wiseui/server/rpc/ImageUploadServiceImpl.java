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
package eu.wisebed.wiseui.server.rpc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

@Singleton
public class ImageUploadServiceImpl extends UploadAction{


	private static final long serialVersionUID = 8765045952525138205L;

	@SuppressWarnings("unused")
	private final Logger LOGGER = 
		LoggerFactory.getLogger(ImageUploadServiceImpl.class.getName());
	
	/**
	 * Override executeAction to save the received files in a custom place
	 * and delete these items from session
	 */
	@Override
	public String executeAction(HttpServletRequest request, 
			List<FileItem> sessionFiles) throws UploadActionException{
//		String response = "";
//		int cont = 0;
//		
//		for (FileItem item: sessionFiles){
//			if (false == item.isFormField()){
//				cont++;
//				try {
//					// Save a temporary file in the default system temp directory
//					File uploadedFile = File.createTempFile("upload-", ".bin");
//
//					item.write(uploadedFile);
//
//					FileInputStream content = new FileInputStream(uploadedFile);
//					BinaryImage image = new BinaryImage();
//					image.setImageFileName(item.getName());
//					image.setImageFileSize(item.getSize());
//					image.setContentStream(content.);
//					image.setContentType(item.getContentType());
//					LOGGER.log(Level.INFO, "Storing image: [Filename-> "
//							+ item.getName() + " ]");
//					ImageServiceManager.saveImage(image);
//
//					// Compose an xml message with the full file information
//					// which can be parsed in client side
//					response += "<file-" + cont + "-field>" +
//						item.getFieldName() + "</file-" + cont + "-field>\n";
//					response += "<file-" + cont + "-name>" +
//						item.getName() + "</file-" + cont + "-name>\n";
//					response += "<file-" + cont + "-size>" +
//						item.getSize() + "</file-" + cont + "-size>\n";
//					response += "<file-" + cont + "-type>" +
//						item.getContentType() + "</file-" + cont + "-type>\n";
//
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new UploadActionException(e);
//				}
//			}
//		}
//		// Remove files from session because we have a copy of them
//		removeSessionFileItems(request);
//		
//		// Send information of the received files to the client
//		return "<response>\n" + response + "</response>\n";
		
		return null;
	}
	
	public void removeItem(HttpServletRequest request, String fieldName) 
		throws UploadActionException{
		// TODO implement this when required
	}
}
