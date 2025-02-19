package com.example.real.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.real.DataBase.Expense;
import com.example.real.DataBase.ExpenseDao;

@Database(entities = {Expense.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExpenseDao expenseDao();
}
