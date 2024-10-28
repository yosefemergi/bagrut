package com.example.real.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    // פונקציה להוספת הוצאה חדשה למסד הנתונים
    @Insert
    void insertExpense(Expense expense);

    // פונקציה לשליפת כל ההוצאות, בסדר יורד לפי id
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    List<Expense> getAllExpenses();
}

