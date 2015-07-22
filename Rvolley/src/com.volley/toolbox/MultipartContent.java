package com.volley.toolbox;

import java.io.File;

/**
 * Created by Shedings on 2015/7/21.
 */
public class MultipartContent {
    private String type;
    private String fileType;
    private String fileName;
    private byte[] binary;
    private File file;
    public static final String BINARYFILETYPE = "binary";
    public static final String DEFAUTLFILETYPE = "file";

    public MultipartContent(String fileType, String fileName, byte[] binary) {
        this(null,BINARYFILETYPE,fileType,fileName,binary);
    }

    public MultipartContent(String fileType,String fileName,File file) {
        this(file,DEFAUTLFILETYPE,fileType,fileName,null);
    }

    public MultipartContent(File file, String type, String fileType, String fileName, byte[] binary) {
        this.file = file;
        this.type = type;
        this.fileType = fileType;
        this.fileName = fileName;
        this.binary = binary;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }
}
