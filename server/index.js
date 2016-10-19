'use strict';

let spawnp = require('spawnp');
let messChunkPC = require('./messChunkPC');
let log = console.log; // eslint-disable-line

module.exports = ({
    maxOpenTimes = 5
} = {}) => {
    let adbLogcat = (handler, times = 0) => {
        if (times > maxOpenTimes) {
            throw new Error('fail to connect adb logcat');
        }
        return spawnp('adb logcat -c').then(() => {
            return spawnp('adb logcat', [], null, {
                onChild: (child) => {
                    child.stdout.on('data', (chunk) => {
                        // log(chunk.toString());
                        handler(chunk);
                    });
                }
            });
        }).then(() => {
            // hangout
            return adbLogcat(handler, ++times);
        }).catch(() => {
            // hangout
            return adbLogcat(handler, ++times);
        });
    };

    let {
        connect, disConnect
    } = messChunkPC(adbLogcat, sendCommand);

    return {
        connect,
        disConnect
    };
};

let sendCommand = (commandJsonPath, cmd) => {
    return spawnp('adb shell echo', [`'${JSON.stringify(cmd)}'`, '>', commandJsonPath]);
};
