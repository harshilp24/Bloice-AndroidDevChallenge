package com.thetechroot.vision;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.thetechroot.vision.CommonCl.SharedCommon;
import com.thetechroot.vision.Helper.GraphicOverlay;
import com.thetechroot.vision.Helper.TextGraphic;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import me.toptas.fancyshowcase.FancyShowCaseView;

import static com.thetechroot.vision.CommonCl.SharedCommon.key1;

public class TextActivity extends AppCompatActivity {

    CameraView cameraView;

    boolean clicked = false;
    int numClicks = 0;

    ImageView Gallerypick;


    android.app.AlertDialog WaitingDialog;

    GraphicOverlay graphicOverlay;

    Button btncap;
    TextView txtrecog,txtlan;

    Locale myLocale;

    Spinner spinnerlan;

    CardView cd_text_re;
    TextView txtview;

    Integer maxclicks = 5; Integer currentnumber = 1;

    private static final String API_KEY = "MY_API_KEY";

    ImageView arrowhide;

    //LAN STRING

    public  String lanspport = "English,\n Hindi(beta),\n Italian,\n Japanese,\n Portuguese,\n Spanish,\n Tamil(beta),\n Russian,\n Sanskrit,\n French,\n German,\n Chinese,\n Arabic,\n Marathi,\n Turkish,\n Swedish,\n Bengali,\n Ukrainian,\n Vietnamese,\n Romanian";

    public  String transspport = "English,\n Hindi(beta),\n Arabic, \n Tamil,\n Turkish,\n French,\n Gujarati,\n Dutch,\n Russian,\n Vietnamese, \n Marathi,\n Telugu,\n Spanish,\n Chinese,\n Swedish,\n kannada ";

    //URLS

    String PrivacyUrl = "https://translate.google.co.in/#auto/hi/";
    String EnglishURL = "https://translate.google.co.in/#auto/en/";
    String TamilURL = "https://translate.google.co.in/#auto/ta/";
    String ArabicURL = "https://translate.google.co.in/#auto/ar/";
    String DutchUrl = "https://translate.google.co.in/#auto/nl/";

    String FrenchURL = "https://translate.google.com/#auto/fr/";
    String TurkishURL = "https://translate.google.com/#auto/tr/";
    String GujratiUrl = "https://translate.google.com/#auto/gu/";
    String RussiaUrl = "https://translate.google.com/#auto/ru/";
    String VitURL = "https://translate.google.com/#auto/vi/";


    String MarathiUrl = "https://translate.google.com/#auto/mr/";
    String TeluguURL = "https://translate.google.com/#auto/te/";
    String SpanishURL = "https://translate.google.com/#auto/es/";
    String ChineseURL = "https://translate.google.com/#auto/zh-CN/";
    String SwedishUrl = "https://translate.google.com/#auto/sv/";

