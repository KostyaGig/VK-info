package ru.kostya.vkinfo.utils;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NewTworkUtils {

    public static final String VK_API_BASE_URL= "https://api.vk.com/";
    public static final String VK_USERS_GET = "/method/users.get";
    public static final String PARAM_USER_iD = "user_ids";
    public static final String PARAM_VERSION = "v";
    public static final String ACCESS_TOKEN_URL = "access_token";
    public static final String ACCESS_TOKEN = "55f1418355f1418355f14183335582f9be555f155f141830abf43746fc799c9ff140e2e";

    //STATIC метод<который будет генерировать url адрес для запроса к API VK
    public static URL generateURL(String userId){
        Uri buildUri = Uri.parse(VK_API_BASE_URL + VK_USERS_GET)
                .buildUpon()
                //Метод который продолжает строить наш uri здесь мы указываем параметры в url ссылке,в нашем случае
                //Это пармаетром под названием user_ids и значением из поля эдит текст
                //И второй параметр это версия под названием v в url ссылке
                .appendQueryParameter(PARAM_USER_iD,userId)
                .appendQueryParameter(PARAM_VERSION,"5.21")
                .appendQueryParameter(ACCESS_TOKEN_URL,ACCESS_TOKEN)
                .build();
        //Теперь нужно сконвертировать наш uri в url
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
