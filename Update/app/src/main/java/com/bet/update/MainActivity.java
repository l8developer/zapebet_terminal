package com.bet.update;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bet.update.api.APIClient;
import com.bet.update.api.APIInterface;
import com.bet.update.api.APIResponse;
import com.bet.update.api.UpdateResponse;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Button btnBack, btnTryAgain;
    private ProgressBar progressBar;
    private TextView userMessage;
    private LinearLayout layoutDownloading;
    private LinearLayout layoutBack;

    private Boolean install = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutDownloading = (LinearLayout) findViewById(R.id.download_layout);
        userMessage = findViewById(R.id.tv_user_message);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);

        btnBack = findViewById(R.id.btn_back);
        btnTryAgain = findViewById(R.id.btn_try_again);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            state(0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getAppVersion();
                        }
                    }).start();
                }
            }, 500);
        } else {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        deleteOldApp();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getPackageManager();
                String packageName = "com.bet.mpos.debug"; // Pacote do aplicativo de destino
                Intent intent = packageManager.getLaunchIntentForPackage(packageName);

                if (intent != null) {
                    // Abrir o aplicativo de destino
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("startIntentUpdate", "null");
                }
            }
        });

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state(0);
                getAppVersion();
            }
        });

    }
    private void download(String APP_URL) {

        try {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APP_URL));
            request.setTitle("ZapeBet.apk");
            request.setDescription("Downloading");

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "bet-release.apk");

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(true);
            request.setAllowedOverMetered(true);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);

            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            BroadcastReceiver receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadId);
                        Cursor cursor = downloadManager.query(query);
                        if (cursor.moveToFirst()) {
                            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        state(2);
                                    }
                                });

                                Log.d("DownloadManager", "success");
                                if (!install) {
                                    install = true;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    installApp();
                                                }
                                            }).start();
                                        }
                                    }, 1000);
                                }
                            } else if (status == DownloadManager.STATUS_FAILED) {
                                state(1);
                                Log.d("DownloadManager", "failure");
                            }
                        }
//                    downloading(false);
                    }
                }

                private void installApp() {

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/bet-release.apk");

                    if (file.exists()) {
                        Log.d("PATH", file.getPath());
                        String res = SysTester.getInstance().installApp(file.getPath());
                        if (res == "instalado com sucesso") {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    userMessage.setText(res);
                                    state(3);

                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userMessage.setText("Falha -21");
                                state(1);
                            }
                        });

                    }
                }
            };

            registerReceiver(receiver, filter);
        }catch(Exception e){
            Log.e("Download Error", e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            state(0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getAppVersion();
                        }
                    }).start();
                }
            }, 500);
        } else {
            Log.e("", "Permissão negada, a operação não pode ser realizada");
        }
    }

    private void deleteOldApp(){
        String TAG = "deleteOldApp";

        try {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/bet-release.apk");

            if (file.exists()) {
                Boolean deleted = file.delete();
                if (deleted) {
                    Log.d(TAG, "success");
                } else {
                    Log.e(TAG, "failed");
                }
            } else {
                Log.e(TAG, "does not exist");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void state(int state){
        switch (state) {
            //DOWNLOADING
            case 0:
                userMessage.setText("Baixando aplicativo");
                progressBar.setVisibility(View.VISIBLE);
                btnTryAgain.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                break;
            //ERROR
            case 1:
                progressBar.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                break;
            //INSTALL APP
            case 2:
                userMessage.setText("Atualizando aplicativo");
                progressBar.setVisibility(View.VISIBLE);
                break;
            //APP INSTALED
            case 3:
                userMessage.setText("Aplicativo atualizado");
                progressBar.setVisibility(View.GONE);
//                btnBack.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PackageManager packageManager = getPackageManager();
                        String packageName = "com.bet.mpos.debug"; // Pacote do aplicativo de destino
                        Intent intent = packageManager.getLaunchIntentForPackage(packageName);

                        if (intent != null) {
                            // Abrir o aplicativo de destino
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("startIntentUpdate", "null");
                        }
                    }
                },1500);
                break;
            //API ERROR
            case 4:
                userMessage.setText("Falha na conexão");
                btnTryAgain.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    private void getAppVersion() {

        Retrofit retrofit = new APIClient("https://super-admin.pixcred.vc/api/").getClient();

        APIInterface service = retrofit.create(APIInterface.class);
        //val responseCall: Call<Fees> = service.getCustomerFee(Functions.real_to_int(_value.value))
        String serial = Build.SERIAL;
//        serial = "1492203301";
        Pair<String, String> encrypted = Functions.encrypt(
                "{"
                        + "\"serial_number\"" + ":\"" + serial + "\"" +
                        "}");

        final Call<APIResponse> responseCall = service.getVersion(encrypted.first, encrypted.second);

        responseCall.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> APIresponse) {
                final APIResponse response = APIresponse.body();
                if(APIresponse.isSuccessful())
                {
                    Gson gson =  new Gson();
                    UpdateResponse data = gson.fromJson(Functions.decrypt(response.ct, response.iv), UpdateResponse.class);
                    System.out.println(data.toString());
                    if(data != null) {
//                        download(data.link_download);
                        download("https://xas-presave-public.s3.us-west-2.amazonaws.com/app/Pixcred_1.0.3.apk");
                    }
                    else{
                        state(4);
                        Log.e("GetVersion", "error data");
                    }
                }else{
                    state(4);
                    try {
                        ResponseBody error = APIresponse.errorBody();

                        Log.e("GetVersion", error.string() );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("GetVersion",t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}



