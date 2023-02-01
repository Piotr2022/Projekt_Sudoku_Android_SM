package com.example.sudokuapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sudokuapp.R;
import com.example.sudokuapp.entity.Tile;
import com.example.sudokuapp.view.custom.SudokuBoardView;
import com.example.sudokuapp.viewmodel.PlaySudokuViewModel;

import java.util.Set;

public class GameActivity extends AppCompatActivity implements SudokuBoardView.OnTouchListener {

    private PlaySudokuViewModel sudokuViewModel;
    private Button[] buttons;
    private ImageButton noteButton;
    private ImageButton deleteButton;
    private TextView timer;
    private long startTime = 0;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        int difficultyLevel = intent.getIntExtra("EXTRA_DIFFICULTY_LEVEL",1);

        ((SudokuBoardView)findViewById(R.id.sudokuBoardView)).registerListener(this);

        sudokuViewModel = ViewModelProviders.of(this).get(PlaySudokuViewModel.class);

        sudokuViewModel.setGame(difficultyLevel,9);

        ((SudokuBoardView)findViewById(R.id.sudokuBoardView)).updateTiles(sudokuViewModel.sudokuGame.getBoard().tiles);


        sudokuViewModel.sudokuGame.selectedTileLiveData.observe(this, new Observer<Pair<Integer,Integer>>(){
            @Override
            public void onChanged(Pair<Integer,Integer> tile){
                updateSelectedTile(tile);
            }
        });
        sudokuViewModel.sudokuGame.tilesLiveData.observe(this, new Observer<Tile[][]>(){
            @Override
            public void onChanged(Tile[][] tiles) {
                updateTiles(tiles);
            }
        });
        sudokuViewModel.sudokuGame.isTakingNotesLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isTakingNotes) {
                updateNoteTaking(isTakingNotes);
            }
        });
        sudokuViewModel.sudokuGame.highLightedKeysLiveData.observe(this, new Observer<Set<Integer>>() {
            @Override
            public void onChanged(Set<Integer> notes) {
                updateHighLightedKeys(notes);
            }
        });
        sudokuViewModel.sudokuGame.didWonLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                openWinActivity();
            }
        });
        sudokuViewModel.sudokuGame.mistakesLiveData.observe(this, new Observer<Set<Pair<Integer, Integer>>>() {
            @Override
            public void onChanged(Set<Pair<Integer, Integer>> mistakes) {
                updateMistakenTiles(mistakes);
            }
        });

        timer = (TextView) findViewById(R.id.timerValueText);

        buttons = new Button[]{findViewById(R.id.oneButton),findViewById(R.id.twoButton),findViewById(R.id.threeButton),
            findViewById(R.id.fourButton),findViewById(R.id.fiveButton),findViewById(R.id.sixButton),
            findViewById(R.id.sevenButton),findViewById(R.id.eightButton),findViewById(R.id.nineButton)};

        for(int i=0;i<buttons.length;i++){
            int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sudokuViewModel.sudokuGame.handleInput(finalI + 1);
                }
            });
            buttons[i].setBackgroundColor(Color.LTGRAY);
            buttons[i].setTextColor(Color.BLACK);
        }

        noteButton = findViewById(R.id.noteButton);
        noteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sudokuViewModel.sudokuGame.changeIsTakingNotes();
            }
        });

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sudokuViewModel.sudokuGame.delete();
            }
        });

        runGameTimer();

    }


    private void openWinActivity(){

        Intent intent = new Intent(this,WinActivity.class);
        intent.putExtra("EXTRA_CURRENT_GAME_TIME", timer.getText());
        finish();
        startActivity(intent);
    }


    private void runGameTimer(){
        handler= new Handler();
        startTime = 0;

        Runnable run = new Runnable() {
            Integer hours;
            Integer minutes;
            Integer seconds;
            String text;
            @Override
            public void run() {
                startTime++;
                hours = (int)startTime/3600;
                minutes = (int) (startTime%3600)/60;
                seconds = (int) startTime%60;
                text="";
                if(hours>0){
                    text+= hours.toString()+":";
                }
                if(minutes<10){
                    text+= "0"+minutes.toString();
                } else {
                    text+=minutes.toString();
                }
                if(seconds<10){
                    text+= ":0"+seconds.toString();
                } else {
                    text+=":"+seconds.toString();
                }
                //Log.d("TIMER","WORKING");
                timer.setText(text);
                handler.postDelayed(this,1000);
            }
        };
        handler.post(run);
    }

    private void updateHighLightedKeys(Set<Integer> notes) {
        if(notes!=null) {
            int color;
            for (int i=0;i<buttons.length;i++){
              if(notes.contains(i+1)){
                  color = ContextCompat.getColor(this,R.color.pasek_aplikacji);
              } else {
                  color = Color.LTGRAY;
              }
                buttons[i].setBackgroundColor(color);

            }
        }
    }

    private void updateNoteTaking(Boolean isTakingNotes) {
        if(isTakingNotes!=null){
            int color;
            if(isTakingNotes){
                color = ContextCompat.getColor(this, R.color.pasek_aplikacji);
            } else {
                color = Color.LTGRAY;
            }
            noteButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void updateTiles(Tile[][] tiles){
        if(tiles!=null){
            ((SudokuBoardView)findViewById(R.id.sudokuBoardView)).updateTiles(tiles);
        }
    }

    private void updateSelectedTile(Pair<Integer,Integer> tile){
        if(tile.first!=null && tile.second!=null){
            ((SudokuBoardView)findViewById(R.id.sudokuBoardView)).updateSelectedTile(tile.first,tile.second);
        }
    }

    private void updateMistakenTiles(Set<Pair<Integer,Integer>> mistakes){
        if(mistakes!=null){
            ((SudokuBoardView)findViewById(R.id.sudokuBoardView)).updateMistakenTile(mistakes);
        }
    }

    @Override
    public void onTileTouched(int row, int col) {
        sudokuViewModel.sudokuGame.updateSelectedTile(row,col);
    }
}