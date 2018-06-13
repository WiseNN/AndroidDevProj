package digitalfavors.wisen.android.mobiledev2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MyViewPager extends FragmentActivity
{
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int mPageCount = 2;
//    private List<Fragment> fragList;


//    MyViewPager(List<Fragment> fragList)
//    {
////        this.fragList = fragList;
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.view_pager_activity);
        //get current cell selection
        int position = getIntent().getIntExtra("pagePosition", -99);

        //change amount of pages based on cell index number
        switch (position)
        {
            case 1 : mPageCount = 2;
                break;
            case 2 : mPageCount = ImageView.ScaleType.values().length;
        }
        mPager = findViewById(R.id.my_view_pager);
        mPagerAdapter = new ScreenPageSlider(getSupportFragmentManager(),position);


        mPager.setAdapter(mPagerAdapter);

    }






    public class ScreenPageSlider extends FragmentStatePagerAdapter {


        int cellPosition;
        public ScreenPageSlider(FragmentManager fm, int cellPosition) {
            super(fm);

            this.cellPosition = cellPosition;
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

        @Override
        public Fragment getItem(int position) {

            switch (cellPosition)
            {
                case 1: {
                    switch (position)
                    {
                        case 0 : return new DemoFragment();
                        case 1 : return new WorkFragment();
                    }
                }
                case 2 : {
                    PicFragment picFrag = new PicFragment();
                    picFrag.pagePosition = position;
                    return picFrag;
                }

            }

        return null;
        }


    }
}
