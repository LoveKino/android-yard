'use strict';

const CHUNK_BOUNDARY = '[*mess-chunk-bridge-pc-line*]';

const CHUNK_ID_DELIMITER = '|';

const CHUNK_END = '[*mess-chunk-bridge-pc-end*]';

/**
 * node default chunk max size is 8192
 *
 * send log data chunk by chunk, make each chunk's size less than 8192
 *
 * data format
 *
 * some mess ${commandJsonPath}${CHUNK_BOUNDARY}${chunkId}${CHUNK_ID_DELIMITER} ...
 * some mess ${commandJsonPath}${CHUNK_BOUNDARY}${chunkId}${CHUNK_ID_DELIMITER} ...
 * some mess ${commandJsonPath}${CHUNK_BOUNDARY}${chunkId}${CHUNK_ID_DELIMITER} ...
 * ...
 * some mess ${commandJsonPath}${CHUNK_END}${chunkId}
 */

module.exports = (commandJsonPath) => {
    let chunkMap = {};
    return (chunk) => {
        let str = chunk.toString();
        if (str.indexOf(commandJsonPath) === -1) return null;
        let boundryIndex = str.indexOf(CHUNK_BOUNDARY);
        if (boundryIndex !== -1) {
            let next = str.substring(boundryIndex + 1);
            let chunkIdIndex = next.indexOf(CHUNK_ID_DELIMITER);
            if (chunkIdIndex !== -1) {
                let chunkId = next.substring(0, CHUNK_ID_DELIMITER);
                chunkMap[chunkId] = chunkMap[chunkId] || [];
                chunkMap[chunkId].push(next.substring(CHUNK_ID_DELIMITER + 1));
            }
        } else {
            let chunkEndIndex = str.indexOf(CHUNK_END);
            if (chunkEndIndex !== -1) {
                let chunkId = str.indexOf(chunkEndIndex + 1);
                let data = chunkMap[chunkId].join('');
                delete chunkMap[chunkId];

                return data;
            }
        }

        return null;
    };
};


