package nicelee.function.magnet.domain;

public class MediaInfo {

	String title;
	String img;
	String actors;

	public MediaInfo() {
	}

	public MediaInfo(String title, String img, String actors) {
		this.title = title;
		this.img = img;
		this.actors = actors;
	}

	public void print() {
		System.out.println("----------------------------");
		System.out.println(title);
		System.out.println(actors);
		System.out.println("----------------------------");
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}
}
