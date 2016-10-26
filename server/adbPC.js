'use strict';

let {
    pc
} = require('general-bridge');

let spawnp = require('spawnp');

module.exports = (accept) => {
    let handlers = {};
    let accepts = {};

    let connect = (commandJsonPath, commandDir, sandbox) => {
        accepts[commandDir] = accept(commandDir);

        accepts[commandDir].start((outStr) => {
            let {
                channel, data
            } = JSON.parse(outStr);
            let handler = handlers[channel];
            if (handler) {
                handler(data);
            }
        });

        let sendByChannel = (msg) => send(commandJsonPath, msg);
        return pc((handler, send) => {
            handlers[commandJsonPath] = (data) => {
                handler(data, send);
            };
        }, sendByChannel, sandbox);
    };

    let disConnect = (commandJsonPath, commandDir) => {
        delete handlers[commandJsonPath];
        accepts[commandDir].stop();
        delete accepts[commandDir];
    };

    return {
        connect,
        disConnect
    };
};

let send = (commandJsonPath, cmd) => {
    return spawnp('adb shell echo', [`'${JSON.stringify(cmd)}'`, '>', commandJsonPath]);
};
