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

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private List<Expense> incomes;

    public IncomeAdapter(List<Expense> incomes) {
        this.incomes = incomes;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Expense income = incomes.get(position);
        holder.categoryTextViewIncome.setText(income.category);
        holder.amountTextViewIncome.setText("+₪" + income.amount);
        holder.dateTextViewIncome.setText(income.date);
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }

    public void setIncomes(List<Expense> incomes) {
        this.incomes = incomes;
        notifyDataSetChanged(); // לעדכן את ה-RecycleView לאחר שינוי הנתונים
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextViewIncome, amountTextViewIncome, dateTextViewIncome;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextViewIncome = itemView.findViewById(R.id.categoryTextViewIncome);
            amountTextViewIncome = itemView.findViewById(R.id.amountTextViewIncome);
            dateTextViewIncome = itemView.findViewById(R.id.dateTextViewIncome);
        }
    }
}
