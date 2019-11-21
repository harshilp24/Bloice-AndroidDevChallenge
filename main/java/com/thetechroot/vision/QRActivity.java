package com.thetechroot.vision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.thetechroot.vision.CommonCl.SharedCommon;
import com.thetechroot.vision.Helper.GraphicOverlay;
import com.thetechroot.vision.Helper.RectOverlay;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.thetechroot.vision.CommonCl.SharedCommon.key1;

public class QRActivity extends AppCompatActivity {

    CameraView cameraView;

    private DatabaseReference mDatabase;


    android.app.AlertDialog WaitingDialog;

    GraphicOverlay graphicOverlay;

    Button btnscan,qr_plan,btnplanintent;

    TextView txtenable;

    int clickcount=0;





    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(QRActivity.this, MainActivity.class);
        startActivity(startIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thetechroot.vision.R.layout.activity_qr);




        btnplanintent = (Button)findViewById(R.id.btn_plan_credit);



        qr_plan= (Button) findViewById(R.id.btn_qr_plan);

        qr_plan.setEnabled(false);

        btnscan = (Button)findViewById(com.thetechroot.vision.R.id.btn_qr);





        graphicOverlay = (GraphicOverlay)findViewById(com.thetechroot.vision.R.id.graphic_qr);
        cameraView = (CameraView)findViewById(com.thetechroot.vision.R.id.camera_view_qr);



        WaitingDialog = new SpotsDialog.Builder()
                .setCancelable(false)
                .setMessage("Please Wait")
                .setContext(this)
                .build();

        //LIMIT

        int i = SharedCommon.getPreferencesInt(getApplicationContext(), key1,50);


        final SharedCommon sc = new SharedCommon();

        if (i <= 0)

        {
            dialogoveruse(i);
        }



        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = SharedCommon.getPreferencesInt(getApplicationContext(), key1,50);


                if (i <= 0){

                    Toast.makeText(QRActivity.this, "Limit Over "+i, Toast.LENGTH_SHORT).show();
                    btnscan.setEnabled(false);
                    dialogoveruse(i);
                }

                i--;
                sc.putPreferencesInt(getApplicationContext(),SharedCommon.key1,i);


               // Toast.makeText(QRActivity.this, ""+i, Toast.LENGTH_SHORT).show();
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();

