package h3.com.happyhog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import h3.com.happyhog.Dropbox.H3Dropbox;

public class MainActivity extends AppCompatActivity {

  private static final String APP_KEY = "07nj8f7mehl6tvi";
  private static final String APP_SECRET = "07nj8f7mehl6tvi";

  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @Bind(R.id.ic_main_animal)
  ImageView mainAnimalImage;
  @Bind(R.id.main_state_text)
  TextView mainStateText;
  @Bind(R.id.main_animal_title)
  TextView mainTitle;
  @Bind(R.id.main_animal_memo)
  TextView mainMemo;
  @Bind(R.id.drawer)
  ListView listView;

  @OnClick(R.id.btnHelp)
  public void onClickHelpBtn(View view) {
    Toast.makeText(getApplicationContext(), "Help Button", Toast.LENGTH_SHORT).show();
    animalDatabase.clear();
    makeDrawerList();
    animalAdapter.notifyDataSetChanged();
  }

  @OnClick(R.id.btnInfo)
  public void onClickInfoBtn(View view) {
    Toast.makeText(getApplicationContext(), "Info Button", Toast.LENGTH_SHORT).show();
    Log.d("WHY : ", animalDatabase.toString());

    H3Dropbox dBox = H3Dropbox.getInstance();
    Log.i("logged in", "" + dBox.getLoggedIn());
    if (!dBox.getLoggedIn()) {
      H3Dropbox.getInstance().startOAuth2Authentication(MainActivity.this);
    }
  }

  @OnClick(R.id.btnCamera)
  public void onClickBtnCamera(View view) {
    Toast.makeText(getApplicationContext(), ((View) (view.getParent())).toString(), Toast.LENGTH_SHORT).show();
  }

  @OnClick(R.id.btnSetting)
  public void onClickBtnSetting(View view) {
    Intent intent = new Intent(this, SettingActivity.class);
    intent.putExtra(Define.INTENT_ANIMAL, mainAnimal);
    startActivityForResult(intent, Define.ACTIVITY_SETTING);
  }

  @OnClick(R.id.drawer_add_btn)
  public void onClickBtnAdd(View view) {
    Toast.makeText(getApplicationContext(), "add ... ", Toast.LENGTH_SHORT).show();
  }

  private Animal mainAnimal = null;
  private ArrayList<Animal> arrayList = null;
  private AnimalAdapter animalAdapter = null;

  private AnimalDatabase animalDatabase = null;

  public Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      mainAnimalImage.setImageResource(mainAnimal.getImg());
      mainStateText.setText(mainAnimal.getState());
      mainTitle.setText(mainAnimal.getName());
      mainMemo.setText(mainAnimal.getMemo());
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // dropbox session setup
    AndroidAuthSession session = buildSession();
    H3Dropbox.getInstance().createDropboxApi(session);
    checkAppKeySetup();

    // make animal list
    makeDrawerList();
    //animalDatabase.clear();

    // store sensing data
    H3Dropbox.getInstance().downloadFiles(MainActivity.this, mRunnable, "/1AFE34F4B48D/SensingInfo.json", mainAnimal.getSensingInfo());

    // toolbar setting
    toolbar.setTitle(" ");
    setSupportActionBar(toolbar);

    // drawer setting
    animalAdapter = new AnimalAdapter(this, arrayList);
    listView.setAdapter(animalAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("------------fuck", "" + position);
        Toast.makeText(getApplicationContext(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
      }
    });

    // main setting
    mainAnimalImage.setImageResource(mainAnimal.getImg());
    mainStateText.setText(mainAnimal.getState());
    mainTitle.setText(mainAnimal.getName());
    mainMemo.setText(mainAnimal.getMemo());

