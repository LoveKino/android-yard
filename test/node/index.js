'use strict';

let adbCon = require('../../server');

let assert = require('assert');

let spawnp = require('spawnp');

let path = require('path');

let sleep = (duration) => {
    return new Promise((resolve) => {
        setTimeout(resolve, duration);
    });
};

let runAppTest = (testPkgDir) => {
    return spawnp('./gradlew cAT', [], {
        cwd: testPkgDir,
        stdio: 'inherit'
    });
};

let lunchApp = (pkgName, mainPath, testPkgDir) => {
    // update communication library
    return spawnp('./updateLibrary.sh', [
        'messchunkpc',
        '../Adbcon/messchunkpc',
        testPkgDir
    ], {
        cwd: path.join(__dirname, '..'),
        stdio: 'inherit'
    }).then(() => {
        return spawnp('../installApp.sh', [
            testPkgDir,
            pkgName
        ], {
            cwd: __dirname,
            stdio: 'inherit'
        });
    }).then(() => {
        // run tests
        return spawnp('../../bin/lunchApp.sh', [
            pkgName,
            mainPath
        ], {
            cwd: __dirname,
            stdio: 'inherit'
        });
    }).then(() => {
        return sleep(4000);
    });
};

describe('index', () => {
    it('call app\' api', () => {
        let testPkgName = 'com.freekite.android.yard.adbcontest2';
        let mainPath = `${testPkgName}.MainActivity`;
        let testFile = `/data/user/0/${testPkgName}/files/aosp_hook/command.json`;
        let testPkgDir = path.join(__dirname, './AdbconTest2');
        let commandDir = `/data/user/0/${testPkgName}/files/aosp_hook/output`;

        let {
            connect
        } = adbCon();

        return lunchApp(testPkgName, mainPath, testPkgDir).then(() => {
            let call = connect(testFile, commandDir, {});
            return call('subtraction', [6, 2]).then(ret => {
                assert.equal(ret, 4);
            });
        });
    });

    it('call from app', () => {
        let testPkgName = 'com.freekite.android.yard.adbcontest1';
        let testFile = `/data/user/0/${testPkgName}/files/aosp_hook/command.json`;
        let testPkgDir = path.join(__dirname, '../../Adbcon');
        let commandDir = `/data/user/0/${testPkgName}/files/aosp_hook/output`;

        let {
            connect
        } = adbCon();

        connect(testFile, commandDir, {
            add: (a, b) => a + b,
            test: (a, b) => {
                a['new'] = b;
                return a;
            },
            error: () => {
                throw new Error('error test');
            }
        });

        return runAppTest(testPkgDir).then(() => {
            return sleep(1000);
        });
    });
});
