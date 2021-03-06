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

package com.jackie.loader;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.jackie.loader.impl.MemoryCache;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载类
 * Created by on 16/5/18.
 *
 * @author Jackie Zhu
 * @version 1.1
 */
public class ImageLoader {
    // 图片缓存
    ImageCache mImageCache = new MemoryCache();
    // 线程池,线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime()
            .availableProcessors());

    public ImageLoader() {
    }

    public void setmImageCache(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

    /**
     * {@code imageView}显示指定{@code url}路径的图片,如果url已经缓存了可以使用缓存中的图片,不需要再
     * 连接网络来下载这个图片,从而节约了流量和时间
     * @param url 图片所在的路径
     * @param imageView 显示指定图片的{@link ImageView}
     */
    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        imageView.setTag(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(url, bitmap);
            }
        });
    }

    /**
     * 从网络中下载图片
     * @param imageUrl 图片的网络路径
     * @return 下载到的图片,如果没有下载到图片返回空
     */
    private Bitmap downloadImage(String imageUrl) {
        Bitmap result = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            result = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
