'use strict';

let messChunkParser = require('../../server/messChunkParser');

let assert = require('assert');

describe('messChunkParser', () => {
    it('base', () => {
        let parse = messChunkParser();
        let blocks = parse(`noise...
                noise...${messChunkParser.LINE_PREFIX}[0]${messChunkParser.ID_END}1234${messChunkParser.LINE_END}

                ${messChunkParser.BLOCK_END}[0]
            `);
        assert.deepEqual(blocks, ['1234']);
    });

    it('chunk', () => {
        let parse = messChunkParser();
        parse(`noise...
                noise...${messChunkParser.LINE_PREFIX}[0]${messChunkParser.ID_END}1234${messChunkParser.LINE_END}

            `);

        let blocks = parse(`
                ${messChunkParser.BLOCK_END}[0]
            `);

        assert.deepEqual(blocks, ['1234']);
    });

    it('chunk2', () => {
        let parse = messChunkParser();
        parse(`noise...
                noise...${messChunkParser.LINE_PREFIX}[0]${messChunkParser.ID_END}1234${messChunkParser.LINE_END}

            `);
        parse(`noise...
                noise...${messChunkParser.LINE_PREFIX}[0]${messChunkParser.ID_END}4657${messChunkParser.LINE_END}`);

        let blocks = parse(`
                ${messChunkParser.BLOCK_END}[0]
            `);

        assert.deepEqual(blocks, ['12344657']);
    });
});
