package com.elm.boycothelper.activites;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.elm.boycothelper.R;
import com.elm.boycothelper.pojo.Constants;
import com.elm.boycothelper.pojo.LanguageUtils;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                if (selectedRadioButton != null) {
                    String selectedLanguage = selectedRadioButton.getText().toString();
                    if (selectedLanguage.equals("عربي")){
                        LanguageUtils.changeAppLanguage(SettingsActivity.this, Constants.LANGUAGE_ARABIC);

                    }else{
                        LanguageUtils.changeAppLanguage(SettingsActivity.this, Constants.LANGUAGE_ENGLISH);

                    }
                }

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



    }

}