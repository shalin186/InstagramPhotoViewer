package com.codepath.instagramviewer.adapters;

import java.util.List;

import com.codepath.instagramviewer.models.PhotoViewerModel;
import com.codepath.instagramviewer.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PopularPhotosAdapter extends ArrayAdapter<PhotoViewerModel> {

	public static final long SECOND_IN_MILLIS = 1000;

	private static class ViewHolder {
		CircularImageView cvUserProfilePicture;
		TextView tvUsername;
		TextView tvRelativeTime;
		ImageView ivPopularPhoto;
		TextView tvLikes;
		ImageView ivLikes;
		TextView tvCaption;
		TextView tvComments;
		TextView tvComment1;
		TextView tvComment2;
		TextView tvComment3;
	}

	public PopularPhotosAdapter(Context context,
			List<PhotoViewerModel> popularPhotosList) {
		super(context, android.R.layout.simple_list_item_1, popularPhotosList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PhotoViewerModel popularPhoto = getItem(position);

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_single_row, parent, false);

			viewHolder.cvUserProfilePicture = (CircularImageView) convertView
					.findViewById(R.id.cvUserProfilePicture);
			viewHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.tvUsername);
			viewHolder.tvRelativeTime = (TextView) convertView
					.findViewById(R.id.tvRelativeTime);
			viewHolder.ivPopularPhoto = (ImageView) convertView
					.findViewById(R.id.ivPopularPhoto);
			viewHolder.tvLikes = (TextView) convertView
					.findViewById(R.id.tvLikes);
			viewHolder.ivLikes = (ImageView) convertView
					.findViewById(R.id.ivLikes);
			viewHolder.tvCaption = (TextView) convertView
					.findViewById(R.id.tvCaption);
			viewHolder.tvComments = (TextView) convertView
					.findViewById(R.id.tvComments);
			viewHolder.tvComment1 = (TextView) convertView
					.findViewById(R.id.tvComment1);
			viewHolder.tvComment2 = (TextView) convertView
					.findViewById(R.id.tvComment2);
			viewHolder.tvComment3 = (TextView) convertView
					.findViewById(R.id.tvComment3);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Picasso.with(getContext()).load(popularPhoto.userProfilePictureUrl)
				.fit().into(viewHolder.cvUserProfilePicture);
		viewHolder.tvUsername.setText(Html
				.fromHtml("<font color=\"#206199\"><b>" + popularPhoto.username
						+ "  " + "</b></font>"));
		String relativeTime = DateUtils.getRelativeTimeSpanString(
				popularPhoto.timeCreated * 1000, System.currentTimeMillis(),
				SECOND_IN_MILLIS)
				+ "";
		String relativeTimeInt = relativeTime.substring(0,
				relativeTime.indexOf(" "));
		String relativeTimeStr = relativeTime.substring(
				relativeTime.indexOf(" ") + 1, relativeTime.indexOf(" ") + 2);
		viewHolder.tvRelativeTime.setText(relativeTimeInt + relativeTimeStr);
		viewHolder.tvRelativeTime.setTextColor(Color.GRAY);
		Picasso.with(getContext())
				.load(popularPhoto.imageStandardResolutionUrl)
				.into(viewHolder.ivPopularPhoto);
		if (popularPhoto.likesCount > 0) {
			viewHolder.tvLikes.setText(Html
					.fromHtml("<font color=\"#206199\"><b>"
							+ popularPhoto.likesCount + "  likes"
							+ "</b></font>"));
		} else {
			viewHolder.tvLikes.setVisibility(View.GONE);
			viewHolder.ivLikes.setVisibility(View.GONE);
		}

		if (popularPhoto.commentsCount > 0) {
			viewHolder.tvComment1.setVisibility(View.VISIBLE);
			viewHolder.tvComment1
					.setText(Html.fromHtml("<font color=\"#206199\"><b>"
							+ popularPhoto.comments[0].username + "  "
							+ "</b></font>" + "<font color=\"#000000\">"
							+ popularPhoto.comments[0].commentText + "</font>"));

			if (popularPhoto.commentsCount > 1) {
				viewHolder.tvComment2.setVisibility(View.VISIBLE);
				viewHolder.tvComment2.setText(Html
						.fromHtml("<font color=\"#206199\"><b>"
								+ popularPhoto.comments[1].username + "  "
								+ "</b></font>" + "<font color=\"#000000\">"
								+ popularPhoto.comments[1].commentText
								+ "</font>"));

				if (popularPhoto.commentsCount > 2) {
					viewHolder.tvComment3.setVisibility(View.VISIBLE);
					viewHolder.tvComment3.setText(Html
							.fromHtml("<font color=\"#206199\"><b>"
									+ popularPhoto.comments[2].username + "  "
									+ "</b></font>"
									+ "<font color=\"#000000\">"
									+ popularPhoto.comments[2].commentText
									+ "</font>"));

					if (popularPhoto.commentsCount > 3) {
						viewHolder.tvComments.setVisibility(View.VISIBLE);
						viewHolder.tvComments.setText("view all "
								+ popularPhoto.commentsCount + " comments");
						viewHolder.tvComments.setTextColor(Color.GRAY);
					} else {
						viewHolder.tvComments.setVisibility(View.GONE);
					}
				} else {
					viewHolder.tvComment3.setVisibility(View.GONE);
				}
			} else {
				viewHolder.tvComment2.setVisibility(View.GONE);
			}
		} else {
			viewHolder.tvComment1.setVisibility(View.GONE);
		}

		viewHolder.tvCaption.setText(Html
				.fromHtml("<font color=\"#206199\"><b>" + popularPhoto.username
						+ "  " + "</b></font>" + "<font color=\"#000000\">"
						+ popularPhoto.caption + "</font>"));

		return convertView;
	}

}
