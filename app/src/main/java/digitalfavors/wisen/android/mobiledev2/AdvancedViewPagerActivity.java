package digitalfavors.wisen.android.mobiledev2;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class AdvancedViewPagerActivity extends AppCompatActivity {


    @BindView(R.id.adv_view_pager_title)
    TabLayout viewPagerTitle;

    @BindView(R.id.adv_view_pager)
    ViewPager viewPager;

    private List<Pair<String, Fragment>> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.advanced_view_pager);
        ButterKnife.bind(this);



        fragmentList.add(new Pair<String, Fragment>("First", new WorkFragment()));
        fragmentList.add(new Pair<String, Fragment>("Second", new DemoFragment()));
        fragmentList.add(new Pair<String, Fragment>("Third", new WorkFragment()));
        fragmentList.add(new Pair<String, Fragment>("Fourth", new DemoFragment()));

        AdvViewPagerAdapter advViewPagerAdapter = new AdvViewPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(advViewPagerAdapter);
        viewPagerTitle.setupWithViewPager(viewPager);

    }
}
