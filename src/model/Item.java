package model;

public class Item {
	String info ;
	String img1;
	String img2;
	public Item(String info ,String img1 ,String img2) {
		this.info=info ;
		this.img1=img1;
		this.img2=img2;
	}
	public String getInfo() {
		return info;
	}
	 public String getImg1() {
		return img1;
	}
	 public String getImg2() {
		return img2;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public void setMatchID(String img1) {
		this.img1=img1;
	}
	public void setSearchID(String img2) {
		this.img2=img2;
	}

}
