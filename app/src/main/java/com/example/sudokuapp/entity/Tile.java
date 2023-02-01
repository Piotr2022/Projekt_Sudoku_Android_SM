package com.example.sudokuapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Set;

@Entity(tableName = "tiles")
public class Tile {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tileId")
    public int tileId;
    @ColumnInfo(name = "boardId")
    public int boardId;
    @ColumnInfo(name = "row")
    public int row;
    @ColumnInfo(name = "col")
    public int col;
    @ColumnInfo(name = "value")
    public Integer value;
    @ColumnInfo(name = "isStartingTile")
    public boolean isStartingTile;
    @Ignore
    public Set<Integer> notes = new HashSet<Integer>();

    @Ignore
    public Tile(int row, int col, int value){
        this.row = row;
        this.col = col;
        this.value = value;
        this.isStartingTile = false;
    }

    public Tile(int row, int col, int value, boolean isStartingTile){
        this.row = row;
        this.col = col;
        this.value = value;
        this.isStartingTile = isStartingTile;
    }

    public Tile(int boardId, int row, int col, int value, boolean isStartingTile, Set<Integer> notes){
        this.boardId = boardId;
        this.row = row;
        this.col = col;
        this.value = value;
        this.isStartingTile = isStartingTile;
        this.notes = notes;
    }

}
