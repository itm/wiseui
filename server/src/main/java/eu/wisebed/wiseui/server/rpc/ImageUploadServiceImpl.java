package eu.wisebed.wiseui.server.rpc;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.google.inject.Singleton;

import eu.wisebed.wiseui.server.model.Image;
import eu.wisebed.wiseui.server.util.WiseUiHibernateUtil;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

@SuppressWarnings("serial")
@Singleton
public class ImageUploadServiceImpl extends UploadAction{

	private final Logger LOGGER = 
		Logger.getLogger(ImageUploadServiceImpl.class.getName());
	
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

					item.write(uploadedFile);

					FileInputStream content = new FileInputStream(uploadedFile);
					Image image = new Image();
					image.setImageFileName(item.getName());
					image.setImageFileSize(item.getSize());
					image.setContentStream(content);
					image.setContentType(item.getContentType());
					LOGGER.log(Level.INFO, "Storing image: [Filename-> " 
							+ item.getName() + " ]");
					saveImage(image);
					
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
					// TODO Auto-generated catch block
					throw new UploadActionException(e);
				}
			}
		}
		// Remove files from session because we have a copy of them
		removeSessionFileItems(request);
		
		// Send information of the received files to the client
		return "<response>\n" + response + "</response>\n";
		
	}
	
	public void removeItem(HttpServletRequest request, String fieldName) 
		throws UploadActionException{
		// TODO implement this when required
	}
	
	private static final void saveImage(Image image){
		final Session session = WiseUiHibernateUtil.getSessionFactory()
			.getCurrentSession();
		session.beginTransaction();
		session.save(image);
		session.getTransaction().commit();
	}
}