                if(clickcount==1)
                {

                    Log.e("Home", "@@@ Response clickcount" + clickcount);

                    //first time clicked to do this
                    //Toast.makeText(getApplicationContext(),"Button clicked first time!", Toast.LENGTH_LONG).show();
                }
                else if (clickcount >= 10)
                {
                    //check how many times clicked and so on
                    btnplanintent.setVisibility(View.VISIBLE);

                }

            }
        });

        btnplanintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startIntent = new Intent(QRActivity.this, PriceListActivity.class);
                startActivity(startIntent);
            }
        });

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {



            }

            @Override
            public void onError(CameraKitError cameraKitError) {

                Log.e("QR", "@@@ Response cameraKitError" + cameraKitError.toString());


            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                WaitingDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                cameraView.stop();

                scanqr(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });




        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void scanqr(Bitmap bitmap) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE,
                                FirebaseVisionBarcode.FORMAT_PDF417)
                        .build();

        FirebaseVisionBarcodeDetector barcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        barcodeDetector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {

                        processresult(firebaseVisionBarcodes);
                        Log.e("QR", "@@@ Response firebaseVisionBarcodes" + firebaseVisionBarcodes.toString());

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("QR", "@@@ Response e" + e.toString());

                Toast.makeText(QRActivity.this, "ERROR : "+e, Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void processresult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {

        for (FirebaseVisionBarcode item : firebaseVisionBarcodes)
        {

            Rect rectbounds  = item.getBoundingBox();
            RectOverlay rectoverlay = new RectOverlay(graphicOverlay,rectbounds);
            graphicOverlay.add(rectoverlay);

            Log.e("QR", "@@@ Response item)" + item.toString());

            int value_type = item.getValueType();
            switch (value_type)
            {
                case FirebaseVisionBarcode.TYPE_TEXT:
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(item.getRawValue());

                    Log.e("QR", "@@@ Response item.getRawValue()" + item.getRawValue().toString());

                    builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });

                    // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                    Toast toast = Toast.makeText(this, ""+item.getRawValue(), Toast.LENGTH_LONG);
                    View view = toast.getView();

                    view.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(Color.BLACK);

                    toast.show();

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                break;

                case FirebaseVisionBarcode.TYPE_URL:
                {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getRawValue()));

                    startActivity(intent);

                    Log.e("QR", "@@@ Response item.getRawValue()" + item.getRawValue().toString());

                }
                break;

                case FirebaseVisionBarcode.TYPE_CONTACT_INFO:
                {
                    String info = new StringBuilder("Name :" )
                            .append(item.getContactInfo().getName().getFormattedName())
                            .append("\n")
                            .append("Address :")
                            .append(item.getContactInfo().getAddresses().get(0).getAddressLines())
                            .append("\n")
                            .append("Email :")
                            .append(item.getContactInfo().getEmails().get(0).getAddress())
                            .toString();



                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(info);
                    builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                break;

                default:
                    break;
            }
        }

        WaitingDialog.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sug) {




            LayoutInflater li = LayoutInflater.from(QRActivity.this);
            View promptsView = li.inflate(R.layout.prompts, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    QRActivity.this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text




                                    if (userInput.getText().toString().length() == 0 || userInput.getText().toString().length() == 1) {


                                        Toast.makeText(QRActivity.this, "Please Enter Your Suggestion", Toast.LENGTH_SHORT).show();

                                        Log.e("Home", "@@@ Response editText.getText()" + userInput.getText());
                                    }

                                    else if(userInput.getText().toString().length() <= 10 || userInput.getText().toString().length() == 1) {

                                      /*  FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = current_user.getUid();
*/




                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Suggestions");

                                       /* mDatabase.setValue(userInput.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                              @Override
                                                                                              public void onComplete(@NonNull Task<Void> task) {

                                                                                                  HashMap<String, Editable> userMap = new HashMap<>();
                                                                                                  userMap.put("Suggestion",userInput.getText());
                                                                                                  Toast.makeText(QRActivity.this, "Recived", Toast.LENGTH_SHORT).show();
                                                                                              }

                                                                                          });
*/





                                                /*HashMap<String, Editable> userMap = new HashMap<>();
                                        userMap.put("Suggestion",userInput.getText());*/


                                        Toast.makeText(QRActivity.this, "Thanks For Your Suggestion : "+userInput.getText(), Toast.LENGTH_SHORT).show();

                                    }



                                    else {

                                      /*  FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = current_user.getUid();
*/




                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Suggestions");




                                        HashMap<String, Editable> userMap = new HashMap<>();
                                        userMap.put("Suggestion",userInput.getText());


                                       Toast.makeText(QRActivity.this, "Thanks For Your Suggestion ", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return true;
        }

        else if (id == R.id.action_help) {


            Intent startIntent = new Intent(QRActivity.this, HelpActivity.class);
            startActivity(startIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogoveruse(int i) {




        LayoutInflater li = LayoutInflater.from(QRActivity.this);
        View promptsView = li.inflate(R.layout.overuse_promt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                QRActivity.this);

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

                Toast.makeText(QRActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        final Button userInput = (Button) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent startIntent = new Intent(QRActivity.this, PriceListActivity.class);
                startActivity(startIntent);

            }
        });

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Contact Us",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {





                                Toast toast = Toast.makeText(QRActivity.this, "Mail Us For More Detail", Toast.LENGTH_LONG);
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

                                Intent startIntent = new Intent(QRActivity.this, MainActivity.class);
                                startActivity(startIntent);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();



        Toast.makeText(QRActivity.this, "Limit Overused"+i, Toast.LENGTH_SHORT).show();
        btnscan.setEnabled(false);
    }
}
