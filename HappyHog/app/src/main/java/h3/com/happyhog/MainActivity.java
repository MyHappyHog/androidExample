package h3.com.happyhog;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.ic_main_animal) ImageView mainAnimalImage;

    // ActionBarDrawerToggle actionBarDrawerToggle;

    @OnClick(R.id.btnHelp)
    public void OnClickHelpBtn(View view) {
        Toast.makeText(getApplicationContext(), "Help Button", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnInfo)
    public void OnClickInfoBtn(View view) {
        Toast.makeText(getApplicationContext(), "Info Button", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btnCamera)
    public void OnClickBtnCamera(View view) {
        Toast.makeText(getApplicationContext(), ((View)(view.getParent())).toString(), Toast.LENGTH_SHORT).show();

        /*

        switch (((View)(view.getParent().getParent())).getId()) {
            case R.id.container_layout:
                Toast.makeText(getApplicationContext(), "container camera button", Toast.LENGTH_SHORT).show(); break;

            case R.id.drawer:
                Toast.makeText(getApplicationContext(), "drawer camera button", Toast.LENGTH_SHORT).show(); break;

            default:
                break;
        }
         */
    }

    private ArrayList<Animal> arrayList = null;
    private AnimalAdapter animalAdapter = null;
    private ListView listView = null;
    private Animal mainAnimal = null;
    private String url = "http://52.68.82.234:19918/";
    private ArrayList<String> data = new ArrayList<String>();

    private void makeList() {
        arrayList = new ArrayList<Animal>();
        arrayList.add(mainAnimal = new Animal(R.mipmap.ic_launcher));
        arrayList.add(new Animal(android.R.drawable.ic_dialog_map));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // parsing
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    URL htmlURL = new URL(url);

                    // xml 데이터를 읽어서 inputstream에 저장
                    InputStream in = htmlURL.openStream();

                    // XmlPullParserFactory 인스턴스 가져옴
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                    // XmlPullParser 인스턴스 생성
                    XmlPullParser xpp = factory.newPullParser();

                    // XmlPullParser에 스트림을 이어줌. charset은 utf-8을 사용
                    xpp.setInput(in, "utf-8");

                    // xpp 의 현재 타입을 가져옴.
                    int eventType = xpp.getEventType();

                    String xmlTag = "";
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                // 시작태그를 만나면 태그의 이름 저장
                                xmlTag = xpp.getName();
                                break;
                            case XmlPullParser.TEXT:
                                // 태그의 텍스트 부분을 만나면 타겟 태그와 같은지 확인
                                if (xmlTag.equals("p")) {
                                    data.add(xpp.getText());
                                    Log.d("FUCK", xpp.getText());
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                // 태그의 끝을 만나면 tag 이름 초기화
                                xmlTag = "";
                                break;
                        }

                        // 다음 태그로
                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String temp = data.get(1);
                String[] temps = temp.split(" ");
                data.clear();
                data.add(temps[1]);
                data.add(temps[4]);
                data.add(temps[7]);
            }
        };

        thread.start();

        // toolbar setting
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);

        // drawer setting
        makeList();
        while (thread.isAlive());
        animalAdapter = new AnimalAdapter(this, arrayList, data);
        listView = (ListView)findViewById(R.id.drawer);
        listView.setAdapter(animalAdapter);

        // main setting
        mainAnimalImage.setImageResource(mainAnimal.img);


        // navigation drawer toggle
        /*
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        */
    }

    // navigation drawer toggle
    /*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
