package com.example.real;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.real.DataBase.Expense;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFragment extends Fragment {

    private TextView categoryTextView;
    private TextView dateTimeTextView;
    private Calendar selectedDateTime;
    private TextView amountEditText;
    private Button saveButton;
    private com.example.real.DataBase.AppDatabase db;
    private RadioGroup typeRadioGroup;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // הגדרת פורמט תאריך אחיד

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
        selectedDateTime = Calendar.getInstance();
        amountEditText = view.findViewById(R.id.amountEditText);
        saveButton = view.findViewById(R.id.saveButton);

        // אתחול מסד הנתונים
        db = Room.databaseBuilder(requireContext(), com.example.real.DataBase.AppDatabase.class, "expenses-db")
                .fallbackToDestructiveMigration()
                .build();

        saveButton.setOnClickListener(v -> saveExpense());

        dateTimeTextView.setOnClickListener(v -> showDatePickerDialog());

        String[] categories = {"Food", "Transport", "Shopping", "Health", "Entertainment", "Other"};
        categoryTextView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog);
            builder.setTitle("Choose a Category")
                    .setItems(categories, (dialog, which) -> {
                        String selectedCategory = categories[which];
                        categoryTextView.setText(selectedCategory);
                    });
            builder.create().show();
        });

        return view;
    }

    private void showDatePickerDialog() {
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            selectedDateTime.set(Calendar.YEAR, selectedYear);
            selectedDateTime.set(Calendar.MONTH, selectedMonth);
            selectedDateTime.set(Calendar.DAY_OF_MONTH, selectedDay);

            showTimePickerDialog(); // לאחר בחירת התאריך, פתח את ה-TimePickerDialog

        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, selectedHour, selectedMinute) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            selectedDateTime.set(Calendar.MINUTE, selectedMinute);

            updateDateTimeTextView(); // עדכון התאריך והשעה שנבחרו ב-TextView

        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void updateDateTimeTextView() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateTimeString = dateTimeFormat.format(selectedDateTime.getTime());
        dateTimeTextView.setText(dateTimeString);
    }

    private void saveExpense() {
        String category = categoryTextView.getText().toString();
        String amountString = amountEditText.getText().toString();
        String date = dateFormat.format(selectedDateTime.getTime());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedDateTime.getTime());

        int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
        boolean isIncome = selectedTypeId == R.id.incomeRadioButton;

        if (category.isEmpty() || amountString.isEmpty() || date.isEmpty() || selectedTypeId == -1) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float expenseAmount = Float.parseFloat(amountString); // בדוק שהסכום ניתן להמרה למספר
            Expense expense = new Expense(category, amountString, date, time, isIncome);
            Log.d("AddFragment", "Saving expense with date: " + date);

            new Thread(() -> {
                db.expenseDao().insertExpense(expense);

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String currentBalanceString = sharedPreferences.getString("balance_amount", "0");
                float currentBalance = Float.parseFloat(currentBalanceString);
                float newBalance = isIncome ? (currentBalance + expenseAmount) : (currentBalance - expenseAmount);

                editor.putString("balance_amount", String.valueOf(newBalance));
                editor.apply();

                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Expense saved", Toast.LENGTH_SHORT).show();
                    categoryTextView.setText("Category");
                    amountEditText.setText("");
                    dateTimeTextView.setText("Date & Time");
                    typeRadioGroup.clearCheck();

                    TextView balanceTextView = getActivity().findViewById(R.id.textViewBalance);
                    if (balanceTextView != null) {
                        balanceTextView.setText("₪" + newBalance);
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
        }
    }
}
