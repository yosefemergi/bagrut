package com.example.real;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.real.DataBase.AppDatabase;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.real.DataBase.Expense;

import java.util.List;

public class DashboardFragment extends Fragment {

    private TextView textViewBalance;
    private RecyclerView recyclerView;
    private AppDatabase db;

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
        // Set up RecyclerView with GridLayoutManager for 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Add DividerItemDecoration for spacing between items
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Set up RecyclerView with LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Room Database
        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "expenses-db")
                .fallbackToDestructiveMigration()
                .build();

        // Load and display the balance amount
        loadBalance();

        // Load and display expenses in RecyclerView
        loadExpenses();
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

    private void loadExpenses() {
        // Load expenses from the database in a separate thread
        new Thread(() -> {
            List<Expense> expenses = db.expenseDao().getAllExpenses();
            if (expenses != null && !expenses.isEmpty()) {
                getActivity().runOnUiThread(() -> {
                    // Set the adapter with the retrieved expenses
                    ExpenseAdapter adapter = new ExpenseAdapter(expenses);
                    recyclerView.setAdapter(adapter);
                });
            } else {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "No expenses found", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}
