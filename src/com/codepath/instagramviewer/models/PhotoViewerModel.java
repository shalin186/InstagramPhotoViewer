package com.codepath.instagramviewer.models;

public class PhotoViewerModel {

	public class Comments {
		public String username;
		public String commentText;
	}

	public String username;
	public String userProfilePictureUrl;
	public String caption;
	public String imageStandardResolutionUrl;
	public int imageStandardResolutionHeight;
	public int likesCount;
	public int commentsCount;
	public long timeCreated;
	public Comments[] comments;

	public PhotoViewerModel() {
		this.comments = new Comments[3];
		for (int i = 0; i < 3; i++) {
			this.comments[i] = new Comments();
		}
	}

}
