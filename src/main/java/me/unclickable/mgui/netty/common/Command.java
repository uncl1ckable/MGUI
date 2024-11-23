package me.unclickable.mgui.netty.common;

import lombok.Builder;
import lombok.Getter;
import me.unclickable.mgui.netty.CommandType;
import me.unclickable.mgui.utils.GsonUtils;

@Builder
@Getter
public class Command {

    private final CommandType type;
    private final String payload;

    public String serialize() {
        return GsonUtils.serialize(this);
    }

    public static Command deserialize(String json) {
        return GsonUtils.deserialize(json, Command.class);
    }

}