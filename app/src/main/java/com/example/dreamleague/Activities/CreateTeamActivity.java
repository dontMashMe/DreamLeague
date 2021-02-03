package com.example.dreamleague.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dreamleague.R;

import java.util.ArrayList;
import java.util.Locale;

public class CreateTeamActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    Intent intent;
    Button button;
    EditText teamName;
    Spinner spinner;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        setupVars();
        setupSpinner();
    }

    void setupVars(){
        teamName = findViewById(R.id.et_teamName);
        button = findViewById(R.id.btn_continueTeamCreate);
        spinner = findViewById(R.id.spinner_lang);
        button.setOnClickListener(v -> {
            if(!teamName.getText().toString().equals("")){
                intent = new Intent(v.getContext(), CreateTeamFragmentHolderActivity.class);
                intent.putExtra("TEAM_NAME", teamName.getText().toString());
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Niste unijeli ime vaše momčadi!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    void setupSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lang_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Locale current = getResources().getConfiguration().locale;

        if(current.getLanguage().equals("en")) {
            spinner.setSelection(0);
        }
        if(current.getLanguage().equals("hr")) {
            spinner.setSelection(1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setLocale(Integer pos) {
        ArrayList<String> strLocale = new ArrayList<String>();
        strLocale.add("en");
        strLocale.add("hr");
        strLocale.add("hu");

        setAppLocale(strLocale.get(pos));
        if (++check > 1)
        {
            finish();
            startActivity(getIntent());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setAppLocale (String localeCode)
    {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        setLocale(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}