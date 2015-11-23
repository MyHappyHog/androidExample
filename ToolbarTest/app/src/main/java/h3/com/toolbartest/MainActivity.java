package h3.com.toolbartest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

    private ArrayList<TestItem> arrayList = new ArrayList<>();
    private TestAdapter testAdapter = null;

    @OnClick(R.id.addIconBtn)
    public void addNavigationContents(View view) {
        arrayList.add(new TestItem(android.R.drawable.ic_input_add, "ic_input_add_" + (arrayList.size() + 1)));
        testAdapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.left_drawer)
    public void clickAnimalItem(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), arrayList.get(position).text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        arrayList.add(new TestItem(android.R.drawable.ic_dialog_alert, "ic_dialog_alert"));
        arrayList.add(new TestItem(android.R.drawable.ic_dialog_dialer, "ic_dialog_dialer"));
        arrayList.add(new TestItem(android.R.drawable.ic_dialog_email, "ic_dialog_email"));
        arrayList.add(new TestItem(android.R.drawable.ic_dialog_info, "ic_dialog_info"));
        arrayList.add(new TestItem(android.R.drawable.ic_dialog_map, "ic_dialog_map"));

        testAdapter = new TestAdapter(getApplicationContext(), R.layout.list_item, arrayList);
        listView.setAdapter(testAdapter);
    }
}

class TestItem {
    int img;
    String text;

    public TestItem() {

    }

    public TestItem(int img, String text) {
        this.img = img;
        this.text = text;
    }
}

class TestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<TestItem> arrayList;
    private LayoutInflater layoutInflater;

    public TestAdapter(Context context, int layout, ArrayList<TestItem> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.imageView.setImageResource((arrayList.get(position)).img);
        viewHolder.textView.setText((arrayList.get(position)).text);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.testIcon)
        ImageView imageView;

        @Bind(R.id.testText)
        TextView textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
