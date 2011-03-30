package eu.wisebed.wiseui.server.util;

import java.io.DataInputStream;
import java.io.InputStream;

import eu.wisebed.testbed.api.wsn.v22.Program;
import eu.wisebed.testbed.api.wsn.v22.ProgramMetaData;
import eu.wisebed.wiseui.server.model.Image;
import eu.wisebed.wiseui.shared.exception.ExperimentationException;

public class ImageUtil {
	
	public static final Program readImage(final Image image,
		final String name, final String other, final String platform, 
		final String version) throws ExperimentationException{
		
		final ProgramMetaData programMetaData = new ProgramMetaData();
		programMetaData.setName(name);
		programMetaData.setOther(other);
		programMetaData.setPlatform(platform);
		programMetaData.setVersion(version);

		Program program = new Program();
		try{
			InputStream is = image.getContentStream();
			DataInputStream dis = new DataInputStream(is);
			long length = image.getImageFileSize();
			byte[] binaryData = new byte[(int) length];
			dis.readFully(binaryData);
			program.setProgram(binaryData);
			program.setMetaData(programMetaData);
		}catch(Exception e){
			throw new ExperimentationException(e.getMessage());
		}
		
		return program;
	}

}
