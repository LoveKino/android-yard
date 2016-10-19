'use strict';

let {
    pc
} = require('general-bridge');
let {
    forEach
} = require('bolzano');
let messChunkParser = require('./messChunkParser');
let log = console.log; // eslint-disable-line

module.exports = (stream, send) => {
    let handlers = {};
    let chunkParser = messChunkParser();

    stream((chunk) => {
        let blocks = chunkParser(chunk);
        // data format
        // TODO exception of parsing json
        forEach(blocks, (block) => {
            try {
                let {
                    channel, data
                } = JSON.parse(block);
                let handler = handlers[channel];
                if (handler) {
                    handler(data);
                }
            } catch(err) {
                log(block);
                log(err);
            }
        });
    });

    let connect = (commandJsonPath, sandbox) => {
        let sendByChannel = (msg) => send(commandJsonPath, msg);
        return pc((handler, send) => {
            handlers[commandJsonPath] = (data) => {
                handler(data, send);
            };
        }, sendByChannel, sandbox);
    };

    let disConnect = (commandJsonPath) => {
        delete handlers[commandJsonPath];
    };

    return {
        connect,
        disConnect
    };
};
