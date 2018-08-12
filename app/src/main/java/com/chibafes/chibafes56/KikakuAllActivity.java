package com.chibafes.chibafes56;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chibafes.a56thchibafes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

;

/**
 * Created by steee on 2017/09/01.
 */

// 0903_kimura:ActivityからFragmentへ変更、それに伴う調整
public class KikakuAllActivity extends Fragment {
    private LinearLayout viewSearch;
    private FrameLayout viewFavorite;
    private Button buttonTabSearch;
    private Button buttonTabFavorite;
    private Button buttonSearch;
    private ListView tableSearch;
    private ListView tableFavorite;
    private TextView viewNoFavorite;
    private AlertDialog alertSearch = null;
    private AlertDialog alertDetail = null;
    private EditText editFreeWord;
    private ScrollView scrollSearch;

    private static int BUTTON_KIKAKU_TAB_SEARCH = 0;
    private static int BUTTON_KIKAKU_TAB_FAVORITE = 1;

    private static int BUTTON_SEARCH_DAY1 = 0;
    private static int BUTTON_SEARCH_DAY2 = 1;
    private static int BUTTON_SEARCH_DAY3 = 2;
    private static int BUTTON_SEARCH_DAY4 = 3;

    private static int BUTTON_SEARCH_GENRE1 = 4;
    private static int MAX_GENRE = 8;

    private static int BUTTON_SEARCH_TYPE1 = 12;
    private static int MAX_TYPE = 5;
    private static int MAX_OPTION = 17;

    private ArrayList<KikakuItem> arraySearchData;
    private ArrayList<Integer> arrayFavorite;
    private ArrayList<KikakuItem> arrayFavoriteData;
    private String sSearchWord;
    private KikakuItem[] arrayKikakuData;

