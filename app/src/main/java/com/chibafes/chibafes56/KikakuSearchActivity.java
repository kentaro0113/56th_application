package com.chibafes.chibafes56;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**-
 * Created by steee on 2017/09/01.
 */

// 0903_kimura:ActivityからFragmentへ変更、それに伴う調整
public class KikakuSearchActivity extends Fragment {
    private LinearLayout viewSearch;
    private FrameLayout viewFavorite;
    private Button buttonTabSearch;
    private Button buttonTabFavorite;
    private ListView tableSearch;
    private ListView tableFavorite;
//    private TextView viewNoFavorite;
    private AlertDialog alertSearch = null;
    private AlertDialog alertDetail = null;
    private EditText editFreeWord;
    private ScrollView scrollSearch;

    private static int BUTTON_KIKAKU_TAB_SEARCH = 0;
    private static int BUTTON_KIKAKU_TAB_FAVORITE = 1;

    private static int MAX_OPTION = 16;

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

        viewSearch = view.findViewById(R.id.viewSearch);
        viewFavorite = view.findViewById(R.id.viewFavorite);
        buttonTabSearch = view.findViewById(R.id.buttonTabSearch);
        buttonTabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setSearchTab(BUTTON_KIKAKU_TAB_SEARCH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buttonTabFavorite = view.findViewById(R.id.buttonTabFavorite);
        buttonTabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setSearchTab(BUTTON_KIKAKU_TAB_FAVORITE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageButton buttonSearch = view.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSearch.show();
                scrollSearch.scrollTo(0, 0);
            }
        });
        Point displaySize = Commons.getDisplaySize(Objects.requireNonNull(getContext()));
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

        tableSearch = view.findViewById(R.id.tableSearch);
        tableFavorite = view.findViewById(R.id.tableFavorite);
