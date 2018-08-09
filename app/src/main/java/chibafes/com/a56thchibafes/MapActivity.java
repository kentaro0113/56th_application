package chibafes.com.a56thchibafes;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qozix.tileview.TileView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aki09 on 2017/09/08.
 */

public class MapActivity extends Fragment {
    private MapItem[] arraySpotList = null;
    private TileView imageMap = null;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_map, container, false);
        setMapInfo(view);

        return view;
    }


    private void setMapInfo(View view) {
        String newImage = null;
        int imageWidth = 0;
        int imageHeight = 0;

        LinearLayout viewMapBase = (LinearLayout) view.findViewById(R.id.viewMapBase);
        if(imageMap != null) {
            viewMapBase.removeView(imageMap);
            imageMap.destroy();
            imageMap = null;
        }

        imageMap = new TileView(getActivity());
        imageMap.setBackgroundColor(Color.WHITE);
        imageMap.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        viewMapBase.addView(imageMap);

        newImage = "map_nishichiba";
        imageWidth = 2000;
        imageHeight = 962;

        imageMap.setScaleLimits(0.1f, 3.0f);
        imageMap.setSize(imageWidth, imageHeight);

        imageMap.addDetailLevel(1.000f, newImage + "/1000/%d_%d.png");
        imageMap.addDetailLevel(0.500f, newImage + "/500/%d_%d.png");
        imageMap.addDetailLevel(0.250f, newImage + "/250/%d_%d.png");
        imageMap.addDetailLevel(0.125f, newImage + "/125/%d_%d.png");

        // スポット情報を取得する
        String sMap = Commons.readString(getContext(), "data_map");
        arraySpotList = null;
        if(sMap != null) {
            try {
                JSONArray array = new JSONArray(sMap);
                arraySpotList = new MapItem[array.length()];
                for(int i = 0; i < array.length(); ++i) {
                    arraySpotList[i] = new MapItem();
                    arraySpotList[i].setData(array.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] data = new String[arraySpotList.length];
        for(int i = 0;i < arraySpotList.length; ++i) {
            data[i] = arraySpotList[i].getStringValue("name");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        ListView listSpot = (ListView) view.findViewById(R.id.listSpot);
        listSpot.setAdapter(arrayAdapter);

        listSpot.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapItem item = arraySpotList[position];

                for(int i = 0; i < imageMap.getMarkerLayout().getChildCount(); ++i) {
                    ImageView imageView = (ImageView) imageMap.getMarkerLayout().getChildAt(i);
                    if((item.getStringValue("name")).equals(((MapItem)imageView.getTag()).getStringValue("name"))){
                        imageMap.moveToMarker(imageView, true);
                        break;
                    }
                }
            }
        });
        // スポットにボタンを配置する
        for (int i = 0; i < arraySpotList.length; ++i) {
            MapItem item = arraySpotList[i];
            ImageView imageView = new ImageView(getContext());
            Point size = Commons.getDisplaySize(getContext());
            size.x /= 32;
            imageView.setImageBitmap(Commons.getResizeBitmapFromId(getContext().getResources(), R.drawable.pin, size));
            //imageView.setImageResource(R.drawable.pin);
            imageView.setTag(item);
            //imageView.setOnClickListener(markerClickListener);
            imageMap.addMarker(imageView, item.getIntValue("lat"), item.getIntValue("lon"), -0.5f, -1.0f);
        }
        imageMap.setScale(0.3f);

    }
}


// 新着情報管理クラス
class MapItem{
    private JSONObject data;

    public MapItem(){
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
