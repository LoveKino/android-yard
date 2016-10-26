package com.ddchen.bridge.messchunkpc;

import android.content.Context;

import com.ddchen.bridge.messchunkpc.MonitorCommand.ExecuteCommand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yuer on 10/19/16.
 */

/**
 * {
 * channel: "/data/user/0/com.freekite.android.yard.adbcontest1/files/aosp_hook/command.json",
 * data: {
 * type: "request",
 * data: {
 * id: 1,
 * source: {
 * type: "public",
 * name: "add",
 * args: [{
 * type: "jsonItem",
 * arg: 1
 * }, {
 * type: "jsonItem",
 * arg: 2
 * }]
 * }
 * }
 * }
 * }
 * <p>
 * {
 * type: "response",
 * data: {
 * data: 3,
 * id: "1"
 * }
 * }
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

    public interface SandboxFunction {
        Object apply(Object[] args);
    }

    public static Caller pc(Context context, final String channel, final Map sandbox) {
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
                    /**
                     * {
                     *      type: "response",
                     *      data: {
                     *          data: 3,
                     *          id: "1"
                     *      }
                     * }
                     *
                     * {
                     *      type: "request",
                     *      data: {
                     *          id: "122",
                     *          source: {
                     *              type: "public",
                     *              name: "testCallback",
                     *              args: [{type: "jsonItem", arg: 1}]
                     *          }
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
                        JSONObject data = jObject.getJSONObject("data");
                        String id = data.getString("id");
                        JSONObject source = data.getJSONObject("source");
                        String methodName = source.getString("name");
                        JSONArray args = source.getJSONArray("args");
                        try {
                            // TODO accept request from server
                            if (sandbox.containsKey(methodName)) {
                                SandboxFunction fun = (SandboxFunction) sandbox.get(methodName);
                                Object[] params = new Object[args.length()];
                                for (int i = 0; i < args.length(); i++) {
                                    JSONObject arg = args.getJSONObject(i);
                                    params[i] = arg.get("arg");
                                }
                                Object ret = fun.apply(params);
                                JSONObject wrapData = new JSONObject();

                                JSONObject responseData = new JSONObject();
                                responseData.put("id", id);
                                responseData.put("data", ret);

                                wrapData.put("type", "response");
                                wrapData.put("data", responseData);

                                JSONObject response = new JSONObject();

                                response.put("channel", channel);
                                response.put("data", wrapData);

                                MessSender.send(response);
                            } else {
                                throw new Error("missing method " + methodName);
                            }
                        } catch (Exception error) {
                            JSONObject wrapData = new JSONObject();
                            JSONObject responseData = new JSONObject();

                            JSONObject errorData = new JSONObject();
                            errorData.put("msg", error.toString());
                            responseData.put("id", id);
                            responseData.put("error", errorData);

                            wrapData.put("type", "response");
                            wrapData.put("data", responseData);

                            JSONObject response = new JSONObject();

                            response.put("channel", channel);
                            response.put("data", wrapData);

                            MessSender.send(response);
                        }
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
                // generate id
                String id = UUID.randomUUID().toString();
                // map id
                idMap.put(id, handleCallResult);

                String argsStr = "";
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    String argItem = "{" +
                            "\"type\":\"jsonItem\"," +
                            "\"arg\":" + arg.toString() +
                            "}";
                    if (argsStr.equals("")) {
                        argsStr = argItem;
                    } else {
                        argsStr += "," + argItem;
                    }
                }
                // construct request json

                String requestJson = "{\n" +
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
                        "}";
                try {
                    MessSender.send(new JSONObject(requestJson).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
