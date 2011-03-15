package eu.wisebed.wiseui.server.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

public class Image {

	private int imageid;
	private String imageFileName;
	private long imageFileSize;
	private Blob content;
	private String contentType;
	
	public Image(){}
	
	public int getImageid(){
		return this.imageid;
	}
	
	public void setImageid(int imageid){
		this.imageid = imageid;
	}
	
	public String getImageFileName(){
		return this.imageFileName;
	}
	
	public void setImageFileName(String imageFileName){
		this.imageFileName = imageFileName;
	}
	
	private Blob getContent(){
		return content;
	}


	private void setContent( Blob content ){
		this.content = content;
	}

	public InputStream getContentStream()
		throws SQLException{
		if (getContent() == null)
			return null;
		return getContent().getBinaryStream();
	}


	public void setContentStream( InputStream sourceStream )
		throws IOException{
		setContent( Hibernate.createBlob( sourceStream ) );
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setImageFileSize(long imageFileSize) {
		this.imageFileSize = imageFileSize;
	}

	public long getImageFileSize() {
		return imageFileSize;
	}

}
