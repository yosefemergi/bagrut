package com.example.real.DataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses") // הגדרת הטבלה עם @Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;  // מזהה ייחודי לכל רשומה

    public String category;  // קטגוריה של ההוצאה
    public String amount;    // סכום ההוצאה
    public String date;      // תאריך ההוצאה
    public String time;      // שעת ההוצאה

    // קונסטרקטור
    public Expense(String category, String amount, String date, String time) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }
}
