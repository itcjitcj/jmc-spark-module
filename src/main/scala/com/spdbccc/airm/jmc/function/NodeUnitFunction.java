package com.spdbccc.airm.jmc.function;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashSet;

public class NodeUnitFunction {
    public static HashSet<String> set = new HashSet<String>();

    //使用递归获取所有的规则链
    public static JSONObject unit(JSONObject node, String startNode, JSONArray endNode){
        String unit=startNode+",";
        unitNode(node,startNode,endNode,unit);
        JSONObject nodeListUnitJo = JSONUtil.createObj();
        Object[] units = set.toArray();
        for (int i = 0; i < units.length; i++) {
            nodeListUnitJo.set(String.valueOf(i),units[i].toString().split(","));
        }
        return nodeListUnitJo;
    }

    public static void unitNode(JSONObject node, String startNode, JSONArray endNode,String unit){
        JSONObject startnode = node.getJSONObject(startNode);
        JSONArray afterNodeList = startnode.getJSONArray("afterNodeList");
        for (int i = 0; i < afterNodeList.size(); i++) {
            String afterNode = afterNodeList.getStr(i);
            if (endNode.contains(afterNode)){
                set.add(unit+afterNode);
            }else {
                unitNode(node,afterNode,endNode,unit+afterNode+",");
            }
        }

    }
}
