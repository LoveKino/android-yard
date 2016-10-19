'use strict';

let messChunkPC = require('../../server/messChunkPC');
let messChunkParser = require('../../server/messChunkParser');
let assert = require('assert');
let net = require('net');

describe('mess chunk test', () => {
    it('index', (done) => {
        let server = net.createServer((c) => {
            let send = (channel, data) => {
                c.write(JSON.stringify({
                    channel, data
                }));
            };

            let {
                connect
            } = messChunkPC((handler) => {
                c.on('data', (chunk) => {
                    handler(chunk);
                });
            }, send);

            connect('[0]', {
                add: (a, b) => {
                    return a + b;
                }
            });
        });

        server.listen(8124);

        let client = net.createConnection({
            port: 8124
        }, () => {
            client.write(`noise...
                noise...${messChunkParser.LINE_PREFIX}[0]|
                {"channel":"[0]",
                ${messChunkParser.LINE_END}`);

            setTimeout(() => {
                client.write(`noise...
                    ${messChunkParser.LINE_PREFIX}[0]|
                    "data":{"type": "request", "data": {"id": 1, "source":{"type":"public", "name":"add", "args":[{"type":"jsonItem","arg":1},{"type":"jsonItem","arg":2}]}}}}${messChunkParser.LINE_END}`);

                setTimeout(() => {
                    client.write(`***${messChunkParser.BLOCK_END}[0]`);
                }, 1000);
            }, 1000);
        });

        client.on('data', (chunk) => {
            let data = JSON.parse(chunk.toString());
            assert.deepEqual(data, {
                channel: '[0]',
                data: {
                    type: 'response',
                    data: {
                        data: 3,
                        id: 1
                    }
                }
            });
            done();
        });
    });
});
