package tecnologia.apptectextil;


import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by lescalante on 27/03/2018.
 */

public class MyAppWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
// Url base de la APP (al salir de esta url, abre el navegador) poner como se muestra, sin https://
        if(Uri.parse(url).getHost().endsWith("google.com")) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));


        view.getContext().startActivity(intent);

        //view.getContext().startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)

        return true;
    }

}