    private boolean[] arrayOption;
    private boolean bFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_kikakusearch, container, false);

        viewSearch = (LinearLayout) view.findViewById(R.id.viewSearch);
        viewFavorite = (FrameLayout) view.findViewById(R.id.viewFavorite);
        buttonTabSearch = (Button) view.findViewById(R.id.buttonTabSearch);
        buttonTabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchTab(BUTTON_KIKAKU_TAB_SEARCH);
            }
        });
        buttonTabFavorite = (Button) view.findViewById(R.id.buttonTabFavorite);
        buttonTabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchTab(BUTTON_KIKAKU_TAB_FAVORITE);
            }
        });
        buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSearch.show();
                scrollSearch.scrollTo(0, 0);
            }
        });
        Point displaySize = Commons.getDisplaySize(getContext());
        int nTabImageWidth = (displaySize.x - 64) / 2;
        int nTabImageHeight = nTabImageWidth / 4;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buttonTabSearch.getLayoutParams();
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonTabSearch.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) buttonTabFavorite.getLayoutParams();
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonTabFavorite.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) buttonSearch.getLayoutParams();
        nTabImageWidth = displaySize.x - 32;
        nTabImageHeight = nTabImageWidth / 6;
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonSearch.setLayoutParams(params);

        tableSearch = (ListView) view.findViewById(R.id.tableSearch);
        tableFavorite = (ListView) view.findViewById(R.id.tableFavorite);
        viewNoFavorite = (TextView) view.findViewById(R.id.viewNoFavorite);

        arraySearchData = null;
        String sKikaku = Commons.readString(getContext(), "data_kikaku");
        if(sKikaku != null) {
            try {
                JSONArray jsonArray = new JSONArray(sKikaku);
                arrayKikakuData = new KikakuItem[jsonArray.length()];
                for(int i = 0; i < jsonArray.length(); ++i) {
                    arrayKikakuData[i] = new KikakuItem();
                    arrayKikakuData[i].setData(jsonArray.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        arrayFavorite = getFavorite();
        setSearchTab(BUTTON_KIKAKU_TAB_SEARCH);

        // 検索ダイアログの生成
        scrollSearch = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.window_kikaku_search, null);

        editFreeWord = (EditText) scrollSearch.findViewById(R.id.editFreeWord);
        editFreeWord.setText(sSearchWord);

        if(arrayOption == null) {
            arrayOption = new boolean[MAX_OPTION];
            for(int i = 0; i < MAX_OPTION; ++i) {
                arrayOption[i] = true;
            }
        }
        for(int i = 1; i <= MAX_OPTION; ++i) {
            ImageButton button = (ImageButton) scrollSearch.findViewById(getResources().getIdentifier("buttonSearchSetting" + i, "id", getContext().getPackageName()));
            button.setTag(i - 1);
            if (arrayOption[i - 1]) {
                button.setAlpha(1.0f);
            } else {
                button.setAlpha(0.25f);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayOption[(int) v.getTag()] = !arrayOption[(int) v.getTag()];
                    if (arrayOption[(int) v.getTag()]) {
                        v.setAlpha(1.0f);
                    } else {
                        v.setAlpha(0.25f);
                    }
                }
            });
        }

        ImageButton buttonReset = (ImageButton) scrollSearch.findViewById(R.id.buttonSettingReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < MAX_OPTION; ++i) {
                    arrayOption[i] = true;
                    ImageButton button = (ImageButton) scrollSearch.findViewById(getResources().getIdentifier("buttonSearchSetting" + (i + 1), "id", getContext().getPackageName()));
                    button.setAlpha(1.0f);
                }
                editFreeWord = null;
            }
        });
        ImageButton buttonRandom = (ImageButton) scrollSearch.findViewById(R.id.buttonSettingRandom);
        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchRandom();
                KikakuListAdapter arrayAdapterKikaku = new KikakuListAdapter(getActivity(), 0, arraySearchData);
                tableSearch.setAdapter(arrayAdapterKikaku);
                alertSearch.dismiss();
            }
        });

        Button buttonExec = (Button) scrollSearch.findViewById(R.id.buttonSearchExec);
        buttonExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchData(arrayOption, String.valueOf(editFreeWord.getText()));
                KikakuListAdapter arrayAdapterKikaku = new KikakuListAdapter(getActivity(), 0, arraySearchData);
                tableSearch.setAdapter(arrayAdapterKikaku);
                alertSearch.dismiss();
            }
        });
        alertSearch = new AlertDialog.Builder(getActivity()).setView(scrollSearch).create();
        setSearchData(arrayOption, null);
        KikakuListAdapter arrayAdapterKikaku = new KikakuListAdapter(getActivity(), 0, arraySearchData);
        tableSearch.setAdapter(arrayAdapterKikaku);

        final LinearLayout viewDetail = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.window_kikaku_detail, null);
        tableSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KikakuItem item = arraySearchData.get(position);
                bFavorite = false;
                for(int i = 0; i < arrayFavorite.size(); ++i) {
                    if(item.getIntValue("id") == arrayFavorite.get(i)) {
                        bFavorite = true;
                        break;
                    }
                }
                setDetailLayout(viewDetail, item);
                alertDetail.show();
            }
        });

        tableFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KikakuItem item = arrayFavoriteData.get(position);
                bFavorite = true;
                setDetailLayout(viewDetail, item);
                alertDetail.show();
            }
        });

        alertDetail = new AlertDialog.Builder(getActivity()).setView(viewDetail).create();

        return view;
    }

    private void setDetailLayout(final View viewDetail, final KikakuItem item) {
        final ImageButton buttonFavorite = (ImageButton) viewDetail.findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bFavorite = !bFavorite;
                if(bFavorite) {
                    //buttonFavorite.setImageResource(R.drawable.icon_favo_on);
                    arrayFavorite.add(item.getIntValue("id"));
                }
                else {
                    //buttonFavorite.setImageResource(R.drawable.icon_favo);
                    arrayFavorite.remove((Object)item.getIntValue("id"));
                }
                saveFavorite(arrayFavorite);
                if (viewFavorite.isShown()) {
                    setSearchTab(BUTTON_KIKAKU_TAB_FAVORITE);
                }
            }
        });
        if(bFavorite) {
            //buttonFavorite.setImageResource(R.drawable.icon_favo_on);
        }
        else {
            //buttonFavorite.setImageResource(R.drawable.icon_favo);
        }
        TextView textName = (TextView) viewDetail.findViewById(R.id.textName);
        textName.setText(item.getStringValue("name"));
        TextView textSummary = (TextView) viewDetail.findViewById(R.id.textSummary);
        textSummary.setText(item.getStringValue("summary"));
        TextView textDetail = (TextView) viewDetail.findViewById(R.id.textDetail);
        textDetail.setText(item.getStringValue("detail"));
        TextView textLocation = (TextView) viewDetail.findViewById(R.id.textLocation);
        textLocation.setText(item.getStringValue("place_name"));
        ImageView imagePRcut = (ImageView) viewDetail.findViewById(R.id.imagePRCut);
        if(item.getStringValue("image") == null || item.getStringValue("image").length() <= 0) {
            //imagePRcut.setImageResource(R.drawable.no_image);
        }
        else {
            imagePRcut.setImageBitmap(Commons.getAssetsImage(getContext().getResources(), item.getStringValue("image") + ".png"));
        }
        ImageView imageGenre1 = (ImageView) viewDetail.findViewById(R.id.imageGenre1);
        imageGenre1.setImageResource(getResources().getIdentifier("icon_genre" + item.getIntValue("genre1"), "drawable", getContext().getPackageName()));
        ImageView imageGenre2 = (ImageView) viewDetail.findViewById(R.id.imageGenre2);
        if(item.getIntValue("genre2") == 0) {
            imageGenre2.setVisibility(View.INVISIBLE);
        }
        else {
            imageGenre2.setVisibility(View.VISIBLE);
            imageGenre2.setImageResource(getResources().getIdentifier("icon_genre" + item.getIntValue("genre2"), "drawable", getContext().getPackageName()));
        }
        ImageView imageDay1 = (ImageView) viewDetail.findViewById(R.id.imageDay1);
        imageDay1.setAlpha(1.0f);
        if(item.getIntValue("day1") == 0) {
            imageDay1.setAlpha(0.25f);
        }
        ImageView imageDay2 = (ImageView) viewDetail.findViewById(R.id.imageDay2);
        imageDay2.setAlpha(1.0f);
        if(item.getIntValue("day2") == 0) {
            imageDay2.setAlpha(0.25f);
        }
        ImageView imageDay3 = (ImageView) viewDetail.findViewById(R.id.imageDay3);
        imageDay3.setAlpha(1.0f);
        if(item.getIntValue("day3") == 0) {
            imageDay3.setAlpha(0.25f);
        }
        ImageView imageDay4 = (ImageView) viewDetail.findViewById(R.id.imageDay4);
        imageDay4.setAlpha(1.0f);
        if(item.getIntValue("day4") == 0) {
            imageDay4.setAlpha(0.25f);
        }
    }

    private void setSearchTab(int nTarget) {
        if (nTarget == BUTTON_KIKAKU_TAB_SEARCH) {
            buttonTabSearch.setAlpha((float) 1.0);
            buttonTabFavorite.setAlpha((float) 0.3);
            tableSearch.scrollTo(0, 0);
            viewSearch.setVisibility(View.VISIBLE);
            viewFavorite.setVisibility(View.INVISIBLE);
        } else if (nTarget == BUTTON_KIKAKU_TAB_FAVORITE) {
            buttonTabSearch.setAlpha((float) 0.3);
            buttonTabFavorite.setAlpha((float) 1.0);
            viewSearch.setVisibility(View.INVISIBLE);
            arrayFavorite = getFavorite();
            arrayFavoriteData = new ArrayList<>();
            if (arrayFavorite == null || arrayFavorite.size() <=0){
                viewNoFavorite.setVisibility(View.VISIBLE);
            }
            else{
                viewNoFavorite.setVisibility(View.INVISIBLE);
                for(int i = 0; i < arrayFavorite.size(); ++i) {
                    int nId = arrayFavorite.get(i);
                    for (KikakuItem anArrayKikakuData : arrayKikakuData) {
                        if (anArrayKikakuData.getIntValue("id") == nId) {
                            arrayFavoriteData.add((anArrayKikakuData));
                            break;
                        }
                    }
                }
            }
            KikakuListAdapter arrayAdapterKikaku = new KikakuListAdapter(getActivity(), 0, arrayFavoriteData);
            tableFavorite.setAdapter(arrayAdapterKikaku);
            tableFavorite.scrollTo(0, 0);
            viewFavorite.setVisibility(View.VISIBLE);
        }
    }


    public void setSearchRandom() {
        sSearchWord = null;

        arraySearchData = new ArrayList<>();

        for(int i = 0; i < 5; ++i) {
            KikakuItem bufData = arrayKikakuData[Commons.getRandom(arrayKikakuData.length)];
            int h;
            for(h = 0; h < i; ++h) {
                if(bufData.getIntValue("id") == arraySearchData.get(h).getIntValue("id")) {
                    break;
                }
            }
            if(h < i) {
                --i;
                continue;
            }
            arraySearchData.add(bufData);
        }
    }

    public void setSearchData(boolean[] array, String sWord) {
        sSearchWord = sWord;

        arraySearchData = new ArrayList<>();
        for (KikakuItem item : arrayKikakuData) {
            // 日付：チェックしている日付のうちいずれかが出店日であるかのチェック
            if (!((array[BUTTON_SEARCH_DAY1] && item.getIntValue("day1") == 1) ||
                    (array[BUTTON_SEARCH_DAY2] && item.getIntValue("day2") == 1) ||
                    (array[BUTTON_SEARCH_DAY3] && item.getIntValue("day3") == 1) ||
                    (array[BUTTON_SEARCH_DAY4] && item.getIntValue("day4") == 1))) {
                continue;
            }
            // ジャンル：チェックしているいずれかのジャンルであるかのチェック
            int h;
            for (h = 0; h < MAX_GENRE; ++h) {
                if (!array[BUTTON_SEARCH_GENRE1 + h]) {
                    continue;
                }
                if (item.getIntValue("genre1") == (h + 1)) {
                    break;
                }
                if (item.getIntValue("genre2") == (h + 1)) {
                    break;
                }
            }
            if (h == MAX_GENRE) {
                continue;
            }
            // 形態：チェックしているいずれかの形態であるかのチェック
            for (h = 0; h < MAX_TYPE; ++h) {
                if (!array[BUTTON_SEARCH_TYPE1 + h]) {
                    continue;
                }
                if (item.getIntValue("type_id") == (h + 1)) {
                    break;
                }
            }
            if (h == MAX_TYPE) {
                continue;
            }

            // フリーワード：名前、概要、詳細、場所のいずれかにヒットするかのチェック
            if (sWord != null && sWord.length() > 0) {
                if (!item.getStringValue("name").contains(sWord) &&
                        !item.getStringValue("summary").contains(sWord) &&
                        !item.getStringValue("detail").contains(sWord) &&
                        !item.getStringValue("place_name").contains(sWord)) {
                    continue;
                }
            }

            // 全チェックを抜けたデータを配列に追加
            arraySearchData.add(item);
        }
    }

    public ArrayList<Integer> getFavorite() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        String str = Commons.readString(getContext(), "kikaku_favorite");
        if(str == null) {
            return arrayList;
        }
        String[] arrStr = str.split(",");
        for (String anArrStr : arrStr) {
            try {
                arrayList.add(Integer.parseInt(anArrStr));
            } catch (Exception ignored) {

            }
        }

        return arrayList;
    }

    public void saveFavorite(ArrayList<Integer> arrayList) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < arrayList.size() - 1; ++i) {
            result.append(arrayList.get(i)).append(",");
        }
        if(arrayList.size() > 0) {
            result.append(arrayList.get(arrayList.size() - 1));
        }
        Commons.writeString(getContext(), "kikaku_favorite", result.toString());
    }
}

