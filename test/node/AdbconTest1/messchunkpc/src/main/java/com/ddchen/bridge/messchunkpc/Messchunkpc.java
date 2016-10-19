package com.ddchen.bridge.messchunkpc;

import android.content.Context;

import com.ddchen.bridge.messchunkpc.MonitorCommand.ExecuteCommand;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by yuer on 10/19/16.
 */

public class Messchunkpc {
    private static HashMap idMap = new HashMap();

    public interface Caller {
        void call(String name, Object[] args, HandleCallResult handleCallResult);
    }

    public interface HandleCallResult {
        void handle(Object json);

        void handleError(JSONObject errorInfo);
    }

    public static Caller pc(Context context, final String channel) {
        MonitorCommand.monitor(context, new ExecuteCommand() {
            @Override
            public void execute(String command) {
                /**
                 * 1. parse command (call command)
                 * 2. execute command
                 * 3. send results
                 */
                try {
                    JSONObject jObject = new JSONObject(command);
                    System.out.println(jObject);
                    System.out.println(jObject.getString("type"));
                    /**
                     * {
                     *      type: "response",
                     *      data: {
                     *          data: 3,
                     *          id: 1
                     *      }
                     * }
                     */
                    if (jObject.getString("type").equals("response")) {
                        JSONObject responseData = jObject.getJSONObject("data");
                        String id = responseData.getString("id");
                        HandleCallResult handleCallResult = (HandleCallResult) idMap.get(id);
                        if (handleCallResult != null) {
                            if (responseData.has("error") && responseData.get("error") != null) {
                                JSONObject errorInfo = (JSONObject) responseData.get("error");
                                handleCallResult.handleError(errorInfo);
                            } else {
                                handleCallResult.handle(responseData.get("data"));
                            }
                            idMap.remove(id);
                        } else {
                            System.out.println("missing id " + id + " for id map." + "response json is " + jObject);
                        }
                    } else if (jObject.getString("type").equals("request")) {
                        // TODO accept request from server
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return new Caller() {
            /**
             * {
             *     channel: "/data/user/0/com.freekite.android.yard.adbcontest1/files/aosp_hook/command.json",
             *     data: {
             *         type: "request",
             *         data: {
             *             id: 1,
             *             source: {
             *                 type: "public",
             *                 name: "add",
             *                 args: [{
             *                     type: "jsonItem",
             *                     arg: 1
             *                 }, {
             *                     type: "jsonItem",
             *                     arg: 2
             *                 }]
             *             }
             *         }
             *     }
             * }
             */
            @Override
            public void call(String name, Object[] args, HandleCallResult handleCallResult) {
                System.out.println("++++++++++++++++++++++++");
                // generate id
                String id = UUID.randomUUID().toString();
                // map id
                idMap.put(id, handleCallResult);

                String argsStr = null;
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    String argItem = "{" +
                            "\"type\":\"jsonItem\"," +
                            "\"arg\":" + arg.toString() +
                            "}";
                    if (argsStr == null) {
                        argsStr = argItem;
                    } else {
                        argsStr += "," + argItem;
                    }
                }
                System.out.println(argsStr);
                // construct request json
                MessSender.send("{\n" +
                        "    \"channel\": \"" + channel + "\",\n" +
                        "    \"data\": {\n" +
                        "        \"type\": \"request\",\n" +
                        "        \"data\": {\n" +
                        "            \"id\": \"" + id + "\",\n" +
                        "            \"source\": {\n" +
                        "                \"type\": \"public\",\n" +
                        "                \"name\": \"" + name + "\",\n" +
                        "                \"args\": [" + argsStr + "]\n" +
                        "            }\n" +
                        "        }\n" +
                        "    }\n" +
                        "}");
            }
        };
    }
}
