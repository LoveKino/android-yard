package com.ddchen.bridge.messchunkpc;

import java.util.UUID;

/**
 * Created by yuer on 10/19/16.
 */

public class MessSender {
    public static final String LINE_PREFIX = "____mess_chunk_bridge_pc_line_start____";
    public static final String LINE_END = "____mess_chunk_bridge_pc_line_end____";
    public static final String BLOCK_END = "__mess_chunk_bridge_pc_block_end___";
    public static final String ID_END = "|";

    public static void send(Object data) {
        String id = UUID.randomUUID().toString();
        String lines[] = data.toString().split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            System.out.println(LINE_PREFIX + id + ID_END + lines[i] + LINE_END);
        }

        System.out.println(BLOCK_END + id + "\n");
    }
}
