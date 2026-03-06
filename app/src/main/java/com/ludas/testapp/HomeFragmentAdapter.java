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

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private List<User> users;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recId;
        protected final TextView recfname;
        protected final TextView reclname;
        protected final ImageButton recdelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            recId = (TextView) view.findViewById(R.id.recId);
            recfname = (TextView) view.findViewById(R.id.recfname);
            reclname = (TextView) view.findViewById(R.id.reclname);
            recdelete = (ImageButton) view.findViewById(R.id.recdelete);
        }
    }

    public interface OnListClicked {
        void onSelected(int userId);
    }

    public HomeFragmentAdapter(List<User> users) {
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
        viewHolder.recId.setText(Integer.toString(userId));
        viewHolder.recfname.setText(users.get(position).getFirstName());
        viewHolder.reclname.setText(users.get(position).getLastName());
        viewHolder.recdelete.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OnListClicked listener = (OnListClicked) view.getContext();
                    listener.onSelected(userId);
                }
                catch (ClassCastException e) {
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
