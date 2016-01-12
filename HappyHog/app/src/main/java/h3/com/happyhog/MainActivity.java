package h3.com.happyhog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.ic_main_animal) ImageView mainAnimalImage;
    @Bind(R.id.main_state_text) TextView mainStateText;
    @Bind(R.id.main_animal_title) TextView mainTitle;
    @Bind(R.id.main_animal_memo) TextView mainMemo;
    @Bind(R.id.drawer) ListView listView;

    @OnClick(R.id.btnHelp)
    public void onClickHelpBtn(View view) {
        Toast.makeText(getApplicationContext(), "Help Button", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnInfo)
    public void onClickInfoBtn(View view) {
        Toast.makeText(getApplicationContext(), "Info Button", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnCamera)
    public void onClickBtnCamera(View view) {
        Toast.makeText(getApplicationContext(), ((View)(view.getParent())).toString(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnSetting)
    public void onClickBtnSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra(Define.ANIMAL_NAME, mainAnimal.name);
        startActivityForResult(intent, Define.ACTIVITY_SETTING);
    }

    private ArrayList<Animal> arrayList = null;
    private AnimalAdapter animalAdapter = null;
    private Animal mainAnimal = null;
    private String url = "http://52.68.82.234:19918/";
    private Thread getInfoThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // make animal list
        makeList();

        // parsing
        getInfoFromServer();
        while (getInfoThread.isAlive());

        // toolbar setting
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        // drawer setting
        animalAdapter = new AnimalAdapter(this, arrayList);
        listView.setAdapter(animalAdapter);

        // main setting
        mainAnimalImage.setImageResource(mainAnimal.img);
        mainStateText.setText("Temp : " + mainAnimal.temperature + ", humid : " + mainAnimal.humidity);
        mainTitle.setText(mainAnimal.name);
        mainMemo.setText(mainAnimal.memo);
    }

    private void makeList() {
        arrayList = new ArrayList<Animal>();
        arrayList.add(mainAnimal = new Animal(R.mipmap.ic_launcher));
        arrayList.add(new Animal(android.R.drawable.ic_dialog_map));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Define.ACTIVITY_SETTING:
                    mainTitle.setText(data.getStringExtra(Define.ANIMAL_NAME));
                    mainAnimal.name = data.getStringExtra(Define.ANIMAL_NAME);

                    break;

                default:
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            // error handling
        }
    }

    private void getInfoFromServer() {
        // parsing
        getInfoThread = new Thread() {
            @Override
            public void run() {
                ArrayList<String> data = new ArrayList<String>();

                try {
                    URL htmlURL = new URL(url);

                    InputStream in = htmlURL.openStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(in, "utf-8");

                    int eventType = xpp.getEventType();
                    String xmlTag = "";

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                xmlTag = xpp.getName();
                                break;

                            case XmlPullParser.TEXT:
                                if (xmlTag.equals("p")) {
                                    data.add(xpp.getText());
                                }
                                break;

                            case XmlPullParser.END_TAG:
                                xmlTag = "";
                                break;
                        }

                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                int len = data.size();
                // line 0 in server : happy hog .. etc
                for (int i = 1; i < len; ++i) {
                    String[] values = data.get(i).split(" ");
                    arrayList.get(i - 1).temperature = values[1]; // temp
                    arrayList.get(i - 1).humidity = values[4]; // humidity
                }
            }
        };

        getInfoThread.start();
    }
}
