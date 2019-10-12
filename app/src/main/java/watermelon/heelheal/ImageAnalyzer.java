package watermelon.heelheal;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ImageAnalyzer extends AppCompatActivity
{

    Canvas canvas;
    double pixelsPerInch;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_analyzer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        canvas = new Canvas(this,null);
        setContentView(R.layout.image_analyzer);
        LinearLayout linearLayout = findViewById(R.id.linear_layout_image_analyzer);
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
                    System.out.println(pixelsPerInch);
                }
            }
        });

//        canvas.getRootView().addView(fab);
    }


}
