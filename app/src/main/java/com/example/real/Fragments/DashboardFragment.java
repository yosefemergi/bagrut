package com.example.real.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.real.Adapters.ExpenseAdapter;
import com.example.real.Adapters.IncomeAdapter;
import com.example.real.CustomMarkerView;
import com.example.real.DataBase.AppDatabase;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.real.DataBase.Expense;
import com.example.real.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private TextView textViewBalance;
    private RecyclerView recyclerView;
    private AppDatabase db;
    private RecyclerView recyclerViewIncomes;
    private BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the TextView and RecyclerView
        textViewBalance = view.findViewById(R.id.textViewBalance);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewIncomes = view.findViewById(R.id.recyclerViewIncomes);
        barChart = view.findViewById(R.id.bar_chart);


        // Set up RecyclerView with GridLayoutManager for 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Add DividerItemDecoration for spacing between items
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Set up RecyclerView with LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewIncomes.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize Room Database
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "expenses-db")
                .fallbackToDestructiveMigration()
                .build();

        if (barChart != null) {

        } else {
            Log.e("DashboardFragment", "BarChart not found in layout");
        }

        setupBarChart();
        loadBarChartData();
        // Load and display the balance amount
        loadBalance();
        // Load and display expenses in RecyclerView
        loadExpenses();
        loadIncomes();
    }

    private void loadBalance() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        // קריאה ל-balance_amount כ-String והמרה ל-Float
        String balanceString = sharedPreferences.getString("balance_amount", "0");
        float currentBalance = Float.parseFloat(balanceString);

        // הצגת היתרה בטקסט של ה-TextView
        TextView balanceTextView = getView().findViewById(R.id.textViewBalance);
        if (balanceTextView != null) {
            balanceTextView.setText("₪" + currentBalance);
        }
    }
    private void loadIncomes() {
        new Thread(() -> {
            List<Expense> incomes = db.expenseDao().getAllIncomes(); // שליפת הכנסות בלבד
            if (incomes != null && !incomes.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    if (recyclerViewIncomes.getAdapter() == null) {
                        IncomeAdapter adapter = new IncomeAdapter(incomes);
                        recyclerViewIncomes.setAdapter(adapter);
                    } else {
                        ((ExpenseAdapter) recyclerViewIncomes.getAdapter()).setExpenses(incomes);
                    }
                });
            } else {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "No incomes found", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    private void loadExpenses() {
        // Load expenses from the database in a separate thread
        new Thread(() -> {
            List<Expense> expenses = db.expenseDao().getAllExpensesOnly();
            if (expenses != null && !expenses.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    // בדוק אם כבר יש מתאם או צור מתאם חדש אם אין
                    if (recyclerView.getAdapter() == null) {
                        ExpenseAdapter adapter = new ExpenseAdapter(expenses);
                        recyclerView.setAdapter(adapter);
                    } else {
                        // עדכן את הרשימה באמצעות הפונקציה החדשה setExpenses
                        ((ExpenseAdapter) recyclerView.getAdapter()).setExpenses(expenses);
                    }
                });
            } else {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "No expenses found", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setBackgroundColor(Color.BLACK);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE); // צבע טקסט בציר X
        xAxis.setTypeface(Typeface.DEFAULT_BOLD); // הפיכת הטקסט ל-bold בציר X
        xAxis.setTextSize(14f); // גודל טקסט בציר X
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // מקבל את התאריך בתבנית dd/MM מהשבוע האחרון
                int index = (int) value;
                List<String> lastWeekDays = getLastWeekDays();
                if (index >= 0 && index < lastWeekDays.size()) {
                    return lastWeekDays.get(index);
                } else {
                    return ""; // במקרה של ערך לא תקין
                }
            }
        });

        // ציר Y - הגבלת טווח עליון ותחתון
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(-5000f); // הגבלת מינימום על ציר ה-Y
        leftAxis.setAxisMaximum(5000f);  // הגבלת מקסימום על ציר ה-Y
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTypeface(Typeface.DEFAULT_BOLD);
        leftAxis.setTextSize(14f);

        // הסתרת ציר ה-Y בצד ימין
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // הגדרת MarkerView מותאם אישית להצגת נתונים על עמודות הגרף
        CustomMarkerView markerView = new CustomMarkerView(getContext(), R.layout.tvcontent, getLastWeekDays());
        barChart.setMarker(markerView);

        // התאמה אוטומטית של הטווחים לציר Y במידה ויש ערכים חורגים
        barChart.setAutoScaleMinMaxEnabled(true);

        // רענון הגרף
        barChart.invalidate();
    }

    // Helper method to get dates in "dd/MM" format
    private List<String> getLastWeekDaysWithoutYear() {
        List<String> days = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 6; i >= 0; i--) {
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            days.add(dateFormat.format(calendar.getTime()));
            calendar = Calendar.getInstance(); // Reset calendar to the current date for each loop
        }
        return days;
    }
    private void loadBarChartData() {
        new Thread(() -> {
            List<BarEntry> incomeEntries = new ArrayList<>();
            List<BarEntry> expenseEntries = new ArrayList<>();
            List<String> lastWeekDays = getLastWeekDays(); // קבלת רשימת תאריכים של השבוע האחרון

            for (int i = 0; i < lastWeekDays.size(); i++) {
                String date = lastWeekDays.get(i); // התאריך הספציפי של היום
                float income = getIncomeForDay(date); // קבלת ההכנסה ליום הזה
                float expense = getExpenseForDay(date); // קבלת ההוצאה ליום הזה

                Log.d("DashboardFragment", "Date: " + date + ", Income: " + income + ", Expense: " + expense);

                if (income != 0) incomeEntries.add(new BarEntry(i, income));
                if (expense != 0) expenseEntries.add(new BarEntry(i, -expense)); // הפיכת ההוצאה לערך שלילי כדי להציג מתחת לציר ה-X
            }

            getActivity().runOnUiThread(() -> {
                if (barChart != null) {
                    BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "Incomes");
                    incomeDataSet.setColor(Color.parseColor("#4CAF50"));
                    incomeDataSet.setValueTextColor(Color.WHITE);
                    incomeDataSet.setValueTextSize(12f);
                    incomeDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

                    BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "Expenses");
                    expenseDataSet.setColor(Color.parseColor("#A04949"));
                    expenseDataSet.setValueTextColor(Color.WHITE);
                    expenseDataSet.setValueTextSize(12f);
                    expenseDataSet.setValueTypeface(Typeface.DEFAULT_BOLD);


                    BarData barData = new BarData(incomeDataSet, expenseDataSet);
                    barChart.setData(barData);
                    barChart.invalidate(); // רענון הגרף
                } else {
                    Log.e("DashboardFragment", "BarChart is null in loadBarChartData");
                }
            });
        }).start();
    }

    private List<String> getLastWeekDays() {
        List<String> days = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            days.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        return days;
    }


    private float getIncomeForDay(String date) {
        Float income = db.expenseDao().getTotalIncomeForDate(date);
        return income != null ? income : 0;
    }

    private float getExpenseForDay(String date) {
        Float expense = db.expenseDao().getTotalExpenseForDate(date);
        return expense != null ? expense : 0;
    }
}
