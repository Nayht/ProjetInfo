package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebPage {

    private URL url;

    public WebPage(String url){
        try {
            this.url=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String downloadWebPage(){
        try {
            return downloadWebPageToEncapsulade();
        } catch (IOException e) {
            System.out.println("//////////////////////////");
            System.out.println("IOException lors du téléchargemnt de la page");
            System.out.println("//////////////////////////");
            e.printStackTrace();
        }
        return null;
    }

    private String downloadWebPageToEncapsulade() throws IOException {
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder webPage=new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            webPage.append(line);
        }
        return webPage.toString();
    }
}