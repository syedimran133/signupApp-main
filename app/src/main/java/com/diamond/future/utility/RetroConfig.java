package com.diamond.future.utility;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroConfig {

    private static RequestApi requestAPI;
    public static final int REQUEST_TIEMOUT = 60;


    public static RequestApi getClient(Context context) {
        Retrofit retrofit = null;
        final PreManager preManager=new PreManager(context);

        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .callTimeout(REQUEST_TIEMOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIEMOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIEMOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(httpLoggingInterceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization","Bearer " + preManager.getAuthToken()).build();
                return chain.proceed(request);
            }
        });
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.MAIN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        requestAPI = retrofit.create(RequestApi.class);
        return requestAPI;
    }
    public static RequestApi getClient(Context context, int get) {
        Retrofit retrofit = null;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        final PreManager preManager=new PreManager(context);
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder().addHeader("APP_KEY", "8447126401").build();
//                return chain.proceed(request);
//            }
//        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://skymcx.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient())
                .build();
        requestAPI = retrofit.create(RequestApi.class);
        return requestAPI;
    }
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//            if (BuildConfig.DEBUG)
//            {
//                HttpLoggingInterceptor looger=new HttpLoggingInterceptor();
//                looger.setLevel(HttpLoggingInterceptor.Level.BODY);
//                builder.addInterceptor(looger);
//
//            }
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
