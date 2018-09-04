package com.github;

import com.github.common.http.HttpClientUtil;
import com.github.common.http.HttpOkClientUtil;

import java.awt.*;

public class WebNil {

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String []fontFamilies = ge.getAvailableFontFamilyNames();
        for (String fontFamily : fontFamilies) {
            System.out.println(fontFamily);
        }

        System.out.println("(" + HttpClientUtil.get("https://valid-isrgrootx1.letsencrypt.org/") + ")");
        System.out.println("\n\n\n==================\n\n\n");
        System.out.println("(" + HttpOkClientUtil.get("https://valid-isrgrootx1.letsencrypt.org/") + ")");
    }
}
