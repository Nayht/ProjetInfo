package rssFlux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class RSSParser {

    public void run() {
        System.out.println(readRSSFeed("http://rss.cnn.com/rss/edition.rss"));
    }

    public String readRSSFeed(String urlAddress){
        try{
            URL rssUrl = new URL (urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
            String sourceCode = "";
            String line;
            while((line=in.readLine())!=null){
                System.out.println(line);
            }
            in.close();
            return sourceCode;
        } catch (MalformedURLException ue){
            System.out.println("URL malformée");
        } catch (IOException ioe){
            System.out.println("IO Exception");
        }
        return null;
    }
}
