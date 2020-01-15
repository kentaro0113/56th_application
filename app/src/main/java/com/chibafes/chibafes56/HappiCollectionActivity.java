package com.chibafes.chibafes56;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llrk on 2017/03/26.
 */

public class HappiCollectionActivity extends Activity {
    private Array arrayCollectionList;
    private AlertDialog alartInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);

        PList listHappi = Commons.getParsedPlist(this, "HappiCollectionList.plist");
        arrayCollectionList = (Array) listHappi.getRootElement();
        List<Dict> objects = new ArrayList<>();
        for(int i = 0; i < arrayCollectionList.size(); ++i) {
            objects.add((Dict) arrayCollectionList.get(i));
        }

        HappiCollectionAdapter collectionAdapter = new HappiCollectionAdapter(this, 0, objects);
        ListView listView = findViewById(R.id.listCollection);
        listView.setAdapter(collectionAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Commons.readInt(HappiCollectionActivity.this, "getItem_57th" + position) > 0) {
                    Dict dic = (Dict) arrayCollectionList.get(position);

                    ScrollView viewInfo = (ScrollView) getLayoutInflater().inflate(R.layout.collection_info_view, null);
                    TextView textName = viewInfo.findViewById(R.id.textName);
                    textName.setText(dic.getConfiguration("name").getValue());
                    TextView textInfo = viewInfo.findViewById(R.id.textInfo);
                    textInfo.setText(dic.getConfiguration("note").getValue());
                    ImageView imageHappi = viewInfo.findViewById(R.id.imageHappi);
                    imageHappi.setImageResource(getResources().getIdentifier((dic.getConfiguration("image").getValue()), "drawable", getPackageName()));
                    alartInfo = new AlertDialog.Builder(HappiCollectionActivity.this)
                            .setView(viewInfo)
                            .show();
                }
            }
        });

    }

    public void onClickCloseInfo(View view) {
        alartInfo.dismiss();
    }

    public void onClickButtonBack(View view) {
        // この画面を閉じる
        finish();
    }
}

class HappiCollectionAdapter extends ArrayAdapter<Dict> {
    private Context context;
    private LayoutInflater layoutInflater;

    HappiCollectionAdapter(Context context, int textViewResourceId, List<Dict> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Dict item = getItem(position);

        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.list_collection_item, null);
        }

        ImageView image = convertView.findViewById(R.id.imageCollection);
        TextView label = convertView.findViewById(R.id.textName);

        if (Commons.readInt(context, "getItem_57th" + position) > 0) {
            String nFileName = item.getConfiguration("image").getValue();
            int nImageId = context.getResources().getIdentifier(nFileName, "drawable", context.getPackageName());
            image.setImageResource(nImageId);
            label.setText(item.getConfiguration("name").getValue());
        } else {
            image.setImageResource(R.drawable.icon_noget);
            label.setText(context.getResources().getString(R.string.collection_noget));
        }

        return convertView;
    }
}