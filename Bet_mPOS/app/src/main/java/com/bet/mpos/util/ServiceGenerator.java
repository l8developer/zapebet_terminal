package com.bet.mpos.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ServiceGenerator {

    public OkHttpClient GenerateUnsafeOkHttpClient(){
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public OkHttpClient GenerateSecureOkHttpClient(Context context) {
//         OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
//                 .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS);
//
//         try{
//             CertificateFactory cf = CertificateFactory.getInstance("X.509");
//             InputStream caInput = context.getResources().openRawResource(R.raw.pixcredvc);
////             Certificate ca = cf.generateCertificate(caFileInputStream);
////             InputStream caInput = new BufferedInputStream(new FileInputStream("maquininha-MVP/app/src/main/res/layouts/activities/raw/pixcredvc.crt"));
//             Certificate ca;
//             try {
//                 ca = cf.generateCertificate(caInput);
//                 System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//             }finally {
//                 caInput.close();
//             }
//
//             String keyStoreType = KeyStore.getDefaultType();
//             KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//             keyStore.load(null, null);
//             keyStore.setCertificateEntry("ca", ca);
//
//             String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//             TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//             tmf.init(keyStore);
//
//             SSLContext sslContext = SSLContext.getInstance("TLS");
////             sslContext.init( tmf.getTrustManagers(), tmf.getTrustManagers(), null);
//
//             return httpClientBuilder
//                     .sslSocketFactory(sslContext.getSocketFactory())
//                     .build();
//         }catch(Exception e){
//            return null;
////         }
//
//    }
}
