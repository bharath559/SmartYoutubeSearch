package com.bkongara.smartyoutubesearch;


import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class YoutubeAdaptor extends RecyclerView.Adapter<YoutubeAdaptor.MyViewHolder>{

    private Context mContext;
    private List<VideoItem> videoList;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView thumbnail;
        public TextView videoTitle, videoId, videoDescription;
        public RelativeLayout videoView;

        public MyViewHolder(View view) {

            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.video_thumbnail);
            videoTitle = (TextView) view.findViewById(R.id.video_title);
            videoId = (TextView) view.findViewById(R.id.video_id);
            videoDescription = (TextView) view.findViewById(R.id.video_description);
            videoView = (RelativeLayout) view.findViewById(R.id.video_view);
        }
    }

    public YoutubeAdaptor(Context mContext, List<VideoItem> mVideoList) {
        this.mContext = mContext;
        this.videoList = mVideoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {

        final VideoItem eachVideo = videoList.get(position);

        holder.videoId.setText("Video ID : "+eachVideo.getId()+" ");
        holder.videoTitle.setText(eachVideo.getTitle());
        holder.videoDescription.setText(eachVideo.getDescription());
        Picasso.with(mContext)
                .load(eachVideo.getThumbnailURL())
                .resize(480,270)
                .centerCrop()
                .into(holder.thumbnail);

        holder.videoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("VIDEO_ID", eachVideo.getId());
                intent.putExtra("VIDEO_TITLE",eachVideo.getTitle());
                intent.putExtra("VIDEO_DESC",eachVideo.getDescription());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


}
