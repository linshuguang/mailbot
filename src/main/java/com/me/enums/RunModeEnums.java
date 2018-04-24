package com.me.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kenya on 2017/11/27.
 */
public enum RunModeEnums {

    CONSOLE(1, "console"),
    SERVICE(2, "service"),
    CLIENT(3, "client");

    private final int id;
    private final String name;

    private RunModeEnums(int id, String name) {
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
