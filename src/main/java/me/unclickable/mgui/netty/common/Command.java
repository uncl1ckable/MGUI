package me.unclickable.mgui.netty.common;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;
import me.unclickable.mgui.netty.CommandType;

@Builder
@Getter
public class Command {

    private static final Gson GSON = new Gson();

    private final CommandType type;
    private final String payload;

    public String serialize() {
        return GSON.toJson(this);
    }

    public static Command deserialize(String json) {
        return GSON.fromJson(json, Command.class);
    }

}