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
package eu.wisebed.wiseui.persistence.domain;

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

    private static final long serialVersionUID = 7836493821143856623L;
	
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

    @Override
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
