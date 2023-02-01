package com.example.sudokuapp.entity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SudokuGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertSudokuGame(SudokuGame sudokuGame);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertBoard(Board board);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTile(Tile tile);

    @Update
    public void updateSudokuGame(SudokuGame sudokuGame);

    @Update
    public void updateBoard(Board board);

    @Update
    public void updateTile(Tile tile);

    @Delete
    public void deleteSudokuGame(SudokuGame sudokuGame);

    @Delete
    public void deleteBoard(Board board);

    @Delete
    public void deleteTile(Tile tile);

    @Transaction
    @Query("SELECT * FROM games LIMIT 1")
    public SudokuGame getOneSudokuGame();

    @Transaction
    @Query("SELECT COUNT(gameId) FROM games")
    public int getSudokuGamesLenght();

    @Transaction
    @Query("SELECT * FROM games where gameId=:gameId")
    public SudokuGame getSudokuGame(int gameId);

    @Transaction
    @Query("SELECT * FROM boards where boardId=:boardId")
    public Board getBoard(int boardId);

    @Transaction
    @Query("SELECT * FROM tiles where tileId=:tileId")
    public Tile getTile(int tileId);

    @Transaction
    @Query("SELECT * FROM tiles where boardId=:boardId")
    public LiveData<List<Tile>> getTilesForBoard(int boardId);


}
