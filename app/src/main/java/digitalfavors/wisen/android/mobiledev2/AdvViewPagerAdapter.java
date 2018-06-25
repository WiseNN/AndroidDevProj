package digitalfavors.wisen.android.mobiledev2;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;

import java.util.List;

public class AdvViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Pair<String, Fragment>> fragmentList;

    public AdvViewPagerAdapter(FragmentManager fm, List<Pair<String, Fragment>> fragmentList) {
        super(fm);

        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position).second;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).first;
    }
}
