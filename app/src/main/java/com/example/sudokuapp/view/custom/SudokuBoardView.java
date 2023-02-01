package com.example.sudokuapp.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;

import com.example.sudokuapp.entity.Tile;

import java.util.HashSet;
import java.util.Set;

public class SudokuBoardView extends View {

    private int sqrtSize = 3;
    private int size = 9;

    private float tileSizePixels = 0F;
    private float noteSizePixels = 0F;
    private float textFontSize = 0F;
    private float noteFontSize = 0F;
    private float startingTileTextFontSize = 0F;


    private int selectedCol = -1;
    private int selectedRow = -1;
    private SudokuBoardView.OnTouchListener listener = null;
    private Tile[][] tiles = null;
    private Set<Pair<Integer,Integer>> mistakenTiles;



    public SudokuBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        tiles = new Tile[9][9];
        mistakenTiles = new HashSet<Pair<Integer,Integer>>();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixels = Math.min(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(sizePixels,sizePixels);
    }


    // paints
    private Paint getThickLinePaint(){
        Paint thickLinePaint = new Paint();
        thickLinePaint.setStyle(Paint.Style.STROKE);
        thickLinePaint.setColor(Color.BLACK);
        thickLinePaint.setStrokeWidth(5F);
        return thickLinePaint;
    }

    private Paint getThinLinePaint(){
        Paint thinLinePaint = new Paint();
        thinLinePaint.setStyle(Paint.Style.STROKE);
        thinLinePaint.setColor(Color.BLACK);
        thinLinePaint.setStrokeWidth(2F);
        return thinLinePaint;
    }

    private Paint getSelectedTilePaint(){
        Paint selectedTilePaint = new Paint();
        selectedTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedTilePaint.setColor(Color.GREEN);
        return selectedTilePaint;
    }

    private Paint getPossiblyDangerousTilePaint(){
        Paint possiblyDangerousTilePaint = new Paint();
        possiblyDangerousTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        possiblyDangerousTilePaint.setColor(Color.LTGRAY);
        return possiblyDangerousTilePaint;
    }


