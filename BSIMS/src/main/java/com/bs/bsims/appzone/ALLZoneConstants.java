
package com.bs.bsims.appzone;

import java.util.ArrayList;

public class ALLZoneConstants {

    private static String HEADTITILE[];

    public static ArrayList<ALLZoneClassify> getData() {
        if (null == HEADTITILE) {
            return null;
        }
        ArrayList<ALLZoneClassify> newsClassify = new ArrayList<ALLZoneClassify>();
        ALLZoneClassify classify;
        for (int i = 0; i < HEADTITILE.length; i++) {
            classify = new ALLZoneClassify();
            classify.setId(i);
            classify.setTitle(HEADTITILE[i]);
            newsClassify.add(classify);
        }
        return newsClassify;
    }

    public static void SetData(ALLZoneModel allZoneModel) {
        HEADTITILE = allZoneModel.getMtype().split(",");
    }

    public static int GetDataSize() {
        try {
            return HEADTITILE.length;
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }
       
    }

}
