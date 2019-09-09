package studentapp.schoolsafety.com;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PDF_Viewer extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__viewer);
        Bundle bundle = getIntent().getExtras();
        String pdfurl = bundle.getString("pdf_url");
        webview = (WebView) findViewById(R.id.pdfwebview);
        String url = Constant.PDF_URL+pdfurl;
        if(url!= null)
        {
            Log.v("URL",url.toString());
//            webview.loadUrl(url.toString());
            webview.setBackgroundColor(Color.TRANSPARENT);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    super.onPageStarted(view, url, favicon);
                }


            });
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webview.setWebChromeClient(new WebChromeClient() {

            });
            webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        }


    }


}
