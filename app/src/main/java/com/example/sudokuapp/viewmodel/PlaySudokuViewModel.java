package com.example.sudokuapp.viewmodel;

import android.app.Application;
import android.media.AsyncPlayer;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.sudokuapp.entity.SudokuGame;
import com.example.sudokuapp.entity.SudokuRepository;

import java.io.Closeable;


public class PlaySudokuViewModel extends AndroidViewModel {
    //public SudokuGame sudokuGame = new SudokuGame(1);
    public SudokuRepository sudokuRepository;
    public SudokuGame sudokuGame = null;

    public PlaySudokuViewModel(Application application) {
        super(application);
        sudokuRepository = new SudokuRepository(application);
    }


    public void setGame(int difficultyLevel, int size){
        Log.d("PRZED","PRZED");
        this.sudokuGame = new SudokuGame(difficultyLevel,size);
        Log.d("PO","PO");
    }

    public void setGame(SudokuGame game)
    {
        this.sudokuGame = game;
    }

}


