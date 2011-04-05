package eu.wisebed.wiseui.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Binary image business object (BO) for Hibernate persistence.
 *
 * @author Soenke Nommensen
 */
@Entity
@Table(name = "binary_image")
public class BinaryImageBo implements Bo {

    @Id
    @GeneratedValue
    private Integer id;
    private String fileName;
    private long fileSize;
    @Lob
    private byte[] content;
    private String contentType;

    public BinaryImageBo() {
    }

    public BinaryImageBo(final Integer id, final String fileName,
                         final long fileSize, final byte[] content, final String contentType) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.content = content;
        this.contentType = contentType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(final long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "BinaryImageBo{"
                + "id=" + id
                + ", fileName='" + fileName + '\''
                + ", fileSize=" + fileSize
                + ", contentType='" + contentType + '\''
                + '}';
    }
}
