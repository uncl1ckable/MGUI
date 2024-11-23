package me.unclickable.mgui.netty.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerData {

    private String serverKey;

    private int players;
    private int maxPlayers;
    private String version;

    @Builder.Default private Map<String, String> placeholders = Collections.emptyMap();

}