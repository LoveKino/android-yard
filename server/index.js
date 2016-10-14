'use strict';

let spawnp = require('spawnp');
let {
    pc
} = require('general-bridge');
let messChunkParser = require('./messChunkParser');
let log = console.log; // eslint-disable-line

let start = ({
    maxOpenTimes = 5
} = {}) => {
    let chunkHandlers = {};

    let adbLogcat = (times = 0) => {
        if (times > maxOpenTimes) {
            throw new Error('fail to connect adb logcat');
        }
        return spawnp('adb logcat', [], null, {
            onChild: (child) => {
                child.stdout.on('data', (chunk) => {
                    for (var commandJsonPath in chunkHandlers) {
                        chunkHandlers[commandJsonPath](chunk);
                    }
                });
            }
        }).then(() => {
            // hangout
            return adbLogcat(++times);
        }).catch(() => {
            // hangout
            return adbLogcat(++times);
        });
    };

    let openP = adbLogcat();

    let connect = (commandJsonPath, sandbox) => {
        return pc((handler, send) => {
            let chunkParser = messChunkParser(commandJsonPath);
            chunkHandlers[commandJsonPath] = (chunk) => {
                let data = chunkParser(chunk);
                if (data !== null) {
                    handler(data, send);
                }
            };
        }, (msg) => sendCommand(commandJsonPath, msg.data), sandbox);
    };

    let disConnect = (commandJsonPath) => {
        delete chunkHandlers[commandJsonPath];
    };

    return {
        connect,
        disConnect,
        openP
    };
};

let sendCommand = (commandJsonPath, cmdStr) => {
    return spawnp(`adb shell echo ${cmdStr} > ${commandJsonPath}`);
};

module.exports = {
    start, sendCommand
};
