package com.example.sudokuapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sudokuapp.R;

public class WinActivity extends AppCompatActivity {

    Button returnToMenuButton;
    Button newGameButton;
    TextView timerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        Intent intent = getIntent();
        String timerValueString = intent.getStringExtra("EXTRA_CURRENT_GAME_TIME");

        timerValue = (TextView) findViewById(R.id.timeValue);
        timerValue.setText(timerValueString);

        returnToMenuButton = (Button) findViewById(R.id.returnToMenuButton);
        returnToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToMenu();
            }
        });

        newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewGame();
            }
        });
    }

    private void returnToMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void openNewGame() {
        Intent intent = new Intent(this, ChooseDifficultyLevelActivity.class);
        finish();
        startActivity(intent);
    }
}