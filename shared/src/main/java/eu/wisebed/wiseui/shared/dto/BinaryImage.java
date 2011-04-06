package eu.wisebed.wiseui.shared.dto;

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
}
