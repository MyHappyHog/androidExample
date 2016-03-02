package h3.com.happyhog.Dropbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxIOException;
import com.dropbox.client2.exception.DropboxParseException;
import com.dropbox.client2.exception.DropboxPartialFileException;
import com.dropbox.client2.exception.DropboxServerException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by sloth on 2016-03-02.
 */
public class DownloadFiles extends AsyncTask<Void, Long, String> {

  private Context mContext;
  private final ProgressDialog mDialog;
  private DropboxAPI<?> mApi;
  private DropboxFileInfo fileInfo;
  private Runnable mRunnable;
  private String mPath;
  private Serializable data;

  private ByteArrayOutputStream mBaos;

  private boolean mCanceled;
  private Long mFileLen;
  private String mErrorMsg;

  public DownloadFiles(Context context, Runnable runnable, DropboxAPI<?> api,
                       String dropboxPath, Serializable data) {
    // We set the context this way so we don't accidentally leak activities
    mContext = context.getApplicationContext();
    mRunnable = runnable;

    mApi = api;
    mPath = dropboxPath;
    this.data = data;

    mDialog = new ProgressDialog(context);
    mDialog.setMessage("Downloading Image");
    mDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        mCanceled = true;
        mErrorMsg = "Canceled";

        // This will cancel the getThumbnail operation by closing
        // its stream
        if (mBaos != null) {
          try {
            mBaos.close();
          } catch (IOException e) {
          }
        }
      }
    });

    mDialog.show();
  }

  @Override
  protected String doInBackground(Void... params) {
    try {
      // By creating a request, we get a handle to the putFile operation,
      // so we can cancel it later if we want to
      if (mCanceled) {
        return "";
      }

      List<DropboxAPI.Entry> revList = mApi.revisions(mPath, 1);
      mFileLen = revList.get(0).bytes;
      if (mCanceled) {
        return "";
      }

      mBaos = new ByteArrayOutputStream();
      fileInfo = mApi.getFile(mPath, revList.get(0).rev, mBaos,
                              new ProgressListener() {
                                @Override
                                public long progressInterval() {
                                  // Update the progress bar every half-second or so
                                  return 500;
                                }

                                @Override
                                public void onProgress(long bytes, long total) {
                                  publishProgress(bytes);
                                }
                              });

      String st = new String(mBaos.toByteArray(), "UTF-8");

      Log.i("delvelop", st);

      return st;

    } catch (DropboxUnlinkedException e) {
      // The AuthSession wasn't properly authenticated or user unlinked.
    } catch (DropboxPartialFileException e) {
      // We canceled the operation
      mErrorMsg = "Download canceled";
    } catch (DropboxServerException e) {
      // Server-side exception.  These are examples of what could happen,
      // but we don't do anything special with them here.
      if (e.error == DropboxServerException._304_NOT_MODIFIED) {
        // won't happen since we don't pass in revision with metadata
      } else if (e.error == DropboxServerException._401_UNAUTHORIZED) {
        // Unauthorized, so we should unlink them.  You may want to
        // automatically log the user out in this case.
      } else if (e.error == DropboxServerException._403_FORBIDDEN) {
        // Not allowed to access this
      } else if (e.error == DropboxServerException._404_NOT_FOUND) {
        // path not found (or if it was the thumbnail, can't be
        // thumbnailed)
      } else if (e.error == DropboxServerException._406_NOT_ACCEPTABLE) {
        // too many entries to return
      } else if (e.error == DropboxServerException._415_UNSUPPORTED_MEDIA) {
        // can't be thumbnailed
      } else if (e.error == DropboxServerException._507_INSUFFICIENT_STORAGE) {
        // user is over quota
      } else {
        // Something else
      }
      // This gets the Dropbox error, translated into the user's language
      mErrorMsg = e.body.userError;
      if (mErrorMsg == null) {
        mErrorMsg = e.body.error;
      }
    } catch (DropboxIOException e) {
      // Happens all the time, probably want to retry automatically.
      mErrorMsg = "Network error.  Try again.";
    } catch (DropboxParseException e) {
      // Probably due to Dropbox server restarting, should retry
      mErrorMsg = "Dropbox error.  Try again.";
    } catch (DropboxException e) {
      // Unknown error
      mErrorMsg = "Unknown error.  Try again.";
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return "";
  }

  @Override
  protected void onProgressUpdate(Long... progress) {
    int percent = (int) (100.0 * (double) progress[0] / mFileLen + 0.5);
    mDialog.setProgress(percent);
  }

  @Override
  protected void onPostExecute(String result) {
    mDialog.dismiss();
    if (!result.equals("")) {
      // Set the image now that we have it
      data.deserialize(result);
      new Handler().post(mRunnable);
    } else {
      // Couldn't download it, so show an error
      showToast(mErrorMsg);
    }
  }

  private void showToast(String msg) {
    Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
    error.show();
  }
}
