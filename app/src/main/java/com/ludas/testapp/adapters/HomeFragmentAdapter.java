package com.ludas.testapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.User;
import com.ludas.testapp.database.UserDao;

import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {

    private List<User> users;
    private AppDatabase database;
    private UserDao userDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView recDate;
        protected final TextView recFirstName;
        protected final TextView recLastName;
        protected final ImageButton recDelete;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            recDate = (TextView) view.findViewById(R.id.singlerowdesign_recview_date);
            recFirstName = (TextView) view.findViewById(R.id.singlerowdesign_recview_firstName);
            recLastName = (TextView) view.findViewById(R.id.singlerowdesign_recview_lastName);
            recDelete = (ImageButton) view.findViewById(R.id.singlerowdesign_recview_delete);
        }
    }

    public interface OnListClicked {
        void onSelected(String userDate);
    }

    public HomeFragmentAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.singlerowdesign, viewGroup, false);
        database = AppDatabase.getInstance(view.getContext());
        userDao = database.userDao();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String userDate = users.get(position).getDate();
        viewHolder.recDate.setText(userDate);
        viewHolder.recFirstName.setText(users.get(position).getFirstName());
        viewHolder.recLastName.setText(users.get(position).getLastName());
        viewHolder.recDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDao.deleteByDate(userDate);
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
                    listener.onSelected(userDate);
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
