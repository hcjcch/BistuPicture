package com.example.personal;

public class personSuperData {
	private String id;
	private String name;
	private String img_main;
	private String like_num;
	public String get_id() {
		return id;
	}
	public String get_name(){
		return name;
	}
	public String get_img_main(){
		return img_main;
	}
	public String get_like_num(){
		return like_num;
	}
	public void set_id(String id){
		this.id = id;
	}
	public void set_name(String name) {
		this.name = name;
	}
	public void set_imgmain(String img_main) {
		this.img_main = img_main;
	}
	public void set_likenum(String like_num) {
		this.like_num = like_num;
	}
}
