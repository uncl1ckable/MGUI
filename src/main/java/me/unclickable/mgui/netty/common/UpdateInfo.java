package me.unclickable.mgui.netty.common;

import lombok.Builder;
import lombok.Getter;
import me.unclickable.mgui.utils.GsonUtils;

@Builder
@Getter
public class UpdateInfo {

    private int port;
    private ServerData serverData;

    public String serialize() {
        return GsonUtils.serialize(this);
    }

    public static UpdateInfo deserialize(String json) {
        return GsonUtils.deserialize(json, UpdateInfo.class);
    }

}
