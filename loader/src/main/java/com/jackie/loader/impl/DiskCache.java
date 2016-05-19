/*
 *    Copyright 2016 The Open Source Project of Jackie Zhu
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *             $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
 *             $                                                   $
 *             $                       _oo0oo_                     $
 *             $                      o8888888o                    $
 *             $                      88" . "88                    $
 *             $                      (| -_- |)                    $
 *             $                      0\  =  /0                    $
 *             $                    ___/`-_-'\___                  $
 *             $                  .' \\|     |$ '.                 $
 *             $                 / \\|||  :  |||$ \                $
 *             $                / _||||| -:- |||||- \              $
 *             $               |   | \\\  -  $/ |   |              $
 *             $               | \_|  ''\- -/''  |_/ |             $
 *             $               \  .-\__  '-'  ___/-. /             $
 *             $             ___'. .'  /-_._-\  `. .'___           $
 *             $          ."" '<  `.___\_<|>_/___.' >' "".         $
 *             $         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       $
 *             $         \  \ `_.   \_ __\ /__ _/   .-` /  /       $
 *             $     =====`-.____`.___ \_____/___.-`___.-'=====    $
 *             $                       `=-_-='                     $
 *             $     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   $
 *             $                                                   $
 *             $          Buddha bless         Never BUG           $
 *             $                                                   $
 *             $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
 */

package com.jackie.loader.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.jackie.loader.CloseUtils;
import com.jackie.loader.ImageCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 对{@link ImageCache}的实现类,把网络路径对应的图片缓存到本地存储器当中
 * Created by on 16/5/19.
 *
 * @author Jackie Zhu
 * @version 1.0
 */
public class DiskCache implements ImageCache {
    String cacheDir = "/sdcard/picture/";

    @Override
    public Bitmap put(String url, Bitmap bitmap) {
        int index = url.lastIndexOf(File.separator);
        String name = url.substring(index + 1);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheDir + url);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(fileOutputStream);
        }
        return bitmap;
    }

    @Override
    public Bitmap get(String url) {
        Bitmap result = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(cacheDir + url);
            result = BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(fileInputStream);
        }
        return result;
    }
}
