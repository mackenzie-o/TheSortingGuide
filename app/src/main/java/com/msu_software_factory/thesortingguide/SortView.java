package com.msu_software_factory.thesortingguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Constructor;

/**
 * Created by James R. Jacobs on 7/19/2015.
 */
public class SortView extends ImageView {
    private Context mContext;
    Rect[] sortedUnits = new Rect[5];
    Paint paint = new Paint();
    Class sortingClass;
    static final Class<?>[] contSig = new Class[]{Context.class, AttributeSet.class};
    Constructor<? extends Sorting> sortingCont;
    Sorting sorter;

    public SortView(Context context, AttributeSet attr){
        super(context, attr);
        mContext = context;
        try {
            sortingClass = mContext.getClassLoader().loadClass("Sorting.java");
            sortingCont = sortingClass.getConstructor(contSig);
            sorter = sortingCont.newInstance(new Object[]{context, attr});

        }catch (Exception e){
            System.out.println(e);
        }

        int x = 20;
        int y = 20;
        int top = 50;
        int bottom = 50;
        for (int i = 0; i < 5; i++){
            sortedUnits[i] = new Rect(x,y,top,bottom);
            x += x + 10;
            y += y + 10;
            top += top + 10;
            bottom += bottom + 10;
        }
        paint.setColor(Color.BLUE);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c){
        for (Rect unit: sortedUnits){
            c.drawRect(unit, paint);
        }
        findViewById(R.id.result_text);
    }
}
