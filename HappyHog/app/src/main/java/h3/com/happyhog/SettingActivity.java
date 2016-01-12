package h3.com.happyhog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import butterknife.ButterKnife;

/**
 * Created by ngh1 on 2016-01-12.
 */
public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Animal animal = new Animal(R.mipmap.ic_launcher);
        animal.name = intent.getStringExtra(Define.ANIMAL_NAME);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.setting_layout);
        SettingAdapter settingAdapter = new SettingAdapter(this, animal, intent);

        expandableListView.setAdapter(settingAdapter);
        expandableListView.expandGroup(0);
        expandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        setResult(RESULT_OK, intent);
    }
}
