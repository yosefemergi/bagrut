package com.example.real;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BlankFragment extends Fragment {


    private EditText editTextAmount;
    private Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize EditText and Button
        editTextAmount = view.findViewById(R.id.editTextAmount);
        btnSubmit = view.findViewById(R.id.btn_submit);

        // Set click listener for the submit button
        btnSubmit.setOnClickListener(v -> {
            String amount = editTextAmount.getText().toString();
            if (!amount.isEmpty()) {
                saveAmount(amount);
                Toast.makeText(getActivity(), "Amount saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please enter an amount", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //    In this line, we create a SharedPreferences object using getSharedPreferences and request access to a storage location
//        called "MyAppPrefs". Context.MODE_PRIVATE means that the storage is accessible only to the current application.
    private void saveAmount(String amount) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("balance_amount", amount);
        editor.apply();

    }
}