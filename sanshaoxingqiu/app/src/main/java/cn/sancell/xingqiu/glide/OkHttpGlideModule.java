package cn.sancell.xingqiu.glide;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;


/**
 * Created by zj on 2019/7/5.
 */
@GlideModule
public class OkHttpGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        //if (!Config.bIsDebug) {
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        //OkHttpClient client = RestClientBuilder.buildClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
        //  }

    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
