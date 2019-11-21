package com.thetechroot.vision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.thetechroot.vision.CommonCl.SharedCommon;

import java.util.HashMap;

import static com.thetechroot.vision.CommonCl.SharedCommon.key1;

public class WebTransActivity extends AppCompatActivity {

    Button btnlimit;
    WebView webview;

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    String appid = BuildConfig.APPLICATION_ID;

    CardView cardviewhide;

    ProgressBar progressBar;

    TextView txtcard;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_trans);

        txtcard = (TextView)findViewById(R.id.txtcard);
        txtcard.setText("Translation Service Is Not Fully Active");
        progressBar = (ProgressBar) findViewById(R.id.progressBarweb);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0365A9, android.graphics.PorterDuff.Mode.MULTIPLY);


        //Intents
        Intent intent = getIntent();
        String URL = intent.getExtras().getString("URL");
        String TITLE = intent.getExtras().getString("TITLE");

        String HIDE = intent.getExtras().getString("HIDE");

        /*if (HIDE.equals("HIDE")){

            cardviewhide.setVisibility(View.GONE);
        }*/


        setTitle(TITLE);


/*

        cardviewhide = (CardView)findViewById(R.id.cardviewhide);



        if (HIDE.equals("HIDE")){
            cardviewhide.setVisibility(View.GONE);

        }
*/

        /* btnlimit = (Button) findViewById(R.id.btnlimit);*/

        webview = (WebView) findViewById(R.id.transweb);

        WebSettings webSettings = webview.getSettings();
        //webSettings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(URL);

        webview.setWebViewClient(new myWebClient());
        webview.getSettings().setJavaScriptEnabled(true);


    }

        public class myWebClient extends WebViewClient
        {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                Toast.makeText(getApplicationContext(), " No Internet Connection found.", Toast.LENGTH_SHORT).show();

                webview.setVisibility(View.GONE);
                txtcard.setText("Check Your Internet Connection !");
                progressBar.setVisibility(View.VISIBLE);


            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
            }
        }

        // To handle "Back" key press event for WebView to go back to previous screen.
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event)
        {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
                webview.goBack();
                return true;
            }
            return super.onKeyDown(keyCode, event);
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

            int i = SharedCommon.getPreferencesInt(getApplicationContext(), key1,50);

            Toast toast = Toast.makeText(this, "Mail Us For More Detail", Toast.LENGTH_LONG);
            View view = toast.getView();

            view.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.BLACK);

            toast.show();

            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode("thexenonstudio@gmail.com") +
                    "?subject=" + Uri.encode("Around Me - Contact") +
                    "&body=" + Uri.encode("Hello, Type Your Query/Problem/Bug/Suggestions Here"+" \n\n\n ------------ \n\n Version Code : "+versionCode+"\n Version Name : "+versionName+"\n Credit Left : "+i+"\n Application ID : "+appid);
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send Mail Via : "));

        }

        else if (id == R.id.action_help) {


            Intent startIntent = new Intent(WebTransActivity.this, HelpActivity.class);
            startActivity(startIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(WebTransActivity.this, TextActivity.class);
        startActivity(startIntent);
    }*/
}
