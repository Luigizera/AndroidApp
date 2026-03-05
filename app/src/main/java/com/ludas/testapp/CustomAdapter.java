package com.ludas.testapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.User;
import com.ludas.testapp.database.UserDao;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<User> users;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView recId;
        private final TextView recfname;
        private final TextView reclname;
        private final ImageButton recdelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            recId = (TextView) view.findViewById(R.id.recId);
            recfname = (TextView) view.findViewById(R.id.recfname);
            reclname = (TextView) view.findViewById(R.id.reclname);
            recdelete = (ImageButton) view.findViewById(R.id.recdelete);
        }

        public TextView getRecId() {
            return recId;
        }

        public TextView getRecfname() {
            return recfname;
        }

        public TextView getReclname() {
            return reclname;
        }

        public ImageButton getRecdelete() {
            return recdelete;
        }
    }

    public CustomAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.singlerowdesign, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int userId = users.get(position).getId();
        viewHolder.getRecId().setText(Integer.toString(userId));
        viewHolder.getRecfname().setText(users.get(position).getFirstName());
        viewHolder.getReclname().setText(users.get(position).getLastName());
        viewHolder.getRecdelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase database = AppDatabase.getInstance(view.getContext());

                UserDao userDao = database.userDao();
                userDao.deleteById(userId);
                int pos = viewHolder.getAbsoluteAdapterPosition();
                users.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
