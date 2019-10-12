package watermelon.heelheal;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.net.Uri;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

public class ImageAnalyzer extends AppCompatActivity
{

    Canvas canvas;
    double pixelsPerInch;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_analyzer);
        ImageView imageView = findViewById(R.id.wound_image);
        LinearLayout linearLayout = findViewById(R.id.linear_layout_image_analyzer);


            imageView.setImageURI(MainActivity.currentImageURI);
//            linearLayout.addView(MainActivity.imageView);
//            imageView.setImageBitmap(MainActivity.bitmap);
//            imageView.setBackgroundColor(Color.BLACK);
//            System.out.println(MainActivity.bitmap.getPixel(0,0));


        canvas = new Canvas(this,null);
        linearLayout.addView(canvas);
        final TextView textView = findViewById(R.id.instructions);
//        FloatingActionButton fab = new FloatingActionButton(this);


        FloatingActionButton fab = findViewById(R.id.fab_done);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (canvas.stage.equals("perimeter"))
                {
                    System.out.println(canvas.polygonArea(1));
                    System.out.println(canvas.getPerimeter(1));
                    canvas.stage = "inch";
                    textView.setText("Select edges of inch");
                    return;
                }
                if (canvas.stage.equals("inch"))
                {
                    pixelsPerInch = canvas.getPixelsPerInch();
                    if (pixelsPerInch == -1) return;
                    if (pixelsPerInch != 0)
                    {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        int numOfResults = pref.getInt("numOfResults",0);
                        Set<String> resultSet = pref.getStringSet("resultSet", new HashSet<String>());
                        String newResult = "Result #" + (numOfResults+1) + "\n" +
                                        "surface_area:"+ canvas.polygonArea(1) + "\n"
                        + "perimeter:"+ canvas.getPerimeter(1) + "\n"+"max_length:"+canvas.getMaxLength()+
                                "\n"+ "ppi:"+
                                pixelsPerInch;
                        resultSet.add(newResult);
                        editor.putStringSet("resultSet",resultSet);
                        editor.putInt("numOfResults", numOfResults + 1);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                        intent.putExtra("Info:",newResult);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

//        canvas.getRootView().addView(fab);
    }


}