//        viewNoFavorite = view.findViewById(R.id.viewNoFavorite);

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        arrayFavorite = getFavorite();
        try {
            setSearchTab(BUTTON_KIKAKU_TAB_SEARCH);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 検索ダイアログの生成
        scrollSearch = (ScrollView) Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.window_kikakusearch, null);

        editFreeWord = scrollSearch.findViewById(R.id.editFreeWord);
        editFreeWord.setText(sSearchWord);

        if(arrayOption == null) {
            arrayOption = new boolean[MAX_OPTION];
            for(int i = 0; i < MAX_OPTION; ++i) {
                arrayOption[i] = true;
            }
        }

        for(int i = 1; i <= MAX_OPTION; ++i) {
            ImageButton button = scrollSearch.findViewById(getResources().getIdentifier("buttonSearchSetting" + i, "id", getContext().getPackageName()));
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

        ImageButton buttonReset = scrollSearch.findViewById(R.id.buttonSettingReset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < MAX_OPTION; ++i) {
                    arrayOption[i] = true;
                    ImageButton button = scrollSearch.findViewById(getResources().getIdentifier("buttonSearchSetting" + (i + 1), "id", getContext().getPackageName()));
                    button.setAlpha(1.0f);
                }
                editFreeWord = null;
            }
        });
        ImageButton buttonRandom = scrollSearch.findViewById(R.id.buttonSettingRandom);
        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchRandom();
                KikakuListAdapter arrayAdapterKikaku = new KikakuListAdapter(getActivity(), 0, arraySearchData);
                tableSearch.setAdapter(arrayAdapterKikaku);
                alertSearch.dismiss();
            }
        });

        Button buttonExec = scrollSearch.findViewById(R.id.buttonSearchExec);
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

        final LinearLayout viewDetail = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.window_kikakudetail, null);
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
        final ImageButton buttonFavorite = viewDetail.findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bFavorite = !bFavorite;
                if(bFavorite) {
                    buttonFavorite.setImageResource(R.drawable.icon_favo_on);
                    arrayFavorite.add(item.getIntValue("id"));
                }
                else {
                    buttonFavorite.setImageResource(R.drawable.icon_favo);
                    arrayFavorite.remove((Object)item.getIntValue("id"));
                }
                saveFavorite(arrayFavorite);
                if (viewFavorite.isShown()) {
                    try {
                        setSearchTab(BUTTON_KIKAKU_TAB_FAVORITE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if(bFavorite) {
            buttonFavorite.setImageResource(R.drawable.icon_favo_on);
        }
        else {
            buttonFavorite.setImageResource(R.drawable.icon_favo);
        }
        TextView textName = viewDetail.findViewById(R.id.textName);
        textName.setText(item.getStringValue("name"));
        TextView textSummary = viewDetail.findViewById(R.id.textSummary);
        textSummary.setText(item.getStringValue("summary"));
        TextView textDetail = viewDetail.findViewById(R.id.textDetail);
        textDetail.setText(item.getStringValue("detail"));
        TextView textLocation = viewDetail.findViewById(R.id.textLocation);
        textLocation.setText(item.getStringValue("place_name"));
        ImageView imagePRcut = viewDetail.findViewById(R.id.imagePRCut);
        if(item.getStringValue("image") == null || item.getStringValue("image").length() <= 0) {
            imagePRcut.setImageResource(R.drawable.no_image);
        }
        else {
            imagePRcut.setImageBitmap(Commons.getAssetsImage(getResources(), item.getStringValue("image") + ".jpg"));
        }
        ImageView imageGenre1 = viewDetail.findViewById(R.id.imageGenre1);
        imageGenre1.setImageResource(getResources().getIdentifier("icon_genre" + item.getIntValue("genre1"), "drawable", getContext().getPackageName()));
        ImageView imageGenre2 = viewDetail.findViewById(R.id.imageGenre2);
        if(item.getIntValue("genre2") == 0) {
            imageGenre2.setVisibility(View.INVISIBLE);
        }
        else {
            imageGenre2.setVisibility(View.VISIBLE);
            imageGenre2.setImageResource(getResources().getIdentifier("icon_genre" + item.getIntValue("genre2"), "drawable", getContext().getPackageName()));
        }
        ImageView imageDay1 = viewDetail.findViewById(R.id.imageDay1);
        imageDay1.setAlpha(1.0f);
        if(item.getIntValue("day1") == 0) {
            imageDay1.setAlpha(0.25f);
        }
        ImageView imageDay2 = viewDetail.findViewById(R.id.imageDay2);
        imageDay2.setAlpha(1.0f);
        if(item.getIntValue("day2") == 0) {
            imageDay2.setAlpha(0.25f);
        }
        ImageView imageDay3 = viewDetail.findViewById(R.id.imageDay3);
        imageDay3.setAlpha(1.0f);
        if(item.getIntValue("day3") == 0) {
            imageDay3.setAlpha(0.25f);
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
//                viewNoFavorite.setVisibility(View.VISIBLE);
            }
            else{
//                viewNoFavorite.setVisibility(View.INVISIBLE);
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


    private void setSearchRandom() {
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

    private void setSearchData(boolean[] array, String sWord) {
        sSearchWord = sWord;

        arraySearchData = new ArrayList<>();
        if (arrayKikakuData != null) {
            for (KikakuItem item : arrayKikakuData) {
                // 日付：チェックしている日付のうちいずれかが出店日であるかのチェック
                int BUTTON_SEARCH_DAY1 = 0;
                int BUTTON_SEARCH_DAY2 = 1;
                int BUTTON_SEARCH_DAY3 = 2;

                if (!((array[BUTTON_SEARCH_DAY1] && item.getIntValue("day1") == 1) ||
                        (array[BUTTON_SEARCH_DAY2] && item.getIntValue("day2") == 1) ||
                        (array[BUTTON_SEARCH_DAY3] && item.getIntValue("day3") == 1)
                )) {
                    continue;
                }
                // ジャンル：チェックしているいずれかのジャンルであるかのチェック
                int h;
                int MAX_GENRE = 8;
                for (h = 0; h < MAX_GENRE; ++h) {
                    int BUTTON_SEARCH_GENRE1 = 4;
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
                int MAX_TYPE = 5;
                for (h = 0; h < MAX_TYPE; ++h) {
                    int BUTTON_SEARCH_TYPE1 = 12;
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
    }

    private ArrayList<Integer> getFavorite() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        String str = Commons.readString(Objects.requireNonNull(getContext()), "kikaku_favorite");
        if(str == null) {
            return arrayList;
        }
        try {
            String[] arrStr = str.split(",");
            for (String anArrStr : arrStr) {
                arrayList.add(Integer.parseInt(anArrStr));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private void saveFavorite(ArrayList<Integer> arrayList) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < arrayList.size() - 1; ++i) {
            result.append(arrayList.get(i)).append(",");
        }
        if(arrayList.size() > 0) {
            result.append(arrayList.get(arrayList.size() - 1));
        }
        Commons.writeString(Objects.requireNonNull(getContext()), "kikaku_favorite", result.toString());
    }
}

class KikakuItem {
    private JSONObject data;

    KikakuItem(){
        data = null;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    String getStringValue(String key) {
        try {
            return data.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    int getIntValue(String key) {
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
        ImageView imagePRcut = convertView.findViewById(R.id.imagePRCut);
        if (item != null) {
            if(item.getStringValue("image") == null || item.getStringValue("image").length() <= 0) {
                imagePRcut.setImageResource(R.drawable.no_image);
            }
            else {
                imagePRcut.setImageBitmap(Commons.getAssetsImage(getContext().getResources(), item.getStringValue("image") + ".jpg"));
            }
        }
        TextView textName = convertView.findViewById(R.id.textGroupName);
        if (item != null) {
            textName.setText(item.getStringValue("name"));
        }
        TextView textSummary = convertView.findViewById(R.id.textGroupSummary);
        if (item != null) {
            textSummary.setText(item.getStringValue("summary"));
        }

        return convertView;
    }


}