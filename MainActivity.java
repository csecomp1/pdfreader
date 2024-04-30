package com.example.pdfreader;

import android.os.Bundle;
import android.Manifest;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pdfreader.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.BuildersKt;

public class MainActivity extends AppCompatActivity {


  private PdfAdapter pdfAdapter;
  private List<File> pdfList;
  private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         runtimePermission();
    }

    private void runtimePermission() {
        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayPdf();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this,"Permission required",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }
    public ArrayList<File> findPdf(File file){
          ArrayList<File>arrayList=new ArrayList<>();
          File[]files=file.listFiles();
          if(files!=null) {
              for (File singleFile : files) {
                  if (singleFile.isDirectory() && !singleFile.isHidden()) {
                      arrayList.addAll(findPdf(singleFile));
                  } else {
                      if (singleFile.getName().endsWith(".pdf")) {
                          arrayList.add(singleFile);
                      }
                  }
              }
          }
          else{
              Toast.makeText(MainActivity.this,"can't load pdf",Toast.LENGTH_SHORT);
          }
          return arrayList;
    }
    private void displayPdf() {
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        pdfList=new ArrayList<>();
        pdfList.addAll(findPdf(Environment.getExternalStorageDirectory()));
        pdfAdapter=new PdfAdapter(this,pdfList);
        recyclerView.setAdapter(pdfAdapter);
    }


}