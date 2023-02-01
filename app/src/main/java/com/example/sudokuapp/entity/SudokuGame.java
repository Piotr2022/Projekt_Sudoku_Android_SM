package com.example.sudokuapp.entity;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity(tableName = "games")
public class SudokuGame {
    @Ignore
    public MutableLiveData<Pair<Integer, Integer>> selectedTileLiveData = new MutableLiveData<Pair<Integer, Integer>>();
    @Ignore
    public MutableLiveData<Tile[][]> tilesLiveData = new MutableLiveData<Tile[][]>();
    @Ignore
    public MutableLiveData<Boolean> isTakingNotesLiveData = new MutableLiveData<Boolean>();
    @Ignore
    public MutableLiveData<Set<Integer>> highLightedKeysLiveData = new MutableLiveData<Set<Integer>>();
    @Ignore
    public MutableLiveData<Boolean> didWonLiveData = new MutableLiveData<Boolean>();
    @Ignore
    public MutableLiveData<Set<Pair<Integer, Integer>>> mistakesLiveData = new MutableLiveData<Set<Pair<Integer, Integer>>>();

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "gameId")
    public int id;

    @ColumnInfo(name = "selectedCol")
    public Integer selectedCol = -1;
    @ColumnInfo(name = "selectedRow")
    public Integer selectedRow = -1;
    @ColumnInfo(name = "boardId")
    public int boardId;
    @Ignore
    public Board board;
    @ColumnInfo(name = "isTakingNotes")
    public boolean isTakingNotes = false;
    @ColumnInfo(name = "didWon")
    public boolean didWon;
    @ColumnInfo(name = "filledTiles")
    public int filledTiles;
    @ColumnInfo(name = "size")
    public int size;
    @Ignore
    private int sqrtSize;
    @ColumnInfo(name = "difficultyLevel")
    public int difficultyLevel;

    @Ignore
    private Set<Pair<Integer,Integer>> mistakes;


    public SudokuGame(int difficultyLevel,int size) {


        this.difficultyLevel = difficultyLevel;
        this.size = size;
        this.sqrtSize = (int) Math.sqrt(size);
        this.didWon = false;
        generateBoard(difficultyLevel);
        tilesLiveData.postValue(board.tiles);
        mistakes = new HashSet<Pair<Integer,Integer>>();
        selectedTileLiveData.postValue(new Pair(selectedRow, selectedCol));
        isTakingNotesLiveData.postValue(isTakingNotes);
    }

    @Ignore
    public SudokuGame(Integer selectedRow, Integer selectedCol,Board board, boolean isTakingNotes, boolean didWon, int filledTiles, int size){
        this.selectedRow = selectedRow;
        this.selectedCol = selectedCol;
        this.board = board;
        this.isTakingNotes = isTakingNotes;
        this.didWon = didWon;
        this.filledTiles = filledTiles;
        this.size = size;
        this.sqrtSize = (int) Math.sqrt(size);
    }

    private void generateBoard(int difficultyLevel) {
        Random random = new Random();

        int notEmptyTilesCount = 0;
        switch (difficultyLevel) {
            case 0:
                notEmptyTilesCount = 41;
                break;
            case 1:
                notEmptyTilesCount = 28;
                break;
            case 2:
                notEmptyTilesCount = 18;
                break;
            default:
                notEmptyTilesCount = 28;
                break;
        }
        filledTiles = notEmptyTilesCount;

        Tile[][] tiles = new Tile[this.size][this.size];
        Set<Pair<Integer,Integer>> tilesPositionsSet = new HashSet<>();
        Board board;
        for (int rowIndex = 0; rowIndex < this.size; rowIndex++) {
            for (int colIndex = 0; colIndex < this.size; colIndex++) {
                tiles[rowIndex][colIndex] = new Tile(rowIndex, colIndex, 0,true);
            }
        }


        fillDiagonal(tiles,random);
        fillRemaining(0,this.sqrtSize,tiles);

        int randomRow, randomCol;
        for(int i=0; i<(81-notEmptyTilesCount);i++)
        {
            do {
                randomRow = random.nextInt(9);
                randomCol = random.nextInt(9);
            } while (tilesPositionsSet.contains(new Pair<Integer,Integer>(randomRow,randomCol)));
            tilesPositionsSet.add(new Pair<Integer,Integer>(randomRow,randomCol));

            tiles[randomRow][randomCol].value = 0;
            tiles[randomRow][randomCol].isStartingTile = false;
        }
        this.board = new Board(9, tiles);
    }

    private void fillDiagonal(Tile[][] tiles, Random random){
        for(int i=0;i<this.size;i+=this.sqrtSize){
            fillBox(i,i,tiles,random);
        }
    }

    private void fillBox(int row, int col, Tile[][] tiles,Random random){
        int randomValue;
        for(int i=0;i<this.sqrtSize;i++){
            for(int j=0;j<this.sqrtSize;j++){
                do {
                    randomValue = random.nextInt(9) + 1;
                }while(!wasntUsedInBox(row,col,randomValue,tiles));
                tiles[row+i][col+j].value = randomValue;

            }
        }
    }


    private boolean wasntUsedInBox(int startRow, int startCol, int value, Tile[][] tiles){
        for (int i = 0; i<this.sqrtSize; i++)
        {
            for (int j = 0; j<this.sqrtSize; j++)
            {
                if (tiles[startRow+i][startCol+j].value==value && (startRow+i)!=selectedRow && (startCol+j)!=selectedCol)
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean wasntUsedInRow(int chosenRow,int value, Tile[][] tiles)
    {
        for (int col=0;col<9;col++)
        {
            if (tiles[chosenRow][col].value == value && col!=selectedCol){
                return false;
            }
        }
        return true;
    }

    private boolean wasntUsedInCol(int chosenCol,int value, Tile[][] tiles)
    {
        for (int row=0;row<this.size;row++)
        {
            if (tiles[row][chosenCol].value == value && row!=selectedRow)
            {
                return false;
            }
        }

        return true;
    }

    boolean fillRemaining(int i, int j, Tile[][] tiles)
    {
        if (j>=this.size && i<this.size-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=this.size && j>=this.size)
        {
            return true;
        }

        if (i < this.sqrtSize)
        {
            if (j < this.sqrtSize)
            {
                j = this.sqrtSize;
            }
        }
        else if (i < this.size-this.sqrtSize)
        {
            if (j==(int)(i/this.sqrtSize)*this.sqrtSize)
            {
                j =  j + 3;
            }
        }
        else
        {
            if (j == this.size-this.sqrtSize)
            {
                i = i + 1;
                j = 0;
                if (i>=this.size)
                {
                    return true;
                }
            }
        }

        for (int value = 1; value<=this.size; value++)
        {
            if (selectedPositionIsOk(i, j, value, tiles))
            {
                tiles[i][j].value = value;
                if (fillRemaining(i, j+1,tiles))
                {
                    return true;
                }
                tiles[i][j].value = 0;
            }
        }
        return false;
    }


    private boolean selectedPositionIsOk(int row, int col, int value, Tile[][] tiles)
    {
        return (wasntUsedInRow(row, value, tiles) && wasntUsedInCol(col, value, tiles) && wasntUsedInBox(row-row%3, col-col%3, value, tiles));
    }


    public void updateSelectedTile(Integer row, Integer col) {
        Tile tile = board.getTile(row, col);

        if (!tile.isStartingTile) {
            selectedRow = row;
            selectedCol = col;
            selectedTileLiveData.postValue(new Pair(row, col));

            if (isTakingNotes) {
                highLightedKeysLiveData.postValue(tile.notes);
            }
        }
    }

    public void changeIsTakingNotes() {
        isTakingNotes = !isTakingNotes;
        isTakingNotesLiveData.postValue(isTakingNotes);

        Set<Integer> notes;
        if (isTakingNotes && selectedRow != -1 && selectedCol != -1) {
            notes = board.getTile(selectedRow, selectedCol).notes;
        } else {
            notes = new HashSet<Integer>();
        }
        highLightedKeysLiveData.postValue(notes);
    }

    public void handleInput(int newValue) {

        if (selectedCol == -1 || selectedRow == -1 || board.getTile(selectedRow, selectedCol).isStartingTile) {
            return;
        } else {
            Tile tile = board.getTile(selectedRow, selectedCol);
            if (isTakingNotes) {
                if (tile.notes.contains(newValue)) {
                    tile.notes.remove(newValue);
                } else {
                    tile.notes.add(newValue);
                }
                highLightedKeysLiveData.postValue(tile.notes);
            } else {
                if (tile.value == 0) {
                    filledTiles++;
                }
                tile.value = newValue;
                if(!selectedPositionIsOk(selectedRow,selectedCol,newValue, board.tiles) && newValue != 0){
                    mistakes.add(new Pair<Integer,Integer>(selectedRow,selectedCol));
                    mistakesLiveData.postValue(mistakes);
                } else {
                    if(mistakes.contains(new Pair<Integer,Integer>(selectedRow,selectedCol))){
                        mistakes.remove(new Pair<Integer,Integer>(selectedRow,selectedCol));
                        mistakesLiveData.postValue(mistakes);
                    }
                }

                if(filledTiles==81 && mistakes.isEmpty()){
                    won();
                }
            }
            tilesLiveData.postValue(board.tiles);
        }
    }

    public void delete() {
        if (selectedCol != -1 && selectedRow != -1) {
            Tile tile = board.getTile(selectedRow, selectedCol);
            if (isTakingNotes) {
                tile.notes.clear();
                highLightedKeysLiveData.postValue(new HashSet<Integer>());
            } else {
                tile.value = 0;
                filledTiles--;
                Pair<Integer,Integer> maybeMistakenTile = new Pair<Integer,Integer>(selectedRow,selectedCol);
                if(mistakes.contains(maybeMistakenTile)){
                    mistakes.remove(maybeMistakenTile);
                    mistakesLiveData.postValue(mistakes);
                }
            }
            tilesLiveData.postValue(board.tiles);
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public void won() {
        didWon = true;
        didWonLiveData.postValue(didWon);
    }


}