    // test
  }

  @Override
  protected void onResume() {
    super.onResume();

    H3Dropbox dBox = H3Dropbox.getInstance();
    // The next part must be inserted in the onResume() method of the
    // activity from which session.startAuthentication() was called, so
    // that Dropbox authentication completes properly.
    if (dBox.authSuccessful()) {
      try {
        // Mandatory call to complete the auth
        dBox.finishAuth();

        // Store it locally in our app for later use
        storeAuth();
      } catch (IllegalStateException e) {
        showToast("Couldn't authenticate with Dropbox:" + e.getLocalizedMessage());
        Log.i("logged in", "Error authenticating", e);
      }
    }
  }

  private void makeDrawerList() {
    animalDatabase = AnimalDatabase.getInstance(this);

    if (animalDatabase.isAnimalEmpty()) {
      animalDatabase.addAnimal(new Animal());
      animalDatabase.addAnimal(new Animal());
      arrayList = animalDatabase.getAnimalsList();
    }

    arrayList = animalDatabase.getAnimalsList();
    mainAnimal = arrayList.get(0);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case Define.ACTIVITY_SETTING:
          mainAnimal = (Animal) data.getParcelableExtra(Define.INTENT_ANIMAL);
          animalDatabase.updateAnimal(mainAnimal);
          arrayList = animalDatabase.getAnimalsList();
          animalAdapter.notifyDataSetChanged();

          mainTitle.setText(mainAnimal.getName());
          mainMemo.setText(mainAnimal.getMemo());
          mainStateText.setText(mainAnimal.getState());
          mainAnimalImage.setImageResource(mainAnimal.getImg());

          Log.d("FUCK-----", animalDatabase.toString());
          break;

        default:
      }
    } else if (resultCode == RESULT_CANCELED) {
      // error handling
    }

  }

  /**
   * Shows keeping the access keys returned from Trusted Authenticator in a local store, rather than storing user name & password, and
   * re-authenticating each time (which is not to be done, ever).
   */
  private boolean loadAuth(AndroidAuthSession session) {
    SharedPreferences prefs = getSharedPreferences(Define.ACCOUNT_PREFS_NAME, 0);
    String key = prefs.getString(Define.ACCESS_KEY_NAME, null);
    String secret = prefs.getString(Define.ACCESS_SECRET_NAME, null);
      if (key == null || secret == null || key.length() == 0 || secret.length() == 0) {
          return false;
      }

    if (key.equals("oauth2:")) {
      // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
      session.setOAuth2AccessToken(secret);
    } else {
      // Still support using old OAuth 1 tokens.
      session.setAccessTokenPair(new AccessTokenPair(key, secret));
    }

    return true;
  }

  /**
   * Shows keeping the access keys returned from Trusted Authenticator in a local
   * store, rather than storing user name & password, and re-authenticating each
   * time (which is not to be done, ever).
   */
  private void storeAuth() {
    // Store the OAuth 2 access token, if there is one.
    String oauth2AccessToken = H3Dropbox.getInstance().getAccessToken();
    Log.d("logged in", oauth2AccessToken);

    if (oauth2AccessToken != null) {
      SharedPreferences prefs = getSharedPreferences(Define.ACCOUNT_PREFS_NAME, 0);
      SharedPreferences.Editor edit = prefs.edit();
      edit.putString(Define.ACCESS_KEY_NAME, "oauth2:");
      edit.putString(Define.ACCESS_SECRET_NAME, oauth2AccessToken);
      edit.commit();
    }
  }

  private AndroidAuthSession buildSession() {
    AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

    AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
    loadAuth(session);
    return session;
  }

  private void checkAppKeySetup() {

    // Check if the app has set up its manifest properly.
    Intent testIntent = new Intent(Intent.ACTION_VIEW);
    String scheme = "db-" + APP_KEY;
    String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
    testIntent.setData(Uri.parse(uri));
    PackageManager pm = getPackageManager();
    if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
      showToast("URL scheme in your app's " +
                "manifest is not set up correctly. You should have a " +
                "com.dropbox.client2.android.AuthActivity with the " +
                "scheme: " + scheme);
      finish();
    }
  }

  private void showToast(String msg) {
    Toast error = Toast.makeText(this, msg, Toast.LENGTH_LONG);
    error.show();
  }
}