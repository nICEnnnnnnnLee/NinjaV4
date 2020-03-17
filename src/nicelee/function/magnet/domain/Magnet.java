package nicelee.function.magnet.domain;

public class Magnet {

	MediaInfo info;
	String title;
	String link;
	String size;
	String time;
	
	public Magnet(MediaInfo info, String title, String link, String size, String time) {
		this.title = title;
		this.link = link;
		this.info = info;
		this.size = size;
		this.time = time;
	}

	public void print() {
		System.out.println(title);
		System.out.println(size + "\t" + time);
		System.out.println(link);
		System.out.println("-------------------");
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public MediaInfo getInfo() {
		return info;
	}

	public void setInfo(MediaInfo info) {
		this.info = info;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
