package com.example.sudokuapp.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BoardAndTile {
    @Embedded public Board board;
    @Relation(
            parentColumn = "boardId",
            entityColumn = "boardId"
    )
    public Tile tiles;
}

