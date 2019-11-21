package com.thetechroot.vision;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thetechroot.vision.CommonCl.SharedCommon;

import java.net.URL;

import static com.thetechroot.vision.CommonCl.SharedCommon.key1;

public class QRUrlActivity extends AppCompatActivity {

   // private static final String URL = "";
    Button btnlimit;
    WebView webview;

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    String appid = BuildConfig.APPLICATION_ID;



    CardView cardviewhide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrurl);

        //Intents
        Intent intent = getIntent();
        String URL = intent.getExtras().getString("URL");
        String TITLE = intent.getExtras().getString("TITLE");

       // String HIDE = intent.getExtras().getString("HIDE");

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
        webSettings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(URL);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.browser_menu, menu);
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

        if (id == R.id.action_help) {


            Intent startIntent = new Intent(QRUrlActivity.this, HelpActivity.class);
            startActivity(startIntent);
       
            return true;
        }
        if (id == R.id.action_copy) {

            Intent intent = getIntent();
            String URL = intent.getExtras().getString("URL");



            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", URL);
            clipboard.setPrimaryClip(clip);


            Toast.makeText(this, "Copied To Clipboard", Toast.LENGTH_SHORT).show();

            return true;
        }


        if (id == R.id.action_browser) {

            Intent intent = getIntent();
            String URL = intent.getExtras().getString("URL");

            /*WebView webView = new WebView(QRUrlActivity.this);
            webView.loadUrl(URL);*/

           Intent i = new Intent(Intent.ACTION_VIEW);
           i.setData(Uri.parse(URL));
           startActivity(i);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent startIntent = new Intent(QRUrlActivity.this, NewQRActivity.class);
        startActivity(startIntent);
    }
}
