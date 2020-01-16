package com.balinski.api_project.util;

import com.balinski.api_project.database.model.Jsonable;

import java.util.List;

public class JsonHelper {
    public static String mergeFromList(List<? extends Jsonable> list) {
        if(list == null) {
            return "{\"error\":true,\"data\":[]}";
        }

        StringBuilder sb = new StringBuilder("{\"error\":false,\"data\":[");

        if(list.size() > 0) {
            for (var jsonable : list)
                sb.append(jsonable.toJson()).append(",");

            sb.setLength(sb.length() - 1);
        }

        sb.append("]}");

        return sb.toString();
    }
}
