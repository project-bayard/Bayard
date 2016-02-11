package edu.usm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * Wrapper for a file uploaded by a Bayard user.
 */
@Entity(name = "user_file_upload")
public class UserFileUpload extends BasicEntity {

    @Column
    @NotNull
    private String fileName;

    @Column
    private String fileType;

    @Column
    private String description;

    @Lob
    @Column
    private byte[] fileContent;

    public UserFileUpload() {
        super();
    }

    /**
     * @param fileName the name of the file
     * @param fileContent the file
     */
    public UserFileUpload(String fileName, byte[] fileContent) {
        super();
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the file type
     */
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return a description of the file
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the file
     */
    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
