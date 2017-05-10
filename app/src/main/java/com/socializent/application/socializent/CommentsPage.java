package com.socializent.application.socializent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.socializent.application.socializent.Controller.EventBackgroundTask;
import com.socializent.application.socializent.Fragments.NavigationDrawerSecond;
import com.socializent.application.socializent.Fragments.NavigationDrawerThird;
import com.socializent.application.socializent.Modal.Comment;
import com.socializent.application.socializent.Modal.Event;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.socializent.application.socializent.Controller.PersonBackgroundTask.msCookieManager;

/**
 * Created by Toshıba on 9.05.2017.
 */

public class CommentsPage extends Activity {

    private Event mainEvent;
    private String id;
    private String title;
    private Button sendCommentButton;
    private EditText userComment;
    private Context context;
    //private TextView eventTitle;
    private EventBackgroundTask listCommentsTask;
    //private JSONArray commentsArray;
    //private TextView incomingTextView;
    //private TextView outgoingTextView;
    //View commentsView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_page);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        //id = "";
        //title="";
        id = intent.getStringExtra("eventId");
        title = intent.getStringExtra("eventTitle");

        context = this;

        mainEvent = new Event();
        mainEvent.setId(id);
        mainEvent.setName(title);

        sendCommentButton = (Button) findViewById(R.id.send_comment_button);
        userComment = (EditText) findViewById(R.id.my_comment);

        //eventTitle = (TextView) findViewById(R.id.title_for_event);
        //eventTitle.setText(title);
        try {
            recyclerView.setAdapter(new SimpleAdapter(recyclerView));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = userComment.getText().toString();
                EventBackgroundTask addCommentTask = new EventBackgroundTask(context);
                addCommentTask.execute("6",mainEvent.getId(),comment);
                EventBackgroundTask refresh = new EventBackgroundTask(context);
                refresh.execute("7", mainEvent.getId());
                try {
                    recyclerView.setAdapter(new SimpleAdapter(recyclerView));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private static class SimpleAdapter extends RecyclerView.Adapter<CommentsPage.SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;

        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;

        private List<HttpCookie> cookieList = msCookieManager.getCookieStore().getCookies();;
        private String eventComments = "";
        private JSONArray commentsArray = null;

        public SimpleAdapter(RecyclerView recyclerView) throws JSONException {
            this.recyclerView = recyclerView;

            for (int i = 0; i < cookieList.size(); i++) {
                if (cookieList.get(i).getName().equals("eventComments")){
                    eventComments = cookieList.get(i).getValue();
                    break;
                }
            }
            commentsArray = new JSONArray(eventComments);
        }

        @Override
        public CommentsPage.SimpleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item2, parent, false);
            return new CommentsPage.SimpleAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CommentsPage.SimpleAdapter.ViewHolder holder, int position) {

            JSONObject commentObject;
            Comment comment = new Comment();
            JSONObject user = null;

            try {

                commentObject = (JSONObject) commentsArray.get(position);
                user =(JSONObject)(commentObject.get("creator"));

                comment.setContent(commentObject.getString("content"));
                comment.setCreatorName(user.getString("fullName"));
                comment.setCreationDate((Long) commentObject.get("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.bind(position,comment,1);


        }

        @Override
        public int getItemCount() {
            return commentsArray.length();
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ExpandableLayout expandableLayout;
            private TextView commentText;
            private TextView commentText2;
            private TextView creationDate;
            private TextView creatorName;


            private int position;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout2);
                expandableLayout.setInterpolator(new OvershootInterpolator());

                commentText = (TextView) itemView.findViewById(R.id.comment_text);
                commentText2 = (TextView) itemView.findViewById(R.id.comment_text2);
                creationDate = (TextView) itemView.findViewById(R.id.creation_date);
                creatorName = (TextView) itemView.findViewById(R.id.creator_name);

                commentText.setOnClickListener(this);

                commentText.setVisibility(View.VISIBLE);
                //commentText2.setOnClickListener(this);
            }

            public void bind(int position, Comment comment,int type) {
                this.position = position;

                commentText.setText(comment.getContent());

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

                creationDate.setText(formatter.format(comment.getCreationDate()));
                commentText.setSelected(false);
                creatorName.setText(comment.getCreatorName());

                expandableLayout.collapse(false);
            }

            @Override
            public void onClick(View view) {
                CommentsPage.SimpleAdapter.ViewHolder holder = (CommentsPage.SimpleAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.commentText.setSelected(false);
                    holder.expandableLayout.collapse();
                }

                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    commentText.setSelected(true);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }
        }
    }
}
