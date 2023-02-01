package com.example.sudokuapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "boards")
public class Board {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "boardId")
    public int boardId;
    @ColumnInfo(name = "gameId")
    public int gameId;
    @ColumnInfo(name = "size")
    public int size;
    @Ignore
    public Tile[][] tiles = null;

    public Board(int size){
        this.size = size;
    }

    @Ignore
    public Board(int size, Tile[][] tiles){
        this.size = size;
        this.tiles = tiles;
    }

    public Tile getTile(int row, int col){
        return tiles[row][col];
    }


}
