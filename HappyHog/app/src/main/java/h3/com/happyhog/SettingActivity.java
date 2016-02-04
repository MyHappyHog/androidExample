package h3.com.happyhog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ngh1 on 2016-01-12.
 */
public class SettingActivity extends Activity {
    @Bind(R.id.main_setting_layout)
    LinearLayout mainSettingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        Animal animal = (Animal) intent.getParcelableExtra(Define.INTENT_ANIMAL);

        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.setting_layout);
        SettingAdapter settingAdapter = new SettingAdapter(this, animal, intent);

        expandableListView.setAdapter(settingAdapter);
        expandableListView.expandGroup(0);
        expandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        setResult(RESULT_OK, intent);
    }
}