    private Paint getTextPaint(){
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textFontSize);
        return textPaint;
    }

    private Paint getStartingTilePaint(){
        Paint startingTilePaint = new Paint();
        startingTilePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        startingTilePaint.setColor(Color.parseColor("#acacac"));
        return startingTilePaint;
    }

    private Paint getStartingTileTextPaint(){
        Paint startingTileTextPaint = new Paint();
        startingTileTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        startingTileTextPaint.setColor(Color.BLACK);
        startingTileTextPaint.setTextSize(startingTileTextFontSize);
        return startingTileTextPaint;
    }

    private Paint getNoteTextPaint(){
        Paint noteTextPaint = new Paint();
        noteTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        noteTextPaint.setColor(Color.BLACK);
        noteTextPaint.setTextSize(noteFontSize);
        return noteTextPaint;
    }

    private Paint getMistakeTextPaint(){
        Paint mistakeTextPaint = new Paint();
        mistakeTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mistakeTextPaint.setColor(Color.RED);
        mistakeTextPaint.setTextSize(textFontSize);
        return mistakeTextPaint;
    }

    // drawing

    private void drawLines(Canvas canvas){
        canvas.drawRect(0F,0F,(float) getWidth(),(float) getHeight(),getThickLinePaint());

        for(int i=0;i<size;i++){
            Paint paint;
            if(i% sqrtSize ==0){
                paint = getThickLinePaint();
            } else {
                paint = getThinLinePaint();
            }
            canvas.drawLine(i*tileSizePixels, 0F,i*tileSizePixels,(float) getHeight(),paint);
            canvas.drawLine(0F,i*tileSizePixels, (float) getWidth(), i*tileSizePixels,paint);
        }
    }

    private void fillTile(Canvas canvas,int row,int col, Paint paint){
        canvas.drawRect(col*tileSizePixels,row*tileSizePixels,(col+1)*tileSizePixels,(row+1)*tileSizePixels,paint);
    }

    private void fillTiles(Canvas canvas){
        if(selectedCol == -1 || selectedRow == -1){
            return;
        }

        for(int rowIndex=0; rowIndex<size;rowIndex++){
            for(int colIndex=0; colIndex<size;colIndex++){

                if(tiles[rowIndex][colIndex].isStartingTile){
                    fillTile(canvas,rowIndex,colIndex,getStartingTilePaint());
                } else if(rowIndex == selectedRow && colIndex == selectedCol){
                    fillTile(canvas,rowIndex,colIndex,getSelectedTilePaint());
                } else if (rowIndex == selectedRow || colIndex == selectedCol) {
                    fillTile(canvas,rowIndex,colIndex, getPossiblyDangerousTilePaint());
                } else if (rowIndex/ sqrtSize == selectedRow/ sqrtSize && colIndex/ sqrtSize == selectedCol/ sqrtSize) {
                    fillTile(canvas,rowIndex,colIndex, getPossiblyDangerousTilePaint());
                }
            }
        }
    }

    private void drawText(Canvas canvas){
        Paint textPaint;
        String value;
        float textWidth;
        float textHeight;

        for(int rowIndex=0; rowIndex<size;rowIndex++){
            for(int colIndex=0; colIndex<size;colIndex++){
                Rect textBounds = new Rect();
                if(tiles[rowIndex][colIndex].value == 0){
                    for (Integer note: tiles[rowIndex][colIndex].notes) {
                        int rowInTile = (note-1)/sqrtSize;
                        int colInTile = (note-1)%sqrtSize;
                        textPaint = getNoteTextPaint();
                        value = note.toString();
                        textPaint.getTextBounds(value,0,value.length(),textBounds);
                        textWidth = textPaint.measureText(value);
                        textHeight = textBounds.height();

                        canvas.drawText(note.toString(),
                                (colIndex*tileSizePixels)+(colInTile*noteSizePixels)+noteSizePixels/2 - textWidth/2F,
                                (rowIndex*tileSizePixels)+(rowInTile*noteSizePixels)+noteSizePixels/2 + textHeight/2F,
                                textPaint);
                    }
                } else {
                    if(tiles[rowIndex][colIndex].isStartingTile)
                    {
                        textPaint = getStartingTileTextPaint();
                    } else {
                        textPaint = getTextPaint();
                    }

                    value = tiles[rowIndex][colIndex].value.toString();
                    textPaint.getTextBounds(value,0,value.length(), textBounds);
                    textWidth = textPaint.measureText(value);
                    textHeight = textBounds.height();

                    canvas.drawText(value,
                            (colIndex*tileSizePixels)+tileSizePixels/2-textWidth/2,
                            (rowIndex*tileSizePixels)+tileSizePixels/2+textHeight/2,
                            textPaint);

                }
            }
        }
    }

    private void drawMistake(Canvas canvas) {

        int col;
        int row;
        float textWidth;
        float textHeight;
        String value;
        Rect textBounds;
        Paint textPaint = getMistakeTextPaint();

        for (Pair<Integer,Integer> mistake: mistakenTiles) {
            row = mistake.first;
            col = mistake.second;
            value = tiles[row][col].value.toString();
            textBounds = new Rect();
            textPaint.getTextBounds(value,0,value.length(), textBounds);
            textWidth = textPaint.measureText(value);
            textHeight = textBounds.height();
            canvas.drawText(value,
                    (col*tileSizePixels)+tileSizePixels/2-textWidth/2,
                    (row*tileSizePixels)+tileSizePixels/2+textHeight/2,
                    getMistakeTextPaint());
        }


    }

    private void updateMeasurements(int width){
        tileSizePixels = (float) width/size;
        noteSizePixels = tileSizePixels/(float) sqrtSize;
        textFontSize = tileSizePixels / 1.5F;
        noteFontSize = tileSizePixels / (float) sqrtSize;
        startingTileTextFontSize = tileSizePixels / 1.5F;
    }

    @Override
    public void onDraw(Canvas canvas){
        updateMeasurements(getWidth());
        fillTiles(canvas);
        drawLines(canvas);
        drawText(canvas);
        drawMistake(canvas);
    }



    // handle input

    private void handleTouchEvent(float x, float y){

        int possibleSelectedRow = (int) (y/tileSizePixels);
        int possibleSelectedCol = (int) (x/tileSizePixels);
        listener.onTileTouched(possibleSelectedRow,possibleSelectedCol);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            handleTouchEvent(event.getX(),event.getY());
            return true;
        }
        return false;
    }

    public void updateSelectedTile(Integer row, Integer col){
        selectedRow = row;
        selectedCol = col;
        invalidate();
    }

    public void updateTiles(Tile[][] tiles){
        this.tiles = tiles;
        invalidate();
    }

    public void updateMistakenTile(Set<Pair<Integer, Integer>> mistakes) {
        this.mistakenTiles = mistakes;
        invalidate();
    }

    public interface OnTouchListener{
        void onTileTouched(int row, int col);
    }

    public void registerListener(SudokuBoardView.OnTouchListener listener){
        this.listener = listener;
    }
}
