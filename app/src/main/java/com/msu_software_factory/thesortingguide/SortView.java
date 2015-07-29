package com.msu_software_factory.thesortingguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James R. Jacobs on 7/19/2015.
 */
public class SortView extends ImageView {
    private Context mContext;
    int rHeight, rWidth = 0;
    Rect[] sortedUnits;
    int[] toSort;
    Paint paint = new Paint();
    Boolean firstDraw = true;
    static MainActivity activity;
    int textSize = 10;

    public SortView(Context context, AttributeSet attr){
        super(context, attr);
        mContext = context;
        try{
            this.toSort = activity.toSort;
            sortedUnits = new Rect[toSort.length];
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    protected void onDraw(Canvas c){
        if (firstDraw){
            viewSetUp();
            firstDraw = false;
        }
        for (int i = 0; i < sortedUnits.length; i++){
            c.drawRect(sortedUnits[i], paint);
            float txPos = sortedUnits[i].exactCenterX();
            float tyPos = sortedUnits[i].exactCenterY();

            drawDigit(c, textSize, txPos, tyPos, Color.YELLOW, Integer.toString(toSort[i]));
        }
    }
    public static void setActivity(MainActivity act){
        activity = act;
    }
    private void viewSetUp(){
        int slotSize = this.getWidth() / toSort.length;
        int xPos = 10;
        int yPos = this.getHeight() / 2;
        rWidth = slotSize - 20;
        rHeight = rWidth;
        for (int i = 0; i < sortedUnits.length; i++){
            sortedUnits[i] = new Rect(xPos,yPos,xPos + rWidth - 1,yPos + rHeight - 1);
            xPos += rWidth + 20;
        }
        paint.setColor(Color.BLUE);
        textSize = setSuitableTextSize();
        paint.setTextAlign(Paint.Align.CENTER);
        invalidate();
    }
    private int setSuitableTextSize() {
        int textSize = getEstimateTextSize();
        for (; textSize > 0; textSize--) {
            if (isTextSizeSuitable(textSize))
                return textSize;
        }
        return textSize;
    }
    private boolean isTextSizeSuitable(int size) {
        List mTextBreakPoints = new ArrayList<Integer>();
        paint.setTextSize(size);
        String mText = "10";
        int start = 0;
        int end = mText.length();
        while (start < end) {
            int len = paint.breakText(mText, start, end, true, rWidth,
                    null);
            start += len;
            mTextBreakPoints.add(start);
        }
        return mTextBreakPoints.size() * size < rHeight;
    }
    private int getEstimateTextSize() {
        return (int) Math.sqrt(rWidth * rHeight / 4);
    }
    private void drawDigit(Canvas canvas, int textSize,  float cX, float cY, int color, String text) {
        Paint tempTextPaint = new Paint();
        tempTextPaint.setAntiAlias(true);
        tempTextPaint.setStyle(Paint.Style.FILL);

        tempTextPaint.setColor(color);
        tempTextPaint.setTextSize(textSize + 13);

        float textWidth = tempTextPaint.measureText(text);
        //if cX and cY are the origin coordinates of the your rectangle
        //cX-(textWidth/2) = The x-coordinate of the origin of the text being drawn
        //cY+(textSize/2) =  The y-coordinate of the origin of the text being drawn

        canvas.drawText(text, cX - (textWidth / 2), cY + (textSize / 2), tempTextPaint);
    }
}
