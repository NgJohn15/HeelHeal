package watermelon.heelheal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

public class ResultsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        String info = getIntent().getStringExtra("Info:");
        String infos[] = info.split("\n");

        TextView resultsTitleText = findViewById(R.id.results_title);
        TextView surfaceAreaText = findViewById(R.id.surface_area);
        TextView perimeterText = findViewById(R.id.perimeter);
        TextView maxLengthText = findViewById(R.id.max_length);
        TextView healingText = findViewById(R.id.healing_time);
        ImageView results_imag = findViewById(R.id.results_image);
        double surface_area = Double.parseDouble(infos[1].split(":")[1]);
        double perimeter = Double.parseDouble(infos[2].split(":")[1]);
        double max_length = Double.parseDouble(infos[3].split(":")[1]);
        double ppi = Double.parseDouble(infos[4].split(":")[1]);

        resultsTitleText.setText(infos[0]);

        surface_area /= Math.pow(ppi,2);
        surfaceAreaText.setText("Wound Surface Area: " + surface_area + " square inches");

        perimeter /= ppi;
        perimeterText.setText("Wound Perimeter: " + perimeter + " inches");

        max_length /= ppi;
        final double final_max_length = max_length;
        maxLengthText.setText("Wound Max Length (diameter): " + max_length + " inches");

        double mmlength = max_length*25.4;
        int conservativeTime = (int) Math.ceil(mmlength/5);
        int longTime = (int) Math.ceil(mmlength/1);
        healingText.setText("Wound will heal " + conservativeTime + " to " + longTime + " weeks.");

        results_imag.setImageURI(MainActivity.currentImageURI);

        final LinearLayout linearLayout = findViewById(R.id.lin_res);

        Button compare = findViewById(R.id.compare);
        compare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                Set<String> resultSet = pref.getStringSet("resultSet", new HashSet<String>());
                int n = resultSet.size();
                String arr[] = new String[n];

                // Copying contents of s to arr[]
                System.arraycopy(resultSet.toArray(), 0, arr, 0, n);
                final String options[] = arr;
                AlertDialog.Builder builder = new AlertDialog.Builder(ResultsActivity.this);
                builder.setTitle("Pick a color");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String info = options[which];
                        String infos[] = info.split("\n");
                        double max_length2 = Double.parseDouble(infos[3].split(":")[1]);
                        double ppi = Double.parseDouble(infos[4].split(":")[1]);
                        max_length2/= ppi;
                        TextView textView = new TextView(ResultsActivity.this);
                        textView.setText("Change: " + final_max_length + " to " + max_length2);
                        linearLayout.addView(textView);
                    }
                });
                builder.show();
            }
        });
    }


}
