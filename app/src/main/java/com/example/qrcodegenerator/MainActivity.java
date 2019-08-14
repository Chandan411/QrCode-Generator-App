package com.example.qrcodegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static android.graphics.Bitmap.CompressFormat.PNG;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "1";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    Bitmap bitmap;
    ImageView qrImage;
    Button btn_generate;
    EditText editText;
    String [] qr;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_generate = findViewById(R.id.btn_generate);
        editText = findViewById(R.id.edit_text);
        qrImage = findViewById(R.id.imageView);

        file = new File( Environment.getExternalStorageDirectory() + "QRCODE");

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        bytearrayoutputstream = new ByteArrayOutputStream();

       // qr = new String[]{"1234", "2345", "45677"};

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateQr();
               // createDirectoryAndSaveFile(bitmap,editText.getText().toString());
            }
        });



    }

    public void GenerateQr() {
        QRGEncoder qrgEncoder = new QRGEncoder(editText.getText().toString(), null, QRGContents.Type.TEXT, 550);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();

            bitmap.compress(JPEG,60,bytearrayoutputstream);

            try

            {
                file.createNewFile();

                fileoutputstream = new FileOutputStream(file);

                fileoutputstream.write(bytearrayoutputstream.toByteArray());

                fileoutputstream.close();

            }

            catch (Exception e)

            {

                e.printStackTrace();

            }

            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

        // Save with location, value, bitmap returned and type of Image(JPG/PNG).
        try {
            QRGSaver.save(String.valueOf(file), editText.getText().toString().trim(), bitmap, QRGContents.ImageType.IMAGE_JPEG);

            //Toast.makeText(MainActivity.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + File.separator + "QRCODE");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/QRCODE/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/QRCODE/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
