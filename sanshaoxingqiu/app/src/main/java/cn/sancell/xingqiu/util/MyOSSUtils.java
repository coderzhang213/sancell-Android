package cn.sancell.xingqiu.util;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import java.text.SimpleDateFormat;

/**
 * Created by ai11 on 2019/6/24.
 */

public class MyOSSUtils {
    private static MyOSSUtils instance;

    private final String P_BUCKETNAME = "文件夹名字";//（文件夹名字）

    private static OSS oss;

    private static SimpleDateFormat simpleDateFormat;

    public MyOSSUtils() {

    }

    public static MyOSSUtils getInstance() {

        if (instance == null) {

            if (instance == null) {

                return new MyOSSUtils();

            }

        }

        return instance;

    }

    public void getOSs(Context context, String endpoint, String accessKeyId, String accessKeySecret, String securityToken) {

//推荐使用OSSAuthCredentialsProvider。token过期可以及时更新

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId,accessKeySecret,securityToken);

//该配置类如果不设置，会有默认配置，具体可看该类

        ClientConfiguration conf = new ClientConfiguration();

        conf.setConnectionTimeout(15 * 1000);// 连接超时，默认15秒

        conf.setSocketTimeout(15 * 1000);// socket超时，默认15秒

        conf.setMaxConcurrentRequest(5);// 最大并发请求数，默认5个

        conf.setMaxErrorRetry(2);// 失败后最大重试次数，默认2次

        oss = new OSSClient(context, endpoint, credentialProvider,conf);

        if (simpleDateFormat == null) {

            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        }
    }

    /**
     * 上传图片 上传文件
     *
     * @param context       application上下文对象
     * @param ossUpCallback 成功的回调
     * @param bucketName    后台生成的
     * @param objectKey     后台生成的
     * @param imgPath       图片的本地路径
     */

    public void upImage(Context context, final MyOSSUtils.OssUpCallback ossUpCallback, String bucketName, final String objectKey, String imgPath) {


        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, imgPath);

        putObjectRequest.setProgressCallback(new OSSProgressCallback() {


            @Override
            public void onProgress(Object request, long currentSize, long totalSize) {
                ossUpCallback.inProgress(currentSize, totalSize);
            }

        });

        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback() {

            @Override
            public void onSuccess(OSSRequest request, OSSResult result) {
                ossUpCallback.successImg(oss.presignPublicObjectURL(bucketName, objectKey));
            }

            @Override
            public void onFailure(OSSRequest request, ClientException clientException, ServiceException serviceException) {
                ossUpCallback.successImg(null);
            }

        });

    }

    /**
     * 上传图片 上传流
     *
     * @param context       application上下文对象
     * @param ossUpCallback 成功的回调
     * @param bucketName    后台生成的
     * @param objectKey     后台生成的
     * @param imgbyte       图片的byte数组
     */

    public void upImage(Context context, final OssUpCallback ossUpCallback, String bucketName, final String objectKey, byte[] imgbyte) {


        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, imgbyte);

        putObjectRequest.setProgressCallback(new OSSProgressCallback() {

            @Override
            public void onProgress(Object request, long currentSize, long totalSize) {
                ossUpCallback.inProgress(currentSize, totalSize);
            }

        });

        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback() {

            @Override
            public void onSuccess(OSSRequest request, OSSResult result) {
                ossUpCallback.successImg(oss.presignPublicObjectURL(bucketName, objectKey));
            }

            @Override
            public void onFailure(OSSRequest request, ClientException clientException, ServiceException serviceException) {
                ossUpCallback.successImg(null);
            }

        });

    }

    /**
     * 上传视频
     *
     * @param context       application上下文对象
     * @param ossUpCallback 成功的回调
     * @param bucketName    后台生成的
     * @param objectKey     后台生成的
     * @param video_path    视频的本地路径
     */

    public void upVideo(Context context, final MyOSSUtils.OssUpCallback ossUpCallback, String bucketName, final String objectKey, String video_path) {


        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, video_path);

        putObjectRequest.setProgressCallback(new OSSProgressCallback() {

            @Override

            public void onProgress(Object request, long currentSize, long totalSize) {

                ossUpCallback.inProgress(currentSize, totalSize);

            }

        });

        oss.asyncPutObject(putObjectRequest, new OSSCompletedCallback() {


            @Override
            public void onSuccess(OSSRequest request, OSSResult result) {
                ossUpCallback.successVideo(oss.presignPublicObjectURL(bucketName, objectKey));
            }

            @Override
            public void onFailure(OSSRequest request, ClientException clientException, ServiceException serviceException) {
                ossUpCallback.successImg(null);
            }

        });

    }

    public interface OssUpCallback {

        void successImg(String img_url);

        void successVideo(String video_url);

        void inProgress(long progress, long zong);

    }
}
