package com.example.real;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

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
    private TextView  amountEditText;
    private Button saveButton;
    private com.example.real.DataBase.AppDatabase db;
    private RadioGroup typeRadioGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        typeRadioGroup = view.findViewById(R.id.typeRadioGroup);


        categoryTextView = view.findViewById(R.id.categoryTextView);
        dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
        selectedDateTime = Calendar.getInstance();
        categoryTextView = view.findViewById(R.id.categoryTextView);
        amountEditText = view.findViewById(R.id.amountEditText);
        dateTimeTextView = view.findViewById(R.id.dateTimeTextView);
        saveButton = view.findViewById(R.id.saveButton);

        // אתחול מסד הנתונים
        db = Room.databaseBuilder(requireContext(), com.example.real.DataBase.AppDatabase.class, "expenses-db")
                .fallbackToDestructiveMigration() // מאפשר מיגרציה הרסנית
                .build();
        saveButton.setOnClickListener(v -> saveExpense());
        // פתח את ה-Dialog לבחירת תאריך ושעה
        dateTimeTextView.setOnClickListener(v -> showDatePickerDialog());
        // רשימת הקטגוריות
        String[] categories = {"Food", "Transport", "Shopping", "Health", "Entertainment", "Other"};

        // הצגת ה-Dialog בעת לחיצה על קטגוריה
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

            // לאחר בחירת התאריך, פתח את ה-TimePickerDialog
            showTimePickerDialog();

        }, year, month, day);

        datePickerDialog.show();
    }
    private void showTimePickerDialog() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, selectedHour, selectedMinute) -> {
            selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            selectedDateTime.set(Calendar.MINUTE, selectedMinute);

            // עדכון התאריך והשעה שנבחרו ב-TextView
            updateDateTimeTextView();

        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void updateDateTimeTextView() {
        // פורמט תצוגת התאריך והשעה
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateTimeString = dateFormat.format(selectedDateTime.getTime());
        dateTimeTextView.setText(dateTimeString);
    }
    private void saveExpense() {
        String category = categoryTextView.getText().toString();
        String amount = amountEditText.getText().toString();
        String date = dateTimeTextView.getText().toString();
        String time = Calendar.getInstance().getTime().toString();  // כאן השתמשנו בתאריך הנוכחי (אפשר גם מתאריך שנבחר)



// ודא שהגדרת RadioGroup נכון קודם לכן
        int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();

        if (category.isEmpty() || amount.isEmpty() || date.isEmpty() || selectedTypeId == -1) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // יצירת אובייקט Expense ושמירתו במסד הנתונים
        Expense expense = new Expense(category, amount, date, time);

        new Thread(() -> {
            db.expenseDao().insertExpense(expense);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Expense saved", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }
}
