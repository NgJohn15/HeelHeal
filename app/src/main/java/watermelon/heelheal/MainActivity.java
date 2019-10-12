package watermelon.heelheal;

import android.content.Intent;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity
{
    public static final int PICK_IMAGE = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    public static final int PERMISSION_CODE = 1001;
    public static Uri currentImageURI;
    public static ImageView imageView;
    public static Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("TEST 0");
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById(R.id.imageView);
        System.out.println("TEST B");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View view)
            {
                // Prompt user to upload a file.
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                {
                    // request user for location access
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions, PERMISSION_CODE);
                } else
                {
                    pickImage();
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//
//                    Uri selectedImage = intent.getData();
//                    String filepath = selectedImage.getPath();
//                    System.out.println(filepath);
//                    System.out.println("==============================IMAGE=====================: " +selectedImage.toString());
//
//                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
//                    Drawable myDrawable;
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
//                        imageView.setImageBitmap(bitmap);
////                        InputStream inputStream = getContentResolver().openInputStream(selectedImage);
////                        myDrawable = Drawable.createFromStream(inputStream, selectedImage.toString() );
////                        imageView.setBackground(myDrawable);
////                        imageView.setImageDrawable(myDrawable);
////                    imageView.setImageDrawable((myDrawable));
//                    }
//                    catch (Exception e) {
//                        myDrawable = getResources().getDrawable(R.drawable.ic_launcher_background);
//                    }
                }
            }
        });

    }

    private void pickImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        imageView = (ImageView) findViewById(R.id.imageView);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE)
        {
            Intent intent = new Intent(this, ImageAnalyzer.class);
            currentImageURI = data.getData();
//            imageView.setImageURI(data.getData());
//            BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
//            bitmap = draw.getBitmap();
//            CoordinatorLayout parent = findViewById(R.id.image_parent);
//            parent.removeView(imageView);
            startActivity(intent);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted
                    pickImage();
                } else
                {
                    // permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
