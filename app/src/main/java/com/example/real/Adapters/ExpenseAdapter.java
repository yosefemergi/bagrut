package com.example.real.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.real.DataBase.Expense;
import com.example.real.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.categoryTextView.setText(expense.category);
        String amountText = expense.isIncome ? "+₪" + expense.amount : "-₪" + expense.amount ;
        holder.amountTextView.setText("-₪" + expense.amount);
        holder.dateTextView.setText(expense.date);

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged(); // לעדכן את ה-RecycleView לאחר שינוי הנתונים
    }
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView, amountTextView, dateTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

        }
    }
}

