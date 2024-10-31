package com.example.real.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Dao
public interface ExpenseDao {

    // פונקציה להוספת הוצאה חדשה למסד הנתונים
    @Insert
    void insertExpense(Expense expense);

    // פונקציה לשליפת כל ההוצאות, בסדר יורד לפי id
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    List<Expense> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE isIncome = 1 ORDER BY id DESC")
    List<Expense> getAllIncomes();

    @Query("SELECT * FROM expenses WHERE isIncome = 0 ORDER BY id DESC")
    List<Expense> getAllExpensesOnly();


    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = 1 AND date = :date")
    Float getTotalIncomeForDate(String date);

    // סיכום ההוצאות לפי תאריך מסוים
    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = 0 AND date = :date")
    Float getTotalExpenseForDate(String date);

    default String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
}
}

