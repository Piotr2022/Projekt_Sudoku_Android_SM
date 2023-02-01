package com.example.sudokuapp.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

public class SudokuGameAndBoard {
    @Embedded public SudokuGame sudokuGame;
    @Relation(
            parentColumn = "boardId",
            entityColumn = "boardId"
    )
    public Board board;
}
