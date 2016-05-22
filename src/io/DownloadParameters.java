package io;

public class DownloadParameters {
	private String urlRegex;
	private String urlDelimitorStart;
	private String urlDelimitorEnd;
	private String imgRegex;
	private String imgDelimitorStart;
	private String imgDelimitorEnd;
	private String nextUrlRegex;
	private String nextUrlDelimitorStart;
	private String nextUrlDelimitorEnd;
	
	private boolean multipleImgPerLine;
	private boolean searchTags;
	
	public DownloadParameters(String urlRegex, String imgRegex, String nextUrlRegex){
		this.searchTags = false;
		this.multipleImgPerLine = false;
		this.urlRegex = urlRegex;
		this.imgRegex = imgRegex;
		this.nextUrlRegex = nextUrlRegex;
		this.urlDelimitorStart = "\"";
		this.urlDelimitorEnd = "\"";
		this.imgDelimitorStart = "\"";
		this.imgDelimitorEnd="/w650";
		this.nextUrlDelimitorStart ="\"";
		this.nextUrlDelimitorEnd = "\"";
	}

	public DownloadParameters(String urlRegex, String urlDelimitor, String imgRegex,
			String imgDelimitor, String nextUrlRegex, String nextUrlDelimitor) {
		this.urlRegex = urlRegex;
		this.urlDelimitorStart = urlDelimitor;
		this.urlDelimitorEnd = urlDelimitor;
		this.imgRegex = imgRegex;
		this.imgDelimitorStart = imgDelimitor;
		this.imgDelimitorEnd = imgDelimitor;
		this.nextUrlRegex = nextUrlRegex;
		this.nextUrlDelimitorStart = nextUrlDelimitor;
		this.nextUrlDelimitorEnd = nextUrlDelimitor;
		this.searchTags = false;
		this.multipleImgPerLine = false;
	}

	public String getUrlRegex() {
		return urlRegex;
	}

	public void setUrlRegex(String urlRegex) {
		this.urlRegex = urlRegex;
	}

	public void setUrlDelimitor(String urlDelimitor) {
		this.urlDelimitorStart = urlDelimitor;
		this.urlDelimitorEnd = urlDelimitor;
	}
	
	public void setUrl(String urlRegex, String urlDelimitor){
		this.urlRegex = urlRegex;
		this.urlDelimitorStart = urlDelimitor;
		this.urlDelimitorEnd = urlDelimitor;
	}

	public String getImgRegex() {
		return imgRegex;
	}

	public void setImgRegex(String imgRegex) {
		this.imgRegex = imgRegex;
	}

	public void setImgDelimitor(String imgDelimitor) {
		this.imgDelimitorStart = imgDelimitor;
		this.imgDelimitorEnd = imgDelimitor;
	}
	
	public void setImg(String imgRegex, String imgDelimitor){
		this.imgRegex = imgRegex;
		this.imgDelimitorStart = imgDelimitor;
		this.imgDelimitorEnd = imgDelimitor;
	}

	public void setNextUrlRegex(String nextUrlRegex) {
		this.nextUrlRegex = nextUrlRegex;
	}

	public void setNextUrlDelimitor(String nextUrlDelimitor) {
		this.nextUrlDelimitorStart = nextUrlDelimitor;
		this.nextUrlDelimitorEnd = nextUrlDelimitor;
	}
	
	public void setnextUrl(String nextUrlRegex, String nextUrlDelimitor){
		this.nextUrlRegex = nextUrlRegex;
		this.nextUrlDelimitorStart = nextUrlDelimitor;
		this.nextUrlDelimitorEnd = nextUrlDelimitor;
	}

	public boolean isSearchTags() {
		return searchTags;
	}

	public void setSearchTags(boolean searchTags) {
		this.searchTags = searchTags;
	}

	public boolean isMultipleImgPerLine() {
		return multipleImgPerLine;
	}

	public void setMultipleImgPerLine(boolean multipleImgPerLine) {
		this.multipleImgPerLine = multipleImgPerLine;
	}

	public String getUrlDelimitorStart() {
		return urlDelimitorStart;
	}

	public void setUrlDelimitorStart(String urlDelimitorStart) {
		this.urlDelimitorStart = urlDelimitorStart;
	}

	public String getUrlDelimitorEnd() {
		return urlDelimitorEnd;
	}

	public void setUrlDelimitorEnd(String urlDelimitorEnd) {
		this.urlDelimitorEnd = urlDelimitorEnd;
	}

	public String getImgDelimitorStart() {
		return imgDelimitorStart;
	}

	public void setImgDelimitorStart(String imgDelimitorStart) {
		this.imgDelimitorStart = imgDelimitorStart;
	}

	public String getImgDelimitorEnd() {
		return imgDelimitorEnd;
	}

	public void setImgDelimitorEnd(String imgDelimitorEnd) {
		this.imgDelimitorEnd = imgDelimitorEnd;
	}

	public String getNextUrlDelimitorStart() {
		return nextUrlDelimitorStart;
	}

	public void setNextUrlDelimitorStart(String nextUrlDelimitorStart) {
		this.nextUrlDelimitorStart = nextUrlDelimitorStart;
	}

	public String getNextUrlDelimitorEnd() {
		return nextUrlDelimitorEnd;
	}

	public void setNextUrlDelimitorEnd(String nextUrlDelimitorEnd) {
		this.nextUrlDelimitorEnd = nextUrlDelimitorEnd;
	}

	public String getNextUrlRegex() {
		return nextUrlRegex;
	}

}
