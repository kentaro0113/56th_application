package com.chibafes.chibafes56;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;

import java.util.ArrayList;

/**
 * Created by shiho on 2017/09/01.
 */

// 0903_kimura:ActivityからFragmentへ変更、それに伴う調整
public class HappiActivity extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.activity_happi,container, false);

        setHappiSerif(view, Statics.HAPPI_SERIF_NORMAL);

        ImageButton buttonHappi = (ImageButton) view.findViewById(R.id.SerifButton);
        buttonHappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHappiSerif(view, Statics.HAPPI_SERIF_TAP);
            }
        });

        ImageButton buttonProfile = (ImageButton) view.findViewById(R.id.imageButton18);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HappiProfileActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonGacha = (ImageButton) view.findViewById(R.id.imageButton19);
        buttonGacha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HappiGachaActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonCollection = (ImageButton) view.findViewById(R.id.imageButton20);
        buttonCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HappiCollectionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setHappiSerif(View view, int nType) {

        int i;
        ArrayList<String> arraySerif = new ArrayList<>();

        // セリフ情報を読み込み
        PList listTalk = Commons.getParsedPlist(getActivity(), "HappiTalk.plist");
        Array list = (Array) listTalk.getRootElement();

        Dict dic;

        switch(nType) {
            /*
            case Statics.HAPPI_SERIF_FIRST:
                // 起動後最初のセリフは専用セリフのみとする
                // TODO:企画前日からは企画の時間を固定表示させる?
                dic = (Dict) list.get(Statics.HAPPI_SERIF_FIRST);
                setSerif(arraySerif, dic);
                break;
                */
            case Statics.HAPPI_SERIF_TAP:
                // タップ時は通常セリフも含む
                dic = (Dict) list.get(Statics.HAPPI_SERIF_TAP);
                setSerif(arraySerif, dic);
                //
            case Statics.HAPPI_SERIF_NORMAL:
                // 通常時のセリフ
                dic = (Dict) list.get(Statics.HAPPI_SERIF_NORMAL);
                setSerif(arraySerif, dic);

                break;
        }

        // セリフの抽選
        TextView textSerif = (TextView) view.findViewById(R.id.Happi_line);
        textSerif.setText(arraySerif.get(Commons.getRandom(arraySerif.size())));

    }

    private void setSerif(ArrayList<String> arraySerif, Dict dic) {
        Array list = dic.getConfigurationArray("serif");
        for(int i = 0; i < list.size(); ++i) {
            arraySerif.add(((com.longevitysoft.android.xml.plist.domain.String)list.get(i)).getValue());
        }
    }

}

