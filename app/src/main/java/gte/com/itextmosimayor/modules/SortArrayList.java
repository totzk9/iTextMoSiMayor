package gte.com.itextmosimayor.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import gte.com.itextmosimayor.models.LastMessagesData;

public class SortArrayList {

    public static ArrayList<LastMessagesData> sortLastMessagesToDate(ArrayList<LastMessagesData> mLastMessagesData) {
        ArrayList<LastMessagesData> newList = new ArrayList<>();
        Collections.sort(mLastMessagesData, new Comparator<LastMessagesData>() {
            @Override
            public int compare(LastMessagesData o1, LastMessagesData o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });
        int size = mLastMessagesData.size() - 1;

        for (int i = size; i >= 0; i--)
            newList.add(mLastMessagesData.get(i));

        return newList;
    }
}
