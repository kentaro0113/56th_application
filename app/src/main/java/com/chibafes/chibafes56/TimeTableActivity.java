package com.chibafes.chibafes56;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.longevitysoft.android.xml.plist.domain.Array;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by shiho on 2017/08/31.
 */

// 0903_kimura:ActivityからFragmentへ変更、それに伴う調整
public class TimeTableActivity extends Fragment {
    private static final int BUTTON_TIMETABLE_TAB_DAY1 = 0;
    private static final int BUTTON_TIMETABLE_TAB_DAY2 = 1;
    private static final int BUTTON_TIMETABLE_TAB_DAY3 = 2;
    private static final int BUTTON_TIMETABLE_TAB_CHECK = 4;

    private TextView viewNoFavorite;
    private Button buttonTabDay1;
    private Button buttonTabDay2;
    private Button buttonTabDay3;

    private Button buttonTabCheck;
    private ListView listView1;
    private ListView listView2;
    private AlertDialog alertTimeTable = null;
    private LinearLayout viewTimeTable = null;
    private LinearLayout viewTableBase;
    private ScrollView scrollBase;

    private KikakuItem[] arrayKikakuData;
    private Array arrayTimeTable;
    private int nCurrentList = Statics.NONE;

    private List<TimeTableItem> listStage;
    private List<TimeTableItem> listStreet;
    private int[] arrayCheck;
    private boolean bCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_timetable, container, false);

        // タブのボタンサイズを調整
        buttonTabDay1 = view.findViewById(R.id.buttonTabDay1);
        buttonTabDay2 = view.findViewById(R.id.buttonTabDay2);
        buttonTabDay3 = view.findViewById(R.id.buttonTabDay3);
        buttonTabCheck = view.findViewById(R.id.buttonTabCheck);

        Point displaySize = Commons.getDisplaySize(getContext());
        int nTabImageWidth = (displaySize.x - 56) / 5;
        int nTabImageHeight = nTabImageWidth * 4 / 7;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) buttonTabDay1.getLayoutParams();
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonTabDay1.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) buttonTabDay2.getLayoutParams();
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonTabDay2.setLayoutParams(params);

        params = (LinearLayout.LayoutParams) buttonTabDay3.getLayoutParams();
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonTabDay3.setLayoutParams(params);



        params = (LinearLayout.LayoutParams) buttonTabCheck.getLayoutParams();
        params.width = nTabImageWidth;
        params.height = nTabImageHeight;
        buttonTabCheck.setLayoutParams(params);

        buttonTabDay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(BUTTON_TIMETABLE_TAB_DAY1);
            }
        });
        buttonTabDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(BUTTON_TIMETABLE_TAB_DAY2);
            }
        });
        buttonTabDay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(BUTTON_TIMETABLE_TAB_DAY3);
            }
        });

        buttonTabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(BUTTON_TIMETABLE_TAB_CHECK);
            }
        });

        viewTableBase = view.findViewById(R.id.viewTableBase);
        viewNoFavorite = view.findViewById(R.id.viewNoFavorite);
        scrollBase = view.findViewById(R.id.scrollTimeTable);

        // 企画、タイムテーブルデータを読み込む
        String sKikaku = Commons.readString(getContext(), "data_kikaku");
        if(sKikaku != null) {
            try {
                JSONArray jsonArray = new JSONArray(sKikaku);
                int nCount = 0;
                for(int i = 0; i < jsonArray.length(); ++i) {
                    int nType = jsonArray.getJSONObject(i).getInt("type_id");
                    if(nType == 3 || nType == 4) {
                        nCount++;
                    }
                }
                arrayKikakuData = new KikakuItem[nCount];
                int h = 0;
                for(int i = 0; i < jsonArray.length(); ++i) {
                    int nType = jsonArray.getJSONObject(i).getInt("type_id");
                    if(nType == 3 || nType == 4) {
                        arrayKikakuData[h] = new KikakuItem();
                        arrayKikakuData[h].setData(jsonArray.getJSONObject(i));
                        h++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PList plist = Commons.getParsedPlist(getContext(), "TimeTable.plist");
        arrayTimeTable = (Array) plist.getRootElement();

        // チェックリストを読み込む
        arrayCheck = Commons.readArrayInt(getContext());

        // リスト
        listView1 = view.findViewById(R.id.list_timetable);
        listView2 = view.findViewById(R.id.list_timetable2);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimeTableItem item = listStage.get(position);
                setDetailLayout(viewTimeTable, item);
                alertTimeTable.show();
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimeTableItem item = listStreet.get(position);
                setDetailLayout(viewTimeTable, item);
                alertTimeTable.show();
            }
        });

        // 詳細画面の作成
        viewTimeTable = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.window_timetable, null);
        alertTimeTable = new AlertDialog.Builder(getActivity()).setView(viewTimeTable).create();

        setList(BUTTON_TIMETABLE_TAB_DAY1);

        return view;
    }

    private void setList(int nextList) {
        if(nextList == nCurrentList && nextList != BUTTON_TIMETABLE_TAB_CHECK) {
            return;
        }

        buttonTabDay1.setAlpha(0.25f);
        buttonTabDay2.setAlpha(0.25f);
        buttonTabDay3.setAlpha(0.25f);

        buttonTabCheck.setAlpha(0.25f);
        viewNoFavorite.setVisibility(View.GONE);
        switch(nextList) {
            case BUTTON_TIMETABLE_TAB_DAY1:
                buttonTabDay1.setAlpha(1.0f);
                viewTableBase.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTabDay1));
                break;
            case BUTTON_TIMETABLE_TAB_DAY2:
                buttonTabDay2.setAlpha(1.0f);
                viewTableBase.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTabDay2));
                break;
            case BUTTON_TIMETABLE_TAB_DAY3:
                buttonTabDay3.setAlpha(1.0f);
                viewTableBase.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTabDay3));
                break;

            case BUTTON_TIMETABLE_TAB_CHECK:
                buttonTabCheck.setAlpha(1.0f);
                viewTableBase.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTabCheck));

                arrayCheck = Commons.readArrayInt(getContext());
                if(arrayCheck == null || arrayCheck.length <= 0) {
                    viewNoFavorite.setVisibility(View.VISIBLE);
                }
                break;
        }

        listStage = new ArrayList<>();
        listStreet = new ArrayList<>();

        if(nextList == BUTTON_TIMETABLE_TAB_CHECK) {
            if (arrayCheck != null) {
                // 指定日のステージのリスト生成
                for (int anArrayCheck : arrayCheck) {
                    int nTargetIndex = anArrayCheck / 10000;
                    int nTargetTime = anArrayCheck % 10000;

                    Array targetArray = (Array) arrayTimeTable.get(nTargetIndex);
                    for (int i = 0; i < targetArray.size(); ++i) {
                        Dict dic = (Dict) targetArray.get(i);
                        if (dic.getConfigurationInteger("time").getValue() == nTargetTime) {
                            TimeTableItem item = new TimeTableItem();
                            item.setData(dic, nTargetIndex, arrayKikakuData);
                            if (nTargetIndex % 2 == 0) {
                                listStage.add(item);
                            } else {
                                listStreet.add(item);
                            }
                            break;
                        }
                    }
                }
            }
        }
        else {
            // 指定日のステージのリスト生成
            listStage = new ArrayList<>();
            Array targetArray = (Array) arrayTimeTable.get(nextList * 2);
            for(int i = 0; i < targetArray.size(); ++i) {
                Dict dic = (Dict) targetArray.get(i);
                TimeTableItem item = new TimeTableItem();
                item.setData(dic, nextList * 2, arrayKikakuData);
                listStage.add(item);
            }
            // 指定日のストパのリスト生成
            listStreet = new ArrayList<>();
            targetArray = (Array) arrayTimeTable.get(nextList * 2 + 1);
            for(int i = 0; i < targetArray.size(); ++i) {
                Dict dic = (Dict) targetArray.get(i);
                TimeTableItem item = new TimeTableItem();
                item.setData(dic, nextList * 2 + 1, arrayKikakuData);
                listStreet.add(item);
            }
        }

        TimeTableListAdapter arrayAdapterStage = new TimeTableListAdapter(getActivity(), 0, listStage);
        listView1.setAdapter(arrayAdapterStage);
        TimeTableListAdapter arrayAdapterStreet = new TimeTableListAdapter(getActivity(), 0, listStreet);
        listView2.setAdapter(arrayAdapterStreet);

        scrollBase.scrollTo(0, 0);

        nCurrentList = nextList;
    }

    private void setDetailLayout(final View viewDetail, final TimeTableItem itemTimeTable) {
        final KikakuItem item = itemTimeTable.getKikakuItem();
        final ImageButton buttonFavorite = viewDetail.findViewById(R.id.buttonFavorite);
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bCheck = !bCheck;
                if(bCheck) {
                    buttonFavorite.setImageResource(R.drawable.icon_favo_on);
                }
                else {
                    buttonFavorite.setImageResource(R.drawable.icon_favo);
                }

                int nTime = itemTimeTable.getIndex() * 10000 + itemTimeTable.getTime();
                if(bCheck) {
                    if(arrayCheck == null) {
                        arrayCheck = new int[1];
                        arrayCheck[0] = nTime;
                    }
                    else {
                        int[] newCheck = new int[arrayCheck.length + 1];
                        int nIndex;

                        for(nIndex = 0; nIndex < arrayCheck.length; ++nIndex) {
                            int nTargetTime = arrayCheck[nIndex];
                            if (nTime < nTargetTime) {
                                break;
                            }
                        }
                        for (int i = 0; i < newCheck.length; ++i) {
                            int nNewTime;
                            if(i == nIndex) {
                                nNewTime = nTime;
                            }
                            else if(i > nIndex) {
                                nNewTime = arrayCheck[i - 1];
                            }
                            else {
                                nNewTime = arrayCheck[i];
                            }
                            newCheck[i] = nNewTime;
                        }
                        arrayCheck = newCheck;
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(2019, 11, (2 + itemTimeTable.getIndex() / 2), itemTimeTable.getTime() / 100, itemTimeTable.getTime() % 100, 0);
                    calendar.add(Calendar.MINUTE, -15);

                    Calendar calendar2 = Calendar.getInstance();
                    int diff = calendar.compareTo(calendar2);
                    if(diff > 0) {
                        String sTicker = "";
                        String sMessage = "";
                        if(itemTimeTable.getIndex() % 2 == 0) {
                            sMessage = "まもなく「" + item.getStringValue("name") + "」のステージがはじまります！";
                            sTicker = sMessage;
                        }
                        else {
                            sMessage = "まもなく「" + item.getStringValue("name") + "」のストリートパフォーマンスがはじまります！";
                            sTicker = sMessage;
                        }
                        NotificationUtil.setLocalNotification(getContext(), sTicker, sMessage, nTime, calendar);
                    }
                    Commons.writeArrayInt(getContext(), arrayCheck);
                }
                else {
                    if(arrayCheck.length <= 1) {
                        arrayCheck = null;
                        Commons.deleteSave(getContext());
                    }
                    else {
                        int[] newCheck = new int[arrayCheck.length - 1];
                        for (int i = 0, h = 0; i < arrayCheck.length; ++i) {
                            int nTargetTime = arrayCheck[i];
                            if (nTime == nTargetTime) {
                                continue;
                            }
                            newCheck[h] = arrayCheck[i];
                            h++;
                        }
                        arrayCheck = newCheck;

                        NotificationUtil.cancelLocalNotification(getContext(), nTime);

                        Commons.writeArrayInt(getContext(), arrayCheck);
                    }
                }
                    // 通知の作成
                    /*
                    UIUserNotificationType types = UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
                    UIUserNotificationSettings *mySettings = [UIUserNotificationSettings settingsForTypes:types categories:nil];
                [[UIApplication sharedApplication] registerUserNotificationSettings:mySettings];

                    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
                [dateFormatter setDateFormat:@"yyyy/MM/dd HH:mm:ss"];
                    NSDate *targetDate = [dateFormatter dateFromString:[NSString stringWithFormat:@"2017/11/%02d %02d:%02d:00", (2 + [[dicInfo objectForKey:@"array_index"] intValue] / 2), [[dicInfo objectForKey:@"time_value"] intValue] / 100,  [[dicInfo objectForKey:@"time_value"] intValue] % 100]];
                    targetDate = [targetDate initWithTimeInterval:-15 * 60 sinceDate:targetDate];

                    UILocalNotification *notification = [[UILocalNotification alloc] init];
                    notification.fireDate = targetDate;
                    notification.timeZone = [NSTimeZone defaultTimeZone];
                    notification.soundName = UILocalNotificationDefaultSoundName;
                    if([[dicInfo objectForKey:@"array_index"] intValue] % 2 == 0) {
                        notification.alertBody = [NSString stringWithFormat:@"まもなく「%@」のステージがはじまります！", [dicInfo objectForKey:@"name"]];
                    }
                else {
                        notification.alertBody = [NSString stringWithFormat:@"まもなく「%@」のストリートパフォーマンスがはじまります！", [dicInfo objectForKey:@"name"]];
                    }
                    notification.userInfo = [NSDictionary dictionaryWithObject:[NSString stringWithFormat:@"%d", nTime] forKey:@"id"];
                [[UIApplication sharedApplication] scheduleLocalNotification:notification];
                }
                else {
                    NSUInteger nTargetIndex = [self checkCheckd];
                [arrayCheck removeObjectAtIndex:nTargetIndex];
                    // 通知の削除
                    for(UILocalNotification *notification in [[UIApplication sharedApplication]     scheduledLocalNotifications]) {
                        if([[notification.userInfo objectForKey:@"id"] intValue] == nTime) {
                        [[UIApplication sharedApplication] cancelLocalNotification:notification];
                        }
                    }
                }
                */
                
                
                
                
                
                
                
                
                
                
                if (nCurrentList == BUTTON_TIMETABLE_TAB_CHECK) {
                    setList(BUTTON_TIMETABLE_TAB_CHECK);
                }
            }
        });
        if(nCurrentList == BUTTON_TIMETABLE_TAB_CHECK) {
            bCheck = true;
        }
        else {
            bCheck = false;
            if(arrayCheck != null) {
                for (int anArrayCheck : arrayCheck) {
                    int nTargetIndex = anArrayCheck / 10000;
                    if (itemTimeTable.getIndex() != nTargetIndex) {
                        continue;
                    }
                    int nTargetTime = anArrayCheck % 10000;
                    if (itemTimeTable.getTime() == nTargetTime) {
                        bCheck = true;
                        break;
                    }
                }
            }
        }
        
        if(bCheck) {
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
        TextView textTime = viewDetail.findViewById(R.id.textTime);
        textTime.setText(itemTimeTable.getTimeString());
        ImageView imagePRcut = viewDetail.findViewById(R.id.imagePRCut);
        if(item.getStringValue("image") == null || item.getStringValue("image").length() <= 0) {
            imagePRcut.setImageResource(R.drawable.no_image);
        }
        else {
            imagePRcut.setImageBitmap(Commons.getAssetsImage(getContext().getResources(), item.getStringValue("image") + ".jpg"));
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
    }
    
    class TimeTableItem {
        private int nIndex;
        private int nId;
        private int nTime;
        private KikakuItem itemKikaku;

        TimeTableItem(){
        }

        boolean setData(Dict data, int nIndex, KikakuItem[] arrayKikakuData) {
            try {
                this.nIndex = nIndex;
                nId = data.getConfigurationInteger("id").getValue();
                nTime = data.getConfigurationInteger("time").getValue();

                for (KikakuItem anArrayKikakuData : arrayKikakuData) {
                    int n = anArrayKikakuData.getIntValue("id");
                    if (anArrayKikakuData.getIntValue("id") == getId()) {
                        itemKikaku = anArrayKikakuData;
                        break;
                    }
                }

            } catch (Exception e) {
                return false;
            }
            return true;
        }

        public int getId() {
            return nId;
        }
        int getTime() {
            return nTime;
        }
        int getIndex() {
            return nIndex;
        }
        String getTimeString() {
            return (String.format("%d:%02d", (nTime / 100), (nTime % 100)));
        }

        KikakuItem getKikakuItem() {
            return itemKikaku;
        }
    }

    class TimeTableListAdapter extends ArrayAdapter<TimeTableItem> {
        private Context context;
        private LayoutInflater layoutInflater;

        TimeTableListAdapter(Context context, int textViewResourceId, List<TimeTableItem> objects) {
            super(context, textViewResourceId, objects);

            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TimeTableItem itemTime = getItem(position);
            KikakuItem itemKikaku = null;
            if (itemTime != null) {
                itemKikaku = itemTime.getKikakuItem();
            }

            if (null == convertView) {
                convertView = layoutInflater.inflate(R.layout.listitem_timetable, null);
            }
            if(itemKikaku == null) {
                return convertView;
            }
            
            ImageView imagePRcut = convertView.findViewById(R.id.imagePRCut);
            if(itemKikaku.getStringValue("image") == null || itemKikaku.getStringValue("image").length() <= 0) {
                imagePRcut.setImageResource(R.drawable.no_image);
            }
            else {
                imagePRcut.setImageBitmap(Commons.getAssetsImage(getContext().getResources(), itemKikaku.getStringValue("image") + ".jpg"));
            }
            TextView textName = convertView.findViewById(R.id.textGroupName);
            textName.setText(itemKikaku.getStringValue("name"));
            TextView textTime = convertView.findViewById(R.id.textGroupTime);
            textTime.setText(itemTime.getTimeString());

            return convertView;
        }
    }
}
