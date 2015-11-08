package kookmin.hog.example.htmlparseexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.welcome) TextView welcome;
    @Bind(R.id.list_of_things) ListView list_of_things;
    @Bind(R.id.target_tag) EditText target_tag;
    @Bind(R.id.edit_url) EditText edit_url;

    // eidt창에 입력된 데이터로 url에 접속하여 html 파일 parse
    // parse 하기 전에 기존에 있던 parse 데이터 clear
    @OnClick(R.id.parse_html)
    void parse_url() {
        url = "http://" + edit_url.getText().toString();
        tagName = target_tag.getText().toString();

        edit_url.setText("");
        target_tag.setText("");

        data.clear();
        new Thread(getTagData).start();
    }

    // 이건 그냥
    @OnItemClick(R.id.list_of_things)
    void ToastText(AdapterView<?> parent, View view, int position, long id) {
        String split_data[] = data.get(position).split("#");
        Toast.makeText(this, "depth: " + split_data[0] + ", text: " + split_data[1], Toast.LENGTH_SHORT).show();
    }

    private SimpleAdapter adapter;
    private ArrayList<String> data = new ArrayList<>();

    private String tagName = ""; // parse 할 태그 이름
    private String url = "";     // html 파일을 요청할 페이지 url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        adapter = new SimpleAdapter(getApplicationContext(), data);
        list_of_things.setAdapter(adapter);

        welcome.setText("hello Html Parse Test");
    }

    Runnable getTagData = new Runnable() {
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
                    Log.d("mytag", "" + xpp.getName() + " " + xpp.getText());

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            // 시작태그를 만나면 태그의 이름 저장
                            xmlTag = xpp.getName();
                            break;
                        case XmlPullParser.TEXT:
                            // 태그의 텍스트 부분을 만나면 타겟 태그와 같은지 확인
                            if (xmlTag.equals(tagName))
                                data.add(xpp.getDepth() + "#" + xpp.getText());
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

            // main 쓰레드 이외에는 뷰를 건드릴 수 없으므로..
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };
}
