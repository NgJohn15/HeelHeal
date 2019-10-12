package watermelon.heelheal;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Canvas extends View
{
    Paint paint;
    Path path;
    List<Integer> coordinates = new LinkedList<>();
    boolean done = false;
    List<Integer> inches = new LinkedList<>();
    String stage = "perimeter";


    public Canvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

    }

    public double getDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt(Math.pow((x2-x1), 2) + (Math.pow((y2-y1), 2)));
    }

    public double getMaxLength()
    {
        double maxDistance = 0;
        for (int coord : coordinates)
        {
            for (int coord1: coordinates)
            {
                double x1 = coord/100000;
                double y1 = coord%100000;
                double x2 = coord1/100000;
                double y2 = coord1%100000;
                double temp = getDistance(x1, y1, x2, y2);
                if (temp > maxDistance)
                {
                    maxDistance = temp;
                }
            }
        }
        return maxDistance;
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float xPos = event.getX();
        float yPos = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                if (stage.equals("inch"))
                {
                    if (inches.size() < 2)
                    {
                        inches.add((int)xPos * 100000 + (int)yPos);
                        path.addOval(xPos,yPos,xPos+5,yPos+5, Path.Direction.CW);
                    }
                }
                else
                {
                    coordinates.add((int)xPos * 100000 + (int)yPos);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (stage.equals("inch")) break;
                path.lineTo(xPos,yPos);
                coordinates.add((int)xPos * 100000 + (int)yPos);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }
        invalidate();

        return super.onTouchEvent(event);
    }
    public double polygonArea(int increment)
    {
        // Initialze area
        double area = 0.0;


        // Calculate value of shoelace formula
        int j = coordinates.size() - 1;
        for (int i = 0; i < coordinates.size(); i+=increment)
        {
            area += (coordinates.get(j) / 100_000 + coordinates.get(i)/100_000) * (coordinates.get(j)%100_000 - coordinates.get(i)%100_000);
            // j is previous vertex to i
            j = i;
        }

        // Return absolute value
        return Math.abs(area / 2.0);
    }
    public double getPerimeter(int increment)
    {
        double perimeter = 0;
        int n = coordinates.size();
        for (int i = 0; i < n; i+=increment)
        {
            perimeter+=Math.sqrt(Math.pow(coordinates.get((i+increment)%n)%100_000 - coordinates.get(i)%100_000,2) + Math.pow(coordinates.get((i+increment)%n)/100_000 - coordinates.get(i)/100_000,2));

        }
        return perimeter;
    }
    public double getPixelsPerInch()
    {
        return Math.sqrt(Math.pow(inches.get(1)%100_000 - inches.get(0)%100_000,2) + Math.pow(inches.get(1)/100_000 - inches.get(0)/100_000,2));
    }
}


