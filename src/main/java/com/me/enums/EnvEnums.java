package com.me.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenya on 2017/11/28.
 */
public enum EnvEnums {
    MAGIC(1, "console"),
    MAGIC2(2, "service"),
    TEMPLATE_PATH(3, "MAILBOT_TEMPLATE_PATH"),
    WORK_PATH(4, "MAILBOT_WORKING_PATH");

    private final int id;
    private final String name;

    private EnvEnums(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private static Map<Integer, RunModeEnums> idMap = new HashMap<Integer, RunModeEnums>();
    private static Map<String, RunModeEnums> nameMap = new HashMap<String, RunModeEnums>();

    static {
        for (RunModeEnums type : RunModeEnums.values()) {
            idMap.put(type.getId(), type);
            nameMap.put(type.getName(), type);
        }
    }

    public static RunModeEnums getById(int id) {
        return idMap.get(id);
    }
    public static RunModeEnums getByName(String name) {
        return idMap.get(name);
    }
}
