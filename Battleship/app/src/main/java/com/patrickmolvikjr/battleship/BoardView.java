package com.patrickmolvikjr.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

public class BoardView extends AppCompatImageView {

    Paint paint;
    static public int cellWidth;


    public BoardView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        paint.setTypeface(typeface);
        paint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw( Canvas canvas){
        int height = canvas.getHeight() - 1;
        int width = canvas.getWidth() - 1;

        if(height > width){
            cellWidth = width / 11;
        }else{
            cellWidth = height / 11;
        }

//        int cellHeight = height / 11;

        paint.setTextSize(cellWidth);

        /*
        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(0, 0, 0, height, paint);
        canvas.drawLine(0, height, width, height, paint);
        canvas.drawLine(width, 0, width, height, paint);
*/
        for(int i=0; i < 12; i++){
            canvas.drawLine(0, (cellWidth * i), (cellWidth * 11), (cellWidth * i), paint);
            canvas.drawLine((cellWidth * i), 0, (cellWidth * i), cellWidth * 11, paint);
        }

        Rect textBounds = new Rect();
        paint.getTextBounds("A", 0, 1, textBounds);
        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        int textX = cellWidth / 2;
        int textY = cellWidth + ((cellWidth / 2) - (textHeight));
        textY += cellWidth;

        canvas.drawText("A", textX, textY, paint);

        textY += cellWidth;

        canvas.drawText("B", textX, textY, paint);

    }

}