class KikakuItem {
    private JSONObject data;

    KikakuItem(){
        data = null;
    }

    public boolean setData(JSONObject data) {
        try {
            this.data = data;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getStringValue(String key) {
        try {
            return data.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    public int getIntValue(String key) {
        try {
            return data.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}


class KikakuListAdapter extends ArrayAdapter<KikakuItem> {
    private LayoutInflater layoutInflater;

    KikakuListAdapter(Context context, int textViewResourceId, ArrayList<KikakuItem> objects) {
        super(context, textViewResourceId, objects);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KikakuItem item = getItem(position);

        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.listitem_kikaku, null);
        }
        if(position % 2 == 1) {
            convertView.setBackgroundColor(Color.argb((int) (0.4 * 255), 255, 255, 255));
        }
        else {
            convertView.setBackgroundColor(Color.argb(0, 255, 255, 255));
        }
        ImageView imagePRcut = (ImageView) convertView.findViewById(R.id.imagePRCut);
        if (item.getStringValue("image") == null || item.getStringValue("image").length() <= 0) {
            //imagePRcut.setImageResource(R.drawable.no_image);
        } else {
            imagePRcut.setImageBitmap(Commons.getAssetsImage(getContext().getResources(), item.getStringValue("image") + ".png"));
        }
        TextView textName = (TextView) convertView.findViewById(R.id.textGroupName);
        textName.setText(item.getStringValue("name"));
        TextView textSummary = (TextView) convertView.findViewById(R.id.textGroupSummary);
        textSummary.setText(item.getStringValue("summary"));

        return convertView;
    }


}
