package com.example.dreamleague.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dreamleague.R;

public class CreateTeamActivity extends AppCompatActivity {

    Intent intent;
    Button button;
    EditText teamName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        setupVars();
    }

    void setupVars(){
        teamName = findViewById(R.id.et_teamName);
        button = findViewById(R.id.btn_continueTeamCreate);
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



}