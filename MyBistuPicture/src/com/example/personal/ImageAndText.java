package com.example.personal;

public class ImageAndText {
	private String imageUrl;
	private String key;

	public ImageAndText(String imageUrl, String text) {
		this.imageUrl = imageUrl;
		this.key = text;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getText() {
		return key;
	}
}
