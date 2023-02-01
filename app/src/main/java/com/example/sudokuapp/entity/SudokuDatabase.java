package com.example.sudokuapp.entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SudokuGame.class, Board.class, Tile.class}, version = 1)
public abstract class SudokuDatabase extends RoomDatabase {
    public abstract SudokuGameDao sudokuGameDao();

    public static SudokuDatabase INSTANCE;

    public static SudokuDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (SudokuDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),SudokuDatabase.class,"sudoku-database").fallbackToDestructiveMigration().build();
            }
        }
        return INSTANCE;
    }
}
