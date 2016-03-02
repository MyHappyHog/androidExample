package h3.com.happyhog.Dropbox;

import android.content.Context;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

/**
 * Created by sloth on 2016-02-26.
 */
public class H3Dropbox {

  DropboxAPI<AndroidAuthSession> mApi;

  private H3Dropbox() { }

  private static class Singleton {
    private static final H3Dropbox instance = new H3Dropbox();
  }

  public static H3Dropbox getInstance() {
   return Singleton.instance;
  }

  public void startOAuth2Authentication(Context context) {
    mApi.getSession().startOAuth2Authentication(context);
  }

  public boolean authSuccessful() {
    return mApi.getSession().authenticationSuccessful();
  }

  public void finishAuth() {
    mApi.getSession().finishAuthentication();
  }

  public String getAccessToken() {
    return mApi.getSession().getOAuth2AccessToken();
  }

  public boolean getLoggedIn() {
    return mApi.getSession().isLinked();
  }

  public void createDropboxApi(AndroidAuthSession session) {
    mApi = new DropboxAPI<AndroidAuthSession>(session);
  }

  public void updateFiles(Context context, String filePath, String fileContent) {
    UploadString upload = new UploadString(context, mApi, filePath, fileContent);
    upload.execute();
  }

  public void downloadFiles(Context context, Runnable runnable, String filePath, Serializable data) {
    DownloadFiles download = new DownloadFiles(context, runnable, mApi, filePath, data);
    download.execute();
  }
}