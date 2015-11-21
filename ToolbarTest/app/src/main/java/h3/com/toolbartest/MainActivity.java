package h3.com.toolbartest;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    
    @Bind(R.id.left_drawer)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] numbers = { "1", "2", "3", "44" };

        //ListView listView = (ListView)findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.item999, numbers));
        
    }


}
