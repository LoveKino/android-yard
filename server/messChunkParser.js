'use strict';

let Parser = require('stream-token-parser');

let {
    forEach
} = require('bolzano');

const LINE_PREFIX = '____mess_chunk_bridge_pc_line_start____';

const LINE_END = '____mess_chunk_bridge_pc_line_end____';

const BLOCK_END = '__mess_chunk_bridge_pc_block_end___';

const ID_END = '|';

let parse = Parser([{
    word: new RegExp(`${LINE_PREFIX}.*${ID_END}`),
    name: 'block_line_start',
    isPart: (v) => {
        if (LINE_PREFIX.indexOf(v) === 0) {
            return true;
        } else if (v.indexOf(LINE_PREFIX) === 0) {
            return v.indexOf(ID_END) === -1 || v[v.length - 1] === ID_END;
        }
        return false;
    },
    priority: 8
}, {
    word: LINE_END,
    name: 'block_line_end',
    priority: 8
}, {
    word: new RegExp(`${BLOCK_END}.*`),
    name: 'block_end',
    isPart: (v) => {
        if (BLOCK_END.indexOf(v) === 0) {
            return true;
        } else if (v.indexOf(BLOCK_END) === 0) {
            return v.indexOf('\n') === -1;
        }
        return false;
    }
}, {
    word: /[\s\S]/,
    name: 'mess',
    priority: 0
}]);

/**
 * node default chunk max size is 8192
 *
 * send log data chunk by chunk, make each chunk's size less than 8192
 *
 * data format
 *
 * some mess ${BLOCK_BOUNDRY}${chunkId}${CHUNK_ID_DELIMITER} ... ${BLOCK_BOUNDRY_END}
 * some mess ${BLOCK_BOUNDRY}${chunkId}${CHUNK_ID_DELIMITER} ... ${BLOCK_BOUNDRY_END}
 * some mess ${BLOCK_BOUNDRY}${chunkId}${CHUNK_ID_DELIMITER} ... ${BLOCK_BOUNDRY_END}
 * ...
 * some mess ${BLOCK_END}${chunkId}
 *
 *
 * eg:
 *
 * noise...
 * noise...____mess_chunk_bridge_pc_line_start____[0]|
 * {"channel":"[0]",
 * ____mess_chunk_bridge_pc_line_end____
 *
 * noise...
 * ____mess_chunk_bridge_pc_line_start____[0]|
 * "data":{"type": "request", "data": {"id": 1, "source":{"type":"public", "name":"add", "args":[{"type":"jsonItem","arg":1},{"type":"jsonItem","arg":2}]}}}}____mess_chunk_bridge_pc_line_end____
 *
 * noise... __mess_chunk_bridge_pc_block_end___[0]
 */

let messParser = () => {
    let chunkMap = {};

    return (chunk) => {
        // add \n to flush
        let tokens = parse(chunk.toString() + '\n');
        let lineFlag = false;
        let blockLine = '';

        let blocks = [];

        forEach(tokens, ({
            text, tokenType
        }) => {
            if (tokenType.name === 'block_line_start') {
                lineFlag = text.substring(LINE_PREFIX.length, text.length - ID_END.length);
            } else if (tokenType.name === 'block_line_end') {
                chunkMap[lineFlag] = chunkMap[lineFlag] || [];
                chunkMap[lineFlag].push(blockLine);
                lineFlag = false;
                blockLine = '';
            } else if (tokenType.name === 'block_end') {
                let id = text.substring(BLOCK_END.length);
                if (chunkMap[id]) {
                    let data = chunkMap[id].join('');
                    delete chunkMap[id];
                    blocks.push(data);
                }
            } else {
                if (lineFlag !== false) {
                    blockLine += text;
                }
            }
        });
        return blocks;
    };
};

messParser.LINE_PREFIX = LINE_PREFIX;
messParser.LINE_END = LINE_END;
messParser.BLOCK_END = BLOCK_END;
messParser.ID_END = ID_END;

module.exports = messParser;
