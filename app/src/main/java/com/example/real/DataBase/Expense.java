package com.example.real.DataBase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses") // הגדרת הטבלה עם @Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;  // מזהה ייחודי לכל רשומה

    public String category;  // קטגוריה של ההוצאה/הכנסה
    public String amount;    // סכום ההוצאה/הכנסה
    public String date;      // תאריך ההוצאה/הכנסה
    public String time;      // שעת ההוצאה/הכנסה
    public boolean isIncome; // משתנה בוליאני שמסמן אם זו הכנסה (true) או הוצאה (false)

    // קונסטרקטור
    public Expense(String category, String amount, String date, String time, boolean isIncome) {
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.isIncome = isIncome;
    }
}
