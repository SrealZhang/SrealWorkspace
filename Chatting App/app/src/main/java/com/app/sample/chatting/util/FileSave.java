package com.app.sample.chatting.util;

import android.os.Environment;
import android.util.Log;

import com.app.sample.chatting.MyApplication;

import java.io.File;

/**
 * Created by Yangbin on 2016/3/24.
 */
public class FileSave {
    //文件夹目录"/sdcard/FirstFolder/SecondFolder"，多级目录必须逐一创建

    public static String FirstFolder = "neoLife";//一级目录

    public static String SecondFolder = ".neoLifeFriendsIcon";//二级目录

     /*ALBUM_PATH取得机器的SD卡位置，File.separator为分隔符“/”*/

    public final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + File.separator + FirstFolder + File.separator;

    public final static String Second_PATH = ALBUM_PATH + SecondFolder + File.separator;

    public static boolean createDir() {
        //检查手机上是否有外部存储卡

        Log.d("yangbin", ALBUM_PATH);
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        if (!sdCardExist) {//如果不存在SD卡，进行提示

            MyApplication.showToast("请插入外部SD存储卡");
            return false;
        } else {//如果存在SD卡，判断文件夹目录是否存在

            //一级目录和二级目录必须分开创建

            File dirFirstFile = new File(ALBUM_PATH);//新建一级主目录

            if (!dirFirstFile.exists()) {//判断文件夹目录是否存在

                dirFirstFile.mkdir();//如果不存在则创建
            }
            File dirSecondFile = new File(Second_PATH);//新建二级主目录

            if (!dirSecondFile.exists()) {//判断文件夹目录是否存在
                dirSecondFile.mkdir();//如果不存在则创建
            }
            return true;
        }
    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
