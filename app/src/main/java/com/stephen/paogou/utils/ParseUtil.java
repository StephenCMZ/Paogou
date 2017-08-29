package com.stephen.paogou.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by StephenChen on 2017/8/29.
 */

public class ParseUtil {

    /**
     * 解析网页 url
     * @param string
     * @return
     */
    public static List<String> paseHtmlUrls(String string){

        if (StringUtil.isEmpty(string)){
            return null;
        }

        Pattern pattern = Pattern.compile("href=\"https://www-paogou.cc/newsinfo-\\d{7}.html\">\\d{3}期：新版跑狗图");
        Matcher matcher = pattern.matcher(string);

        List<String> urls = new ArrayList<>();
        while (matcher.find()){
            String href = matcher.group(0);
            if (!StringUtil.isEmpty(href)){
                Pattern patternUrl = Pattern.compile("https://www-paogou.cc/newsinfo-\\d{7}.html");
                Matcher matcherUrl = patternUrl.matcher(href);
                if (matcherUrl.find()){
                    urls.add(matcherUrl.group(0));
                }
            }
        }
        if (urls.size() > 10) urls.remove(0);
        return urls;
    }

    /**
     * 解析图片 url
     * @param string
     * @return
     */
    public static String paseImageUrl(String string){

        if (StringUtil.isEmpty(string)){
            return null;
        }

        Pattern pattern = Pattern.compile("src=\"http://www.tkcp010.cc/upload/images/\\d{10}.jpg\" style=\"width: \\d{3}px; height: \\d{3}px;\"");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()){
            String src = matcher.group(0);
            if (!StringUtil.isEmpty(src)){
                Pattern patternUrl = Pattern.compile("http://www.tkcp010.cc/upload/images/\\d{10}.jpg");
                Matcher matcherUrl = patternUrl.matcher(src);
                if (matcherUrl.find()){
                    return matcherUrl.group(0);
                }
            }
        }

        return null;
    }

}
