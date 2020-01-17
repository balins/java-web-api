package com.balinski.api_project.util;

import com.balinski.api_project.database.model.DatabaseModel;

import java.util.List;

public class JsonResponseBuilder {
    public static String mergeFromList(List<? extends DatabaseModel> list) {
        if(list == null) {
            return "{\"errors\":[{\"title\":\"Internal server error\"," +
                    "\"detail\":\"Could not obtain an instance of DAO for given model\"}],\"data\":[]}";
        }

        StringBuilder sb = new StringBuilder("{\"errors\":\"false\",\"data\":[");

        if(list.size() > 0) {
            for (var model : list)
                sb.append(model.asJson()).append(",");

            sb.setLength(sb.length() - 1);
        }

        sb.append("]}");

        return sb.toString();
    }
}
