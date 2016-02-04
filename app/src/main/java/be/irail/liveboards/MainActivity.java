package be.irail.liveboards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.materialviewpager.MaterialViewPager;


public class MainActivity extends AppCompatActivity {
    public MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        ViewPager viewPager = mViewPager.getViewPager();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        mViewPager.getPagerTitleStrip().setViewPager(viewPager);

    }


    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return StationFragment.newInstance();
                case 1:
                    return TrainFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_station);
                case 1:
                    return getString(R.string.title_train);
                default:
                    return "";
            }
        }
    }
}
