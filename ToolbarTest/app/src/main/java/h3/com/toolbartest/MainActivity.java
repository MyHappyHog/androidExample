package h3.com.toolbartest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private TestAdapter testAdapter = null;

    @OnClick(R.id.addIconBtn)
    public void addNavigationContents(View view) {
        arrayList.add(new Animal(android.R.drawable.ic_input_add));
        testAdapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.left_drawer)
    public void clickAnimalItem(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), arrayList.get(position).img, Toast.LENGTH_SHORT).show();
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


        testAdapter = new TestAdapter(getApplicationContext(), R.layout.list_item, arrayList);
        listView.setAdapter(testAdapter);
    }
}

class Animal {
    int img;

    public Animal() {

    }

    public Animal(int img) {
        this.img = img;
    }
}

class TestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Animal> arrayList;
    private LayoutInflater layoutInflater;

    public TestAdapter(Context context, int layout, ArrayList<Animal> arrayList) {
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

        viewHolder.animalView.setImageResource((arrayList.get(position)).img);

        return convertView;
    }

    static class ViewHolder {
        /////////////////////////////////////////

        @Bind(R.id.animalImage)
        AnimalView animalView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

class AnimalView extends ImageView {

    public AnimalView(Context context) {
        super(context);
    }

    public AnimalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }
}