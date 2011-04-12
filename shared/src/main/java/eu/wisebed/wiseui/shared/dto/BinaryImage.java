package eu.wisebed.wiseui.shared.dto;

import java.util.Arrays;

/**
 * Binary image data transfer object (DTO) for Hibernate persistence.
 *
 * @author Soenke Nommensen
 */
public class BinaryImage implements Dto {

	private static final long serialVersionUID = -5391584607559079355L;

	private Integer id;
    private String fileName;
    private long fileSize;
    private byte[] content;
    private String contentType;

    public BinaryImage() {
    }

    public BinaryImage(final Integer id,
                       final String fileName,
                       final long fileSize,
                       final byte[] content,
                       final String contentType) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.content = content;
        this.contentType = contentType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "BinaryImage{"
                + "id=" + id
                + ", fileName='" + fileName + '\''
                + ", fileSize=" + fileSize
                + ", contentType='" + contentType + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BinaryImage that = (BinaryImage) o;

        if (fileSize != that.fileSize) return false;
        if (!Arrays.equals(content, that.content)) return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + (content != null ? Arrays.hashCode(content) : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        return result;
    }
}
