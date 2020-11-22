package cn.yb.datawaiter.crawler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okhttp3.Request;

public class OkHttpClientUtils {
    static OkHttpClientUtils util;
    public static OkHttpClient client;
    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();
    }
    private OkHttpClientUtils() {

    }

    //单例的方法
    public static OkHttpClientUtils getInstance() {
        if (util == null) {
            synchronized (OkHttpClientUtils.class) {
                if (util == null) {
                    util = new OkHttpClientUtils();
                }
            }
        }
        return util;
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    /**
     * post 的构建模式
     * post 的构建模式
     * 如果 gson 为空， 是再 tool里面拿的gson
     */
    public static class PostBuild {

        String url;
        Map<String, Object> paramterMap = new HashMap<>();
        Map<String, String> headparamterMap = new HashMap<>();
        Map<String, File> fileMap = new HashMap<>();


        public PostBuild(String url) {
            this.url = url;
        }


        public PostBuild addParameter(String mark, Object obj) {
            paramterMap.put(mark, obj);
            return this;
        }

        public PostBuild addHead(String key, String value) {
            headparamterMap.put(key,value);
            return this;
        }
        public PostBuild addFile(String fileName, File file) {
            fileMap.put(fileName, file);
            return this;
        }
        Request request;
        public Request build() {
            Request.Builder builder = new Request.Builder();
            for (String headKey : headparamterMap.keySet()){
                builder.addHeader(headKey,headparamterMap.get(headKey));
            }
            if (fileMap.size() == 0) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilderAdd(formBodyBuilder);
                FormBody formBody = formBodyBuilder.build();
                builder
                        .addHeader("Content-Type", "application/json")
                        .post(formBody);
            } else {
                MultipartBody.Builder requestBody = new MultipartBody.Builder();
                formBodyBuilderAdd(requestBody);
                builder.post(requestBody.build());
            }
            request =builder.url(url)
                    .build();
            return request;
        }

        private String getMapJson() {
            JSONObject jsonObject = new JSONObject();
            for (String key : paramterMap.keySet()) {
                Object obj = paramterMap.get(key);
                if (obj instanceof String) {
                    jsonObject.put(key, (String) obj);
                } else {
                    jsonObject.put(key, obj);
                }
            }
            return jsonObject.toJSONString();
        }




        /**
         * 装载数据
         *
         * @param build
         */
        private void formBodyBuilderAdd(FormBody.Builder build) {

            for (String key : paramterMap.keySet()) {
                Object obj = paramterMap.get(key);
                if (obj instanceof String) {
                    build.add(key, (String) obj);
                } else {
                    build.add(key, JSONObject.toJSONString(obj));
                }
            }
        }

        /**
         * 装载数据和文件
         *
         * @param build
         */
        private void formBodyBuilderAdd(MultipartBody.Builder build) {

            build.setType(MultipartBody.FORM);
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
            for (String key : paramterMap.keySet()) {
                Object obj = paramterMap.get(key);
                if (obj instanceof String) {
                    build.addFormDataPart(key, (String) obj);

                } else {
                    build.addFormDataPart(key, JSONObject.toJSONString(obj));
                }

            }
            for (String key : fileMap.keySet()) {
                File file = fileMap.get(key);
                build.addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"Filedata\"; filename=\"" + file.getName() + "\""), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
                //build.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file));
            }
        }


    }


}

