package chibafes.com.a56thchibafes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * FirstRunActivity
 * Created by llrk on 2017/08/08.
 * 初回起動時の説明等を行う画面
 */


// FirstRunActivityからFragmentへ変更、それに伴う調整
public class FirstRunActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private static final String[] arrFileName = {"tutorial1", "tutorial2", "tutorial3", "tutorial4", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーの表示設定：非表示にする
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // このActivityに関連づけるレイアウトの設定
        setContentView(R.layout.activity_firstrun);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return FirstRunFragment.newInstance(arrFileName[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return arrFileName[position];
            }

            @Override
            public int getCount() {
                return arrFileName.length;
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClickButton(View view) {
        // メインメニューへ遷移する
        Intent intent = new Intent(FirstRunActivity.this, TimeTableActivity.class);
        startActivity(intent);
        // 処理が終わったらこのActivityを破棄する
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                // バックキー押下時の処理
                case KeyEvent.KEYCODE_BACK:
                    // バックキーを押しても何も起きないようにする
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position >= arrFileName.length - 1) {
            // 最後のページを開いたら次の画面へ移動する
            // 現在のミリ秒を保存してユーザIDとして用いる(※重複可能性あり)
            long currentTimeMillis = System.currentTimeMillis();
            Commons.writeLong(this, "UserID", currentTimeMillis);

            // メインメニューへ遷移する
            Intent intent = new Intent(FirstRunActivity.this, MainMenuActivity.class);
            startActivity(intent);
            // 処理が終わったらこのActivityを破棄する
            finish();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static class FirstRunFragment extends Fragment {
        private static final String PAGE = "PAGE";

        public FirstRunFragment() {
        }

        public static FirstRunFragment newInstance(String sFileName) {
            Bundle bundle = new Bundle();
            bundle.putString(PAGE, sFileName);

            FirstRunFragment firstrunFragment = new FirstRunFragment();
            firstrunFragment.setArguments(bundle);

            return firstrunFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            String sFileName = getArguments().getString(PAGE, "");

            View view = inflater.inflate(R.layout.activity_firstrun2, container, false);
            ImageView imageTutorial = (ImageView) view.findViewById(R.id.imageTutorial);

            if (!sFileName.equals("")) {
                Resources resource = getContext().getResources();
                imageTutorial.setImageBitmap(Commons.getResizeBitmapFromId(resource, resource.getIdentifier(sFileName, "drawable", getContext().getPackageName()), Commons.getDisplaySize(getContext())));
            }
            return view;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }
    }

}
