'use strict';

let {
    connect, sendCommand
} = require('../../server');

let spawnp = require('spawnp');

let path = require('path');

let sleep = (duration) => {
    return new Promise((resolve) => {
        setTimeout(resolve, duration);
    });
};

let lunchApp = () => {
    return spawnp('./lunchApp.sh com.freekite.android.yard.testapp1 com.freekite.android.yard.testapp1.MainActivity', [], {
        cwd: path.join(__dirname, '../../bin')
    }).then(() => {
        return sleep(2000);
    });
};

describe('index', () => {
    it('base', () => {
        return lunchApp().then(() => {
            connect();
            sendCommand('/data/user/0/com.freekite.android.yard.testapp1/files/aosp_hook/command.json', 'testtttttt!!!!!');
        }).then(() => {
            return sleep(1000);
        });
    });
});
