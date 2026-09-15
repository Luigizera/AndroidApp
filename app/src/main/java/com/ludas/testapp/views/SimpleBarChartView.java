package com.ludas.testapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SimpleBarChartView extends View {

    private Map<String, Double[]> data = new TreeMap<>();
    private final Paint paintIn = new Paint();
    private final Paint paintOut = new Paint();
    private final Paint paintText = new Paint();

    public SimpleBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paintIn.setColor(Color.RED);
        paintIn.setStyle(Paint.Style.FILL);

        paintOut.setColor(0xFF4CAF50); // Green
        paintOut.setStyle(Paint.Style.FILL);

        paintText.setColor(Color.GRAY);
        paintText.setTextSize(30f);
        paintText.setAntiAlias(true);
    }

    public void setData(Map<String, Double> inData, Map<String, Double> outData) {
        this.data = new TreeMap<>();
        for (String date : inData.keySet()) {
            data.put(date, new Double[]{inData.get(date), 0.0});
        }
        for (String date : outData.keySet()) {
            Double[] vals = data.getOrDefault(date, new Double[]{0.0, 0.0});
            vals[1] = outData.get(date);
            data.put(date, vals);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null || data.isEmpty()) return;

        List<String> dates = new ArrayList<>(data.keySet());
        if (dates.size() > 7) {
            dates = dates.subList(dates.size() - 7, dates.size());
        }

        float width = getWidth();
        float height = getHeight();
        float padding = 40f;
        float chartHeight = height - padding * 2;
        float barWidth = (width / (dates.size() * 3f));
        float spacing = width / dates.size();

        double maxVal = 0;
        for (String d : dates) {
            Double[] v = data.get(d);
            maxVal = Math.max(maxVal, Math.max(v[0], v[1]));
        }
        if (maxVal == 0) maxVal = 1;

        for (int i = 0; i < dates.size(); i++) {
            float xBase = spacing * i + (spacing / 2f);
            Double[] vals = data.get(dates.get(i));

            float hIn = (float) (vals[0] / maxVal * chartHeight);
            float hOut = (float) (vals[1] / maxVal * chartHeight);

            // Draw IN bar (expense)
            canvas.drawRect(xBase - barWidth, height - padding - hIn, xBase, height - padding, paintIn);

            // Draw OUT bar (revenue)
            canvas.drawRect(xBase, height - padding - hOut, xBase + barWidth, height - padding, paintOut);
            
            // Optional: Draw date label
            String shortDate = dates.get(i).substring(Math.max(0, dates.get(i).length() - 5));
            canvas.drawText(shortDate, xBase - 20, height - 10, paintText);
        }
    }
}
