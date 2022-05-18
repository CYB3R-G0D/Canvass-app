package com.cyb3rg0d.canvass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {
    SignatureView signatureView;
    ImageButton btnColor,btnClear,btnSave;
    SeekBar seekbar;
    TextView txtBrushSize;

    int defaultBrushColor;
    int defaultColor;

    private static String fileName;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Canvass");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Default actionBar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#3700B3")));

        // init Views
        signatureView = findViewById(R.id.signature_view);
        seekbar = findViewById(R.id.brushSize);
        txtBrushSize = findViewById(R.id.txtbrushSize);
        btnClear = findViewById(R.id.btnClear);
        btnColor = findViewById(R.id.btnColor);
        btnSave = findViewById(R.id.btnSave);

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPallet();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
                Toast.makeText(MainActivity.this, "Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!signatureView.isBitmapEmpty()){
                    saveImage();
                } else {
                    Toast.makeText(MainActivity.this, "Draw something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtBrushSize.setText(i+"px");
                signatureView.setPenSize(i);
                seekBar.setMax(100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        askStoragePermission();

        defaultBrushColor = ContextCompat.getColor(MainActivity.this,R.color.black);
        defaultColor = ContextCompat.getColor(MainActivity.this,R.color.white);

    }

    private void openColorPallet(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultBrushColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultBrushColor = color;
                signatureView.setPenColor(color);
            }
        });
        ambilWarnaDialog.show();
    }

    private void saveImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filename");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType. TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.length()==0){
                    Toast.makeText(MainActivity.this, "Give file a name", Toast.LENGTH_SHORT).show();
                } else {
                    fileName = path + "/" + input.getText() + ".png";
                    if (!path.exists()){
                        path.mkdirs();
                    }
                    File file = new File(fileName);
                    Bitmap bitmap = signatureView.getSignatureBitmap();

                    // converting bitmap in byte array
                    ByteArrayOutputStream BOS = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,0,BOS);
                    byte[] bitmapData = BOS.toByteArray();

                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(bitmapData);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error saving", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, "Saved as PNG", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void askStoragePermission() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) { }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.background_change:
                CanvasColor();
                return true;
            case R.id.about:
                Intent intent = new Intent(MainActivity.this,about.class);
                startActivity(intent);
                return true;
            case R.id.report:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/CYB3R-G0D/Canvass-app/issues"));
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CanvasColor() {
        AmbilWarnaDialog canvasColorDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog canvas, int canvasColor) {
                defaultColor = canvasColor;
                signatureView.setBackgroundColor(defaultColor);
                signatureView.clearCanvas();
            }
        });
        canvasColorDialog.show();
    }
}