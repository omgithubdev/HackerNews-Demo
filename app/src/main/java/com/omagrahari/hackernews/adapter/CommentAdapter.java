package com.omagrahari.hackernews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omagrahari.hackernews.R;
import com.omagrahari.hackernews.helper.DateHelper;
import com.omagrahari.hackernews.model.Comment;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Constructor
     *
     * @param context
     * @param comments
     */
    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments.addAll(comments);
    }

    /**
     * Update data
     *
     * @param comments
     */
    public void setData(List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_comment, parent, false);

        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
        commentViewHolder.setViews(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    private class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView timeStamp, username, description;

        public CommentViewHolder(View itemView) {
            super(itemView);
            timeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
            username = (TextView) itemView.findViewById(R.id.username);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        public void setViews(Comment comment) {

            try {
                username.setText(comment.getUserName());
                description.setText(Html.fromHtml(comment.getDescription()));
                timeStamp.setText(DateHelper.parseDate(String.valueOf(comment.getTimeStamp())));

            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }
}
