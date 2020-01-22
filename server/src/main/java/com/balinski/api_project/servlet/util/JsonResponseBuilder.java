package com.balinski.api_project.servlet.util;

import com.balinski.api_project.database.model.DatabaseModel;

import java.util.List;

public class JsonResponseBuilder {
    public static String mergeFromList(List<? extends DatabaseModel> list) {
        StringBuilder sb = new StringBuilder("{\"errors\":[],\"data\":[");

        if(list.size() > 0) {
            for (var model : list)
                sb.append(model.asJson()).append(",");

            sb.setLength(sb.length() - 1);
        }

        sb.append("]}");

        return sb.toString();
    }

    public static String getErrorJson(Throwable cause) {
        return String.format("{\"errors\":[{\"title\":\"Error when trying to fetch data\"," +
                "\"detail\":\"%s\"}],\"data\":[]}", cause.getMessage());
    }
}
