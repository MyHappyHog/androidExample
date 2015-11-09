package kookmin.hog.example.jsonparseexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.welcome)
    TextView welcome;
    @Bind(R.id.list_of_things)
    ListView list_of_things;

    // eidt창에 입력된 데이터로 url에 접속하여 html 파일 parse
    // parse 하기 전에 기존에 있던 parse 데이터 clear
    @OnClick(R.id.parse_html)
    void parse_url() {
        data.clear();
        new Thread(getTagData).start();
    }

    private String elementName[] = {"temperature", "humidity", "illumination"};

    // 이건 그냥
    @OnItemClick(R.id.list_of_things)
    void ToastText(AdapterView<?> parent, View view, int position, long id) {
        String split_data[] = data.get(position).split("#");
        Toast.makeText(this, "depth: " + split_data[0] + ", text: " + split_data[1], Toast.LENGTH_SHORT).show();
    }

    private SimpleAdapter adapter;
    private ArrayList<String> data = new ArrayList<>();

    private String url = "http://52.68.82.234:19918/test.json";     // html 파일을 요청할 페이지 url

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
                URL jsonURL = new URL(url);
                int elementData[] = new int[3];

                // xml 데이터를 읽어서 inputstream에 저장
                InputStream in = jsonURL.openStream();
                InputStreamReader isr = new InputStreamReader(in, "utf-8");
                BufferedReader br = new BufferedReader(isr);

                // 데이터 읽음
                String fromServer = "";
                String line;
                while( (line = br.readLine()) != null ) {
                    fromServer += line;
                }

                // string을 jsonobject 만듦
                JSONObject hogData = new JSONObject(fromServer);

                int i = 0;
                for(String tag : elementName) {
                    if(hogData.has(tag)) {
                        elementData[i++] = hogData.getInt(tag);
                    }
                }

                data.add("" + elementData[0] + "#" + elementData[1] + "#" + elementData[2]);
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
