package h3.com.toolbartest;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    
    @Bind(R.id.left_drawer)
    ListView listView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ArrayList<Animal> arrayList = new ArrayList<>();
    private AnimalAdapter animalAdapter = null;

    @OnClick(R.id.addIconBtn)
    public void addNavigationContents(View view) {
        arrayList.add(new Animal(android.R.drawable.ic_input_add));
        animalAdapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.left_drawer)
    public void clickAnimalItem(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "position : " + position, Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        arrayList.add(new Animal(android.R.drawable.ic_dialog_alert));
        arrayList.add(new Animal(android.R.drawable.ic_dialog_dialer));
        arrayList.add(new Animal(android.R.drawable.ic_dialog_email));
        arrayList.add(new Animal(android.R.drawable.ic_dialog_info));
        arrayList.add(new Animal(android.R.drawable.ic_dialog_map));


        animalAdapter = new AnimalAdapter(getApplicationContext(), R.layout.list_item, arrayList);
        listView.setAdapter(animalAdapter);
    }
}