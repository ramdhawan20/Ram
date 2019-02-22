package com.hcl.bss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.type.BlobType;

import com.hcl.bss.dto.ErrorCsvFileDto;

@Entity
@Table(name = "ErrorLog")
public class ErrorCsvFile implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "File_Id", nullable = false)
	private long fileId;
	@Column(name = "Error_Log_File")
	private byte[] errorLogFile;
	private String fileName;
	private String description;
	private String fileType;
	

   
	public ErrorCsvFile() {
		super();
	}
	public ErrorCsvFile(long fileId, byte[] errorLogFile) {
		super();
		this.fileId = fileId;
		this.errorLogFile = errorLogFile;
	}
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public byte[] getErrorLogFile() {
		return errorLogFile;
	}
	public void setErrorLogFile(byte[] errorLogFile) {
		this.errorLogFile = errorLogFile;
	}

	
	
}
