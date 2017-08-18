package com.linhminhoo.kukki.FunctionCommon;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by linhminhoo on 6/21/2015.
 */
public class PacketItem {



    public static final String local="192.168.0.106";
    public static final String url_register="http://"+local+"/Laravel/Kukki-Server/api/account";
    public static final String url_login="http://"+local+"/Laravel/Kukki-Server/api/account/login";
    public static final String url_newFeed_all="http://"+local+"/Laravel/Kukki-Server/api/account/newfeed-all";
    public static final String url_update_avatar="http://"+local+"/Laravel/Kukki-Server/api/account/update-avatar/";
    public static final String url_get_speciality="http://"+local+"/Laravel/Kukki-Server/api/account/speciality/";
    public static final String url_create_receipt="http://"+local+"/Laravel/Kukki-Server/api/account/create-receipt";
    public static final String code_register="28418s2Ff?4SaM4G0epYK01va:BPw";

    public PacketItem() {
    }
    public int ReturnScreenWidth(Context context){

        int width=0;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width=display.getWidth();

        return Math.round(width*5/6);

    }

}
