package com.chibafes.chibafes56;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;


/**
 * Created by aki09 on 2017/09/08.
 */

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private MapItem[] arrayKikakuData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_google_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String sKikaku = Commons.readString(getContext(), "data_map");
        if(sKikaku != null) {
            try {
                JSONArray jsonArray = new JSONArray(sKikaku);
                arrayKikakuData = new MapItem[jsonArray.length()];
                for(int i = 0; i < jsonArray.length(); ++i) {
                    arrayKikakuData[i] = new MapItem();
                    arrayKikakuData[i].setData(jsonArray.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    /*
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

        String[] data = new String[0];
        if (arraySpotList != null) {
            data = new String[arraySpotList.length];
        }
        if (arraySpotList != null) {
            for(int i = 0;i < arraySpotList.length; ++i) {
                data[i] = arraySpotList[i].getStringValue("name");
            }
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
        for (MapItem item : arraySpotList) {
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

    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MarkerOptions options = new MarkerOptions();

        // Add a marker in Sydney and move the camera
        for (MapItem item : arrayKikakuData) {
            LatLng position = new LatLng(item.getDoubleValue("g_lat"), item.getDoubleValue("g_lon"));

            options.position(position);
            // マーカー情報設定
            options.title(item.getStringValue("name"));
            // マップにマーカー追加
            Marker marker = googleMap.addMarker(options);

            if(item.getStringValue("name").equals("大祭本部")) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18));
                // インフォウィンドウ表示
                marker.showInfoWindow();
            }
        }
    }
}


// 新着情報管理クラス
class MapItem{
    private JSONObject data;

    MapItem(){
        data = null;
    }

    boolean setData(JSONObject data) {
        try {
            this.data = data;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    String getStringValue(String key) {
        try {
            return data.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    double getDoubleValue(String key) {
        try {
            return data.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1.0;
    }
}