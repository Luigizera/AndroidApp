package com.ludas.testapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ludas.testapp.R;
import com.ludas.testapp.database.AppDatabase;
import com.ludas.testapp.database.FinancialEntry;
import com.ludas.testapp.views.SimpleBarChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphFragment extends Fragment {

    public static final String TAG = "GraphFragment";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private SimpleBarChartView chartView;
    private TextView tvTotalIn, tvTotalOut, tvNetBalance;

    public static GraphFragment newInstance() {
        return new GraphFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        
        chartView = view.findViewById(R.id.chart_view);
        tvTotalIn = view.findViewById(R.id.tv_total_in);
        tvTotalOut = view.findViewById(R.id.tv_total_out);
        tvNetBalance = view.findViewById(R.id.tv_net_balance);

        loadData();
        
        return view;
    }

    private void loadData() {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<FinancialEntry> entries = db.storageLogDao().getFinancialEntries();
            
            Map<String, Double> tempIn = new HashMap<>();
            Map<String, Double> tempOut = new HashMap<>();
            double totalIn = 0;
            double totalOut = 0;

            for (FinancialEntry entry : entries) {
                String date = entry.date.length() >= 10 ? entry.date.substring(0, 10) : entry.date;
                if (entry.type == 0) { // In
                    double cost = entry.quantity * entry.purchasePrice;
                    tempIn.put(date, tempIn.getOrDefault(date, 0.0) + cost);
                    totalIn += cost;
                } else { // Out
                    double revenue = entry.quantity * entry.price;
                    tempOut.put(date, tempOut.getOrDefault(date, 0.0) + revenue);
                    totalOut += revenue;
                }
            }

            final double finalTotalIn = totalIn;
            final double finalTotalOut = totalOut;

            mainHandler.post(() -> {
                if (!isAdded()) return;
                
                chartView.setData(tempIn, tempOut);
                tvTotalIn.setText(String.format(Locale.getDefault(), "R$ %.2f", finalTotalIn));
                tvTotalOut.setText(String.format(Locale.getDefault(), "R$ %.2f", finalTotalOut));
                
                double net = finalTotalOut - finalTotalIn;
                tvNetBalance.setText(String.format(Locale.getDefault(), "R$ %.2f", net));
                tvNetBalance.setTextColor(net >= 0 ? 0xFF4CAF50 : 0xFFFF0000);
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
