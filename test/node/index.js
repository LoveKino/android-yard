'use strict';

let adbCon = require('../../server');

let spawnp = require('spawnp');

let path = require('path');

let sleep = (duration) => {
    return new Promise((resolve) => {
        setTimeout(resolve, duration);
    });
};

let lunchApp = (testPkgDir) => {
    // update communication library
    return spawnp('./updateLibrary.sh', [
        'messchunkpc',
        '../Container/messchunkpc',
        testPkgDir
    ], {
        cwd: path.join(__dirname, '..'),
        stdio: 'inherit'
    }).then(() => {
        // run tests
        return spawnp('./gradlew cAT', [], {
            cwd: testPkgDir,
            stdio: 'inherit'
        });
    });
};

describe('index', () => {
    it('base', () => {
        let testPkgName = 'com.freekite.android.yard.adbcontest1';
        let testFile = `/data/user/0/${testPkgName}/files/aosp_hook/command.json`;
        let testPkgDir = path.join(__dirname, './AdbconTest1');

        let {
            connect
        } = adbCon();

        connect(testFile, {
            add: (a, b) => a + b,
            test: (a, b) => {
                a['new'] = b;
                return a;
            }
        });

        return lunchApp(testPkgDir).then(() => {
            return sleep(1000);
        });
    });
});