    String kannadaURL = "https://translate.google.com/#auto/kn/";






    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_text, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_help) {



            AlertDialog.Builder builder = new AlertDialog.Builder(TextActivity.this);

            builder.setMessage("Note Translate Service And Language Recognition In Not Fully Launched \n\n "+"You Can Recognize Language Listed Below \n\n"+lanspport+"\n\n Language Supported For Translate : \n\n"+transspport+"\n\n\t\t\t\t  -------------------------------- \n To Use Translator Click On Translate Icon Above \n \n For Now We Will Re-Direct You To A Browser To Detect language Using Google Translate \n\n Only Few language Are Available \n\n If You Translate Your Text Into Any language Your 2 Credits Will Be Used \n " );




            builder.setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            }).setNegativeButton("DEMO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new FancyShowCaseView.Builder(TextActivity.this)
                            .focusOn(findViewById(R.id.action_trans))
                            .title("\n \n Click Here To Translate Image Into Different Language  \n\n\n\n\n\n\n  You Can Recognize 20+ Different Language And Translate It Into 15+ Different Language With Text Recognition Feature")
                            .titleStyle(R.style.TextStyle, Gravity.BOTTOM| Gravity.BOTTOM)
                            .build()
                            .show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }
        if (id == R.id.action_contact) {



            Intent startIntent = new Intent(TextActivity.this, HelpActivity.class);
            startActivity(startIntent);
            finish();
            return true;
        }
        if (id == R.id.action_trans) {

            if ((txtrecog.getText().equals(""))){

                /*new FancyShowCaseView.Builder(TextActivity.this)
                        .focusOn(findViewById(R.id.action_trans))
                        .title("\n \n Click Here To Translate Image Into 15+ Different Language  \n\n\n\n\n\n\n  You Can Recognize 20+ Different Language And Translate It Into 15+ Different Language With Text Recognition Feature")
                        .titleStyle(R.style.TextStyle, Gravity.BOTTOM| Gravity.BOTTOM)
                        .build()
                        .show();*/

                /*new FancyShowCaseView.Builder(this)
                        .focusOn(findViewById(R.id.action_trans))
                        .title("Focus on Actionbar items")
                        .titleStyle(R.style.TextStyle, Gravity.CENTER_HORIZONTAL| Gravity.CENTER)
                        .build()
                        .show();*/


                Toast.makeText(this, "Please Capture Image", Toast.LENGTH_SHORT).show();


            }

            else {

                cd_text_re.setVisibility(View.VISIBLE);
                spinnerlan.setVisibility(View.VISIBLE);
                txtview.setVisibility(View.VISIBLE);
                //txtlan.setVisibility(View.VISIBLE);
            }
            return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(TextActivity.this, MainActivity.class);
        startActivity(startIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thetechroot.vision.R.layout.activity_text);

        doFirstRun();

        final Handler textViewHandler = new Handler();

        arrowhide = (ImageView)findViewById(R.id.arrowdown);
        arrowhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cd_text_re.setVisibility(View.GONE);
            }
        });

        txtview = (TextView)findViewById(R.id.txtviewlan);

        spinnerlan = (Spinner) findViewById(R.id.spinnerlan);

        cd_text_re = (CardView)findViewById(R.id.cd_text_re);
        txtrecog = (TextView)findViewById(R.id.txtrecog);

        /*Thread t=new Thread() {
            public void run() {

                try {
                    //sleep thread for 10 seconds, time in milliseconds
                    sleep(60000);

                    //start new activity
                   cameraView.start();
                   graphicOverlay.clear();

                    //destroying Splash activity
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };*/

        //start thread
        /*  t.start();*/

        btncap = (Button)findViewById(com.thetechroot.vision.R.id.btn_capture);



        int i = SharedCommon.getPreferencesInt(getApplicationContext(), key1,50);




        final SharedCommon sc = new SharedCommon();

        if (i <= 0)

        {
            dialogoveruse(i);
        }

        btncap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cd_text_re.setVisibility(View.GONE);


                int i = SharedCommon.getPreferencesInt(getApplicationContext(), key1,50);


                if (i <= 0){

                    Toast.makeText(TextActivity.this, "Limit Overused : "+i, Toast.LENGTH_SHORT).show();
                    btncap.setEnabled(false);
                    dialogoveruse(i);
                }

                i--;
                sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);


               // Toast.makeText(TextActivity.this, ""+i, Toast.LENGTH_SHORT).show();

                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });

        graphicOverlay = (GraphicOverlay)findViewById(com.thetechroot.vision.R.id.graphic_overlay);




        WaitingDialog = new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please Wait")
                .setContext(this)
                .build();

        cameraView = (CameraView)findViewById(com.thetechroot.vision.R.id.camera_view);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {


                Log.e("Home", "@@@ Response cameraKitError" + cameraKitError.toString());

              /*  final View co = findViewById(android.R.id.content);

                Snackbar snackbar3 = Snackbar.make(co, "Error : "+cameraKitError, Snackbar.LENGTH_LONG);
                snackbar3.show();*/
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                WaitingDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                regognizeText(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


    }

    private void regognizeText(Bitmap bitmap) {

        //Lan
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionCloudTextRecognizerOptions options =
                new FirebaseVisionCloudTextRecognizerOptions.Builder()
                        .setLanguageHints(Arrays.asList("en"))
                        .setLanguageHints(Arrays.asList("ja"))
                        .setLanguageHints(Arrays.asList("hi"))
                        .setLanguageHints(Arrays.asList("it"))
                        .setLanguageHints(Arrays.asList("pt"))
                        .setLanguageHints(Arrays.asList("es"))
                        .setLanguageHints(Arrays.asList("ta"))
                        .setLanguageHints(Arrays.asList("ru"))
                        .setLanguageHints(Arrays.asList("sa"))
                        .setLanguageHints(Arrays.asList("fr"))
                        .setLanguageHints(Arrays.asList("de"))
                        .setLanguageHints(Arrays.asList("zh"))
                        .setLanguageHints(Arrays.asList("mr"))
                        .setLanguageHints(Arrays.asList("tr"))
                        .setLanguageHints(Arrays.asList("ar"))
                        .setLanguageHints(Arrays.asList("ro"))
                        .setLanguageHints(Arrays.asList("uk"))
                        .setLanguageHints(Arrays.asList("vi"))
                        .setLanguageHints(Arrays.asList("bn"))
                        .setLanguageHints(Arrays.asList("uk"))
                        .build();


        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getCloudTextRecognizer(options);


        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(final FirebaseVisionText firebaseVisionText) {

                        translatelan(firebaseVisionText);


                         cd_text_re.setVisibility(View.VISIBLE);
                         txtrecog.setText(firebaseVisionText.getText());

                        drawtextvision(firebaseVisionText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                WaitingDialog.dismiss();

                Toast.makeText(TextActivity.this, "Oops ! Something Went Wrong , Try Again Later", Toast.LENGTH_LONG).show();

                final View co = findViewById(android.R.id.content);

                Snackbar snackbar3 = Snackbar.make(co, "Error : "+e, Snackbar.LENGTH_SHORT);
                snackbar3.show();
                Log.e("Home", "@@@ Response e" + e.toString());






            }
        });

    }

    private void translatelan(final FirebaseVisionText firebaseVisionText) {

        spinnerlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {


                int i = SharedCommon.getPreferencesInt(getApplicationContext(), key1,50);

                final SharedCommon sc = new SharedCommon();


                if (i <= 0){

                    Toast.makeText(TextActivity.this, "Limit Overused : "+i, Toast.LENGTH_SHORT).show();
                    btncap.setEnabled(false);
                    dialogoveruse(i);
                }

                if (pos == 1) {


                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(PrivacyUrl+firebaseVisionText.getText());*/
                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", PrivacyUrl+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Hindi");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Hindi ", Toast.LENGTH_SHORT).show();
                }
                if (pos == 2) {


                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);


                    /*WebView webView = new WebView(TextActivity.this);*//*
                    webView.loadUrl(TamilURL+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", TamilURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Tamil");
                    startActivity(intent);




                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Tamil ", Toast.LENGTH_SHORT).show();
                }
                if (pos == 3) {




                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);





                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(ArabicURL+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", ArabicURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Arabic");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Arabic ", Toast.LENGTH_SHORT).show();
                }

                if (pos == 4){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                    /*WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(DutchUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", DutchUrl+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Dutch");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Dutch ", Toast.LENGTH_SHORT).show();

                }
                if (pos == 5){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(TurkishURL+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", TurkishURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Turkish");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Turkish ", Toast.LENGTH_SHORT).show();


                }
                if (pos == 6){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                  /*  WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(FrenchURL+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", FrenchURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","French");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "French ", Toast.LENGTH_SHORT).show();

                }


                if (pos == 7){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", GujratiUrl+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Gujarati");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Gujarati ", Toast.LENGTH_SHORT).show();

                }

                if (pos == 8){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", EnglishURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","English");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "English ", Toast.LENGTH_SHORT).show();

                }
                if (pos == 9){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", RussiaUrl+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Russian");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Russian ", Toast.LENGTH_SHORT).show();

                }
                if (pos == 10){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", VitURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Vietnamese");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Vietnamese ", Toast.LENGTH_SHORT).show();

                }
                if (pos == 11){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", MarathiUrl+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Marathi");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Marathi ", Toast.LENGTH_SHORT).show();

                }
                if (pos ==12){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", TeluguURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Telugu");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Telugu ", Toast.LENGTH_SHORT).show();

                }

                if (pos ==13){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", SpanishURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Spanish");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Spanish ", Toast.LENGTH_SHORT).show();

                }
                if (pos ==14){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", ChineseURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Chinese");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Chinese ", Toast.LENGTH_SHORT).show();

                }
                if (pos ==15){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", SwedishUrl+firebaseVisionText.getText());
                    intent.putExtra("TITLE","Swedish");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "Swedish ", Toast.LENGTH_SHORT).show();

                }

                if (pos ==16){

                    i--;
                    i--;


                    sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);

                   /* WebView webView = new WebView(TextActivity.this);
                    webView.loadUrl(GujratiUrl+firebaseVisionText.getText());*/

                    Intent intent = new Intent(TextActivity.this, WebTransActivity.class);
                    intent.putExtra("URL", kannadaURL+firebaseVisionText.getText());
                    intent.putExtra("TITLE","kannada");
                    startActivity(intent);



                    //https://translate.google.co.in/#en/hi/Hello

                    Toast.makeText(TextActivity.this, "kannada ", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void drawtextvision(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if (blocks.size() == 0)
        {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_LONG).show();
            return;

        }

        Log.e("Home", "@@@ Response firebaseVisionText" + firebaseVisionText.toString());




        graphicOverlay.clear();
        for (int i=0;i<blocks.size();i++){

            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();

            for (int j=0;j<lines.size();j++)
            {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();


                for (int k=0;k<elements.size();k++){


                    TextGraphic textGraphic = new TextGraphic(graphicOverlay,elements.get(k));
                    graphicOverlay.add(textGraphic);


                }

            }


        }

        Thread t=new Thread() {
            public void run() {

                try {
                    //sleep thread for 10 seconds, time in milliseconds
                    sleep(60000);

                    Toast.makeText(TextActivity.this, "", Toast.LENGTH_SHORT).show();

                    //start new activity
                    cameraView.start();
                    graphicOverlay.clear();

                    //destroying Splash activity
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        WaitingDialog.dismiss();

        t.start();
    }

    private void dialogoveruse(int i) {




        LayoutInflater li = LayoutInflater.from(TextActivity.this);
        View promptsView = li.inflate(R.layout.overuse_promt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                TextActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final Spinner sp = (Spinner) promptsView
                .findViewById(R.id.spinnerpro);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(TextActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        final Button userInput = (Button) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent startIntent = new Intent(TextActivity.this, PriceListActivity.class);
                startActivity(startIntent);

            }
        });

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Contact Us",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {





                                Toast toast = Toast.makeText(TextActivity.this, "Mail Us For More Detail", Toast.LENGTH_LONG);
                                View view = toast.getView();

                                view.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                                TextView text = view.findViewById(android.R.id.message);
                                text.setTextColor(Color.BLACK);

                                toast.show();

                                Intent send = new Intent(Intent.ACTION_SENDTO);
                                String uriText = "mailto:" + Uri.encode("thexenonstudio@gmail.com") +
                                        "?subject=" + Uri.encode("Around Me - Premium Query") +
                                        "&body=" + Uri.encode("Hello, Write Query Here  ");
                                Uri uri = Uri.parse(uriText);

                                send.setData(uri);
                                startActivity(Intent.createChooser(send, "Send Mail Via : "));





                            }
                        })
                .setNegativeButton("Go Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                Intent startIntent = new Intent(TextActivity.this, MainActivity.class);
                                startActivity(startIntent);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



        Toast.makeText(TextActivity.this, "Limit Overused"+i, Toast.LENGTH_SHORT).show();
        btncap.setEnabled(false);
    }
    private void doFirstRun() {
        SharedPreferences settings = getSharedPreferences("FIRSTRUNTEXT9", MODE_PRIVATE);
        if (settings.getBoolean("isFirstRunDialogBoxtext9", true)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstRunDialogBoxtext9", false);
            editor.commit();

            //LAN NO.

            /*new FancyShowCaseView.Builder(this)
                    .focusOn(findViewById(R.id.action_trans))
                    .title("Click Here To Translate Into 15+ Different Language")
                    .titleStyle(R.style.TextStyle, Gravity.BOTTOM| Gravity.CENTER)
                    .build()
                    .show();*/


            AlertDialog.Builder builder = new AlertDialog.Builder(TextActivity.this);

            builder.setMessage("You Can Recognize 20+ Different Language And Translate It Into 15+ Different Language With Text Recognition Feature ");

            builder.setCancelable(false);

            builder.setPositiveButton("DEMO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    new FancyShowCaseView.Builder(TextActivity.this)
                            .focusOn(findViewById(R.id.action_trans))
                            .title("\n \n Click Here To Translate Image Into Different Language ")
                            .titleStyle(R.style.TextStyle, Gravity.BOTTOM| Gravity.BOTTOM)
                            .build()
                            .show();

                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();




        }
    }





}
