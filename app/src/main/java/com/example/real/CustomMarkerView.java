package com.example.real;

import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import java.util.List;

public class CustomMarkerView extends MarkerView {

    private final TextView textView;
    private final List<String> dateList;

    public CustomMarkerView(Context context, int layoutResource, List<String> dateList) {
        super(context, layoutResource);
        this.dateList = dateList;
        textView = findViewById(R.id.tvContent); // ודא שזה ה-id הנכון של ה-TextView ב-marker_view.xml
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();
        String date = (index >= 0 && index < dateList.size()) ? dateList.get(index) : "N/A";
        String amount = "₪" + Math.abs(e.getY());
        textView.setText(amount + " - " + date);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
