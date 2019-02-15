package tecnologia.apptectextil;

/**
 * Created by lescalante on 27/03/2018.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // INI AGREGADO
        mWebView = (WebView) findViewById(R.id.webView);
        // Activamos javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Url que carga la app (webview)
        mWebView.loadUrl("https://drive.google.com/drive/folders/1THF57D16ZmrGQ5MYbKlOYUrURyyxX98_?usp=sharing");
        //https://drive.google.com/open?id=1wS7tHiK8y9Io0NsZscDmf8yDXbIsR5dX
        // Forzamos el webview para que abra los enlaces internos dentro de la la APP
        mWebView.setWebViewClient(new WebViewClient());
        // Forzamos el webview para que abra los enlaces externos en el navegador
        mWebView.setWebViewClient(new MyAppWebViewClient());
        // FIN AGREGADO

    }

}