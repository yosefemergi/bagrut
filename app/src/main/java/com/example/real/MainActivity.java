package com.example.real;

import android.os.Bundle;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//            PieChart pieChart = findViewById(R.id.line_chart);
//
//            ArrayList<PieEntry> entries = new ArrayList<>();
//            entries.add(new PieEntry(55f, "Category 1"));
//            entries.add(new PieEntry(99f, "Category 2"));
//            entries.add(new PieEntry(25f, "Category 3"));
//
//            PieDataSet piedataset = new PieDataSet(entries, "Expense Categories");
//            piedataset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//            PieData piedata = new PieData(piedataset);
//            pieChart.setData(piedata);
//
//            pieChart.getDescription().setEnabled(false);
//            pieChart.animateY(1000);
//            pieChart.invalidate();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.navigation_planning) {
                selectedFragment = new PlanningFragment();
            } else if (itemId == R.id.navigation_add) {
                selectedFragment = new AddFragment();
            } else if (itemId == R.id.navigation_statistics) {
                selectedFragment = new StatisticsFragment();
            } else if (itemId == R.id.navigation_more) {
                selectedFragment = new MoreFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        btn1 =findViewById(R.id.btnn);
        btn1.setOnClickListener(v -> {
            loadFragment(new BlankFragment());
        });


    }
        public void loadFragment (Fragment fragment){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.FrameLayout, fragment);
            ft.commit();
        }



}