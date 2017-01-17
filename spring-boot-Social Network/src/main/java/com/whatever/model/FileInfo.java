package com.whatever.model;

public class FileInfo {
	
	private String baseName;
	private String extension;
	private String subDirectory;
	private String baseDirectory;
	
	public FileInfo(String baseName, String extension, String subDirectory, String baseDirectory) {
		this.baseName = baseName;
		this.extension = extension;
		this.subDirectory = subDirectory;
		this.baseDirectory = baseDirectory;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getSubDirectory() {
		return subDirectory;
	}

	public void setSubDirectory(String subDirectory) {
		this.subDirectory = subDirectory;
	}

	public String getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	@Override
	public String toString() {
		return "FileInfo [baseName=" + baseName + ", extension=" + extension + ", subDirectory=" + subDirectory
				+ ", baseDirectory=" + baseDirectory + "]";
	}
	
	
	
	

}
