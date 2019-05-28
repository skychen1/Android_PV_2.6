/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.ruihua.face.recognition.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihua.face.recognition.R;
import com.ruihua.face.recognition.api.FaceApi;
import com.ruihua.face.recognition.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends Activity {

    private RecyclerView UserListRv;
    private UserAdapter adapter;
    private List<User> userList = new ArrayList<>();
    private Button backBtn;
    private String groupId = "";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_activity_user_list);

        findView();
        addListener();
        initGroup();
    }

    private void findView() {
        UserListRv = (RecyclerView) findViewById(R.id.user_list_rv);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        UserListRv.setLayoutManager(layoutmanager);
        UserListRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new UserAdapter();
        UserListRv.setAdapter(adapter);
    }

    private void initGroup() {
        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getStringExtra("group_id");
            List<User> userList = FaceApi.getInstance().getUserList(groupId);
            if (userList == null || userList.isEmpty()) {
                return;
            }
            adapter.setUserList(userList);
        }
    }

    private void addListener() {
        adapter.setOnItemClickLitsener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                List<User> userList = adapter.getUserList();
                if (userList.size() > position) {
                    User user = userList.get(position);
                    Intent intent = new Intent(UserListActivity.this, UserActivity.class);
                    intent.putExtra("user_id", user.getUserId());
                    intent.putExtra("user_info", user.getUserInfo());
                    intent.putExtra("group_id", user.getGroupId());

                    startActivity(intent);
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (position <= adapter.getUserList().size()) {
                    showAlertDialog(adapter.getUserList().get(position));
                }
            }
        });
    }

    public void showAlertDialog(final User user) {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("删除用户");
        alertBuilder.setMessage("确认删除该用户？删除该用户将删除用户下所有的用户数据");
        alertBuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertBuilder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (FaceApi.getInstance().userDelete(user.getUserId(), user.getGroupId())) {
                    Toast.makeText(UserListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    adapter.getUserList().remove(user);
                    adapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

        private List<User> userList = new ArrayList<>();
        private OnItemClickListener mOnItemClickListener;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView userId;
            TextView userInfo;

            public ViewHolder(View view) {
                super(view);
                userId = (TextView) view.findViewById(R.id.user_id_tv);
                userInfo = (TextView) view.findViewById(R.id.user_info_tv);
            }
        }

        public void setUserList(List<User> userList) {
            this.userList = userList;
            this.notifyDataSetChanged();
        }

        public List<User> getUserList() {
            return userList;
        }

        public void setOnItemClickLitsener(OnItemClickListener mOnItemClickLitsener) {
            mOnItemClickListener = mOnItemClickLitsener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.face_activity_user_item_layout, parent,
                    false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            User user = userList.get(position);
            holder.userId.setText("User ID: " + user.getUserId());
            holder.userInfo.setText("Userinfo：" + user.getUserInfo());

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
