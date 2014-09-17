package com.codepath.instagramviewer.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;

import com.codepath.instagramviewer.R;
import com.codepath.instagramviewer.adapters.PopularPhotosAdapter;
import com.codepath.instagramviewer.models.PhotoViewerModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class PhotoViewerActivity extends Activity {

	public static final String CLIENT_ID = "ed02f5ffaa204d5399933cbf93519825";
	PullToRefreshListView lvPopularPhotos;
	PullToRefreshListView lvPopularPhotos1;
	ArrayList<PhotoViewerModel> popularPhotosList;
	PopularPhotosAdapter popularPhotosAdapter;
	String popularPhotosUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_viewer);

		lvPopularPhotos = (PullToRefreshListView) findViewById(R.id.lvPopularPhotos);
		lvPopularPhotos.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				ShowPopularPhotos();
			}
			
		});
		
		popularPhotosList = new ArrayList<PhotoViewerModel>();
		popularPhotosAdapter = new PopularPhotosAdapter(this, popularPhotosList);
		lvPopularPhotos.setAdapter(popularPhotosAdapter);

		// setup popular URL end point
		popularPhotosUrl = "https://api.instagram.com/v1/media/popular?client_id="
				+ CLIENT_ID;

		ShowPopularPhotos();
	}

	private void ShowPopularPhotos() {
		// create n/w client
		AsyncHttpClient client = new AsyncHttpClient();

		// trigger n/w response
		client.get(popularPhotosUrl, new JsonHttpResponseHandler() {

			// Handle the successful photos
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				JSONArray photosJSON = null;

				try {
					// Get JSON data Array
					photosJSON = response.getJSONArray("data");
					popularPhotosList.clear();
					for (int i = 0; i < photosJSON.length(); i++) {
						// Get each element of JSON Array as JSON Object
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						// Setup a new photoviewer Model by parsing JSON data
						PhotoViewerModel photo = new PhotoViewerModel();

						// username = { "data" => [] => "user" => "username" }
						photo.username = photoJSON.getJSONObject("user")
								.getString("username");

						// userProfilePictureUrl = { "data" => [] => "user" =>
						// "profile_picture" }
						photo.userProfilePictureUrl = photoJSON.getJSONObject(
								"user").getString("profile_picture");

						// timeCreated = { "data" => [] => "created_time" }
						photo.timeCreated = photoJSON.getLong("created_time");

						// caption = { "data" => [] => "caption" => "text" }
						if (photoJSON.optJSONObject("caption") != null) {
							photo.caption = photoJSON.getJSONObject("caption")
									.getString("text");
						}

						// imageStandardResolutionUrl = { "data" => [] =>
						// "images" =>
						// "standard_resolution" => "url" }
						if (photoJSON.getJSONObject("images") == null
								|| photoJSON.getJSONObject("images")
										.getJSONObject("standard_resolution") == null) {
							continue;
						}
						photo.imageStandardResolutionUrl = photoJSON
								.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getString("url");

						// imageStandardResolutionHeight = { "data" => [] =>
						// "images" =>
						// "standard_resolution" => "height" }
						photo.imageStandardResolutionHeight = photoJSON
								.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getInt("height");

						// likes = { "data" => [] => "likes" => "count" }
						photo.likesCount = photoJSON.getJSONObject("likes")
								.getInt("count");

						// commentsCount = { "data" => [] => "comments" =>
						// "count" }
						photo.commentsCount = photoJSON.getJSONObject(
								"comments").getInt("count");

						// comments.username = { "data" => [] => "comments" =>
						// "data" => [] => "from" => "username"}

						// comments.commentText = { "data" => [] => "comments"
						// =>
						// "data" => [] => "text" }

						JSONArray photosCommentsJSON = null;
						photosCommentsJSON = photoJSON
								.getJSONObject("comments").getJSONArray("data");

						int numCommentsObjects = photosCommentsJSON.length();
						int k = 0;

						for (int j = numCommentsObjects - 1; j > numCommentsObjects -1 - 3
								&& j > 0; j--) {
							JSONObject photoCommentJSON = photosCommentsJSON
									.getJSONObject(j);
							photo.comments[k].username = photoCommentJSON
									.getJSONObject("from")
									.getString("username");
							photo.comments[k].commentText = photoCommentJSON
									.getString("text");
							k++;
						}
						popularPhotosList.add(photo);
					}
					// Update the List of popular Photos
					popularPhotosAdapter.notifyDataSetChanged();
					lvPopularPhotos.onRefreshComplete();
				} catch (JSONException e) {
					// json parsing is invalid
					e.printStackTrace();
				}

			}

			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

		// handle successful response
	}
}
