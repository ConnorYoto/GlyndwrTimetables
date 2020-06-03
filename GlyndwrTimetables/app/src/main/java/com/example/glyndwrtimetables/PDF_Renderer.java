package com.example.glyndwrtimetables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class PDF_Renderer extends AppCompatActivity
{
    //Interface
    Button backButton;
    ImageView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__renderer);

        setupControls();
    }   //  protected void onCreate(Bundle savedInstanceState)


    protected void setupControls()
    {
        // Back Button
        backButton = findViewById(R.id.pdf_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        pdfView = findViewById(R.id.PDF_ImageView);

        try
        {
            openPDF();

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private void openPDF() throws IOException
    {
        // Intent Date
        Intent intent = getIntent();
        String targetPDF = intent.getStringExtra("PDF_FILEPATH");
        Log.w("FILEPATH = ", targetPDF);

        File file = new File(targetPDF);

        ParcelFileDescriptor fileDescriptor = null;
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        //min. API Level 21
        PdfRenderer pdfRenderer = null;
        pdfRenderer = new PdfRenderer(fileDescriptor);

        //Display page 0
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
        int rendererPageWidth = rendererPage.getWidth();
        int rendererPageHeight = rendererPage.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(rendererPageWidth, rendererPageHeight, Bitmap.Config.ARGB_8888);
        rendererPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        pdfView.setImageBitmap(bitmap);
        rendererPage.close();

        pdfRenderer.close();
        fileDescriptor.close();
    }

}   //  public class PDF_Renderer extends AppCompatActivity
