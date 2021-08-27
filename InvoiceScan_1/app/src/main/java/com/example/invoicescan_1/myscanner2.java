package com.example.invoicescan_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class myscanner2 extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    DecoratedBarcodeView decoratedBarcodeView;
    CaptureManager captureManager;
    Button button;
    Boolean isLight=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myscanner2);

        decoratedBarcodeView=(DecoratedBarcodeView) findViewById(R.id.barcodeview);
        captureManager=new CaptureManager(myscanner2.this,decoratedBarcodeView);
        captureManager.initializeFromIntent(getIntent(),savedInstanceState);
        captureManager.decode();
        button=(Button) findViewById(R.id.lightButton);
        if (!hasFlash()) {
            button.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLight){
                    decoratedBarcodeView.setTorchOff();
                    isLight=false;
                }
                else{
                    decoratedBarcodeView.setTorchOn();
                    isLight=true;
                }
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return decoratedBarcodeView.onKeyDown(keyCode,event)||super.onKeyDown(keyCode, event);
    }

    public boolean hasFlash(){
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        isLight=true;
    }

    @Override
    public void onTorchOff() {
        isLight=false;
    }
}