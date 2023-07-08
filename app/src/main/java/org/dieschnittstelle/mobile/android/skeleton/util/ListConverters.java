package org.dieschnittstelle.mobile.android.skeleton.util;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListConverters {

    private static final String SEPERATOR = ";";

    @TypeConverter
    public static String fromList(List<String> stringList){
        if(stringList == null){
            return "";
        }
        return stringList.stream().collect(Collectors.joining(SEPERATOR));
    }

    @TypeConverter
    public static List<String> fromString(String strings){
        if(strings == null){
            return new ArrayList<>();
        }
        return Arrays.stream(strings.split(SEPERATOR)).collect(Collectors.toList());
    }
}
