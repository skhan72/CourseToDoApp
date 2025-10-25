package org.khan.coursetodoapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileUtil {
    public static String readFile(Context ctx, String filename) {
        try {
            FileInputStream fis = ctx.openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            // no file or read issue -> return null (MainActivity will start empty)
            return null;
        }
    }

    public static boolean writeFile(Context ctx, String filename, String data) {
        try {
            FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
