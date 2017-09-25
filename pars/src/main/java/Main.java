import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String main_url = "https://rozetka.com.ua/ua/gps-navigators/c80047/";
        Document doc = Jsoup.connect(main_url).get();
        Elements urls = doc.select(".paginator-catalog-l-link");
        int count = 0;
        for(Element i: urls){
            if(count > 0){
                page_prod(i.select("a").attr("href"));
            }
            count += 1;
        }
    }
    public static void page_prod(String url) throws IOException {
        Document prod = Jsoup.connect(url).get();
        Elements items = prod.select(".g-i-tile-i-title");
        for(Element i: items){
            page_rew(i.select("a").attr("href") + "/comments");
        }
    }
    public static void page_rew(String url) throws IOException {
        Document fin = Jsoup.connect(url).get();
        Elements marks = fin.select(".pp-review-inner");
        Elements nums = fin.select(".detail-tabs");
        if(nums.select(".m-tabs-i-comments").text().length() < 1){
            System.out.println("0 " + "reviews from" + " " + url);
        }else{
            System.out.println(nums.select(".m-tabs-i-comments").text() + " " + "reviews from" + " " + url);
        }
        String mark, review;
        FileWriter writer = new FileWriter("result.csv", true);
        BufferedWriter bufferWriter = new BufferedWriter(writer);
        for(Element i: marks){
            mark = i.select(".g-rating-stars-i").attr("content");
            if(mark.length() < 1){
                mark = "0";
            }
            review = i.select(".pp-review-text-i").text();
            bufferWriter.write(mark + "," + review + "\n");
        }
        bufferWriter.close();
    }
}