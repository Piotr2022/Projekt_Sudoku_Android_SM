package com.example.sudokuapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sudokuapp.R;

public class ChooseDifficultyLevelActivity extends AppCompatActivity {

    private Button easyButton;
    private Button normalButton;
    private Button hardButton;
    public static final String EXTRA_DIFFICULTY_LEVEL = "com.example.sudokuapp.view.EXTRA_DIFFICULTY_LEVEL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty_level);
        easyButton = findViewById(R.id.easyButton);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDifficultyLevelForGame(0);
            }
        });

        normalButton = findViewById(R.id.normalButton);
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDifficultyLevelForGame(1);
            }
        });

        hardButton = findViewById(R.id.hardButton);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDifficultyLevelForGame(2);
            }
        });
    }

    private void setDifficultyLevelForGame(int difficultyLevel){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("EXTRA_DIFFICULTY_LEVEL", difficultyLevel);
        finish();
        startActivity(intent);
    }

}