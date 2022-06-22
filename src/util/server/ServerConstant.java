package util.server;

import bitzero.server.config.ConfigHandle;

public class ServerConstant {
    public static final String PLAYER_INFO = "player_info";

    public static final String GAME_DATA_KEY_PREFIX = ConfigHandle.instance().get("game_data_key_prefix").trim();
    public static final int FARM_ID_COUNT_FROM = Integer.valueOf(ConfigHandle.instance().get("farm_id_count_from"));
    public static final String FARM_ID_KEY = ConfigHandle.instance().get("farm_id_key").trim();
    public static final String SEPERATOR = ConfigHandle.instance().get("key_name_seperator").trim();

    
    public static final int CUSTOM_LOGIN = ConfigHandle.instance().getLong("custom_login").intValue();

    public static final int CACHE_EXP_TIME = 259200;
}
