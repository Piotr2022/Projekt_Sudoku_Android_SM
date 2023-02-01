package com.example.sudokuapp.entity;
import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SudokuRepository {

    SudokuGameDao sudokuGameDao;

    public SudokuRepository(Application application){
        SudokuDatabase db = SudokuDatabase.getDatabase(application);
        sudokuGameDao = db.sudokuGameDao();
    }

    public SudokuGame getOneSudokuGame(){
        SudokuGame sudokuGame = sudokuGameDao.getOneSudokuGame();
        Board board = getBoard(sudokuGame.boardId);
        sudokuGame.board = board;
        return sudokuGame;
    }

    public SudokuGame getSudokuGame(int gameID){
        SudokuGame sudokuGame = sudokuGameDao.getSudokuGame(gameID);
        Board board = getBoard(sudokuGame.boardId);
        sudokuGame.board = board;
        return sudokuGame;
    }

    public Board getBoard(int boardId){
        Board board = sudokuGameDao.getBoard(boardId);
        Tile[][] tiles = getTilesForBoard(boardId);
        board.tiles = tiles;
        return board;
    }


    public Tile[][] getTilesForBoard(int boardId){
        int size = sudokuGameDao.getBoard(boardId).size;
        Tile[][] tiles = new Tile[size][size];
        LiveData<List<Tile>> tilesList = sudokuGameDao.getTilesForBoard(boardId);
        for (Tile tile: tilesList.getValue()) {
            tiles[tile.row][tile.col]= tile;
        }
        return tiles;
    }

    public int getSudokuGamesLenght(){
        return sudokuGameDao.getSudokuGamesLenght();
    }

    public void insertSudokuGame(SudokuGame sudokuGame){
        sudokuGameDao.insertSudokuGame(sudokuGame);
        insertBoard(sudokuGame.getBoard());
    }

    public void insertBoard(Board board){
        sudokuGameDao.insertBoard(board);
        for(int row=0;row<board.size;row++){
            for(int col=0;col<board.size;col++){
                insertTile(board.tiles[row][col]);
            }
        }
    }

    public void insertTile(Tile tile){
        sudokuGameDao.insertTile(tile);
    }

    public void updateSudokuGame(SudokuGame sudokuGame){
        updateBoard(sudokuGame.getBoard());
        sudokuGameDao.deleteSudokuGame(sudokuGame);
    }

    public void updateBoard(Board board){
        LiveData<List<Tile>> tiles = sudokuGameDao.getTilesForBoard(board.boardId);
        for (Tile tile: tiles.getValue()) {
            updateTile(tile);
        }
        sudokuGameDao.updateBoard(board);
    }

    public void updateTile(Tile tile){
        sudokuGameDao.updateTile(tile);
    }

    public void deleteSudokuGame(SudokuGame sudokuGame){
        deleteBoard(sudokuGame.getBoard());
        sudokuGameDao.deleteSudokuGame(sudokuGame);
    }

    public void deleteBoard(Board board){
        LiveData<List<Tile>> tiles = sudokuGameDao.getTilesForBoard(board.boardId);
        for (Tile tile: tiles.getValue()) {
            deleteTile(tile);
        }
        sudokuGameDao.deleteBoard(board);
    }

    public void deleteTile(Tile tile){
        sudokuGameDao.deleteTile(tile);
    }




}
