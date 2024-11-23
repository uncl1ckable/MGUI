package me.unclickable.mgui.netty.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateInfo {

    private static final Gson GSON = new GsonBuilder().create();

    private int port;
    private ServerData serverData;

    public String serialize() {
        return GSON.toJson(this);
    }

    public static UpdateInfo deserialize(String json) {
        return GSON.fromJson(json, UpdateInfo.class);
    }

}
