'use strict';

let spawnp = require('spawnp');
let {
    reduce, forEach
} = require('bolzano');

/**
 * 1. empty commandDir
 * 2. pool file names
 * 3. if exists file, read it as command
 * 4. delete file after get it's content
 */
module.exports = (commandDir) => {
    const timeGap = 50;

    let fileCommandCache = {}; // cache commands, derepeat

    let scanOutputDir = () => {
        return spawnp(`adb shell [ -d ${commandDir} ]`).then((ret) => {
            if (ret !== 'errored') {
                return spawnp(`adb shell ls ${commandDir}`, [], null, {
                    stdout: true
                }).then(({
                    stdouts
                }) => {
                    let str = stdouts.join('');

                    return reduce(str.split('\n'), (prev, item) => {
                        item = item.trim();
                        if (item) {
                            prev.push(commandDir + '/' + item);
                        }
                        return prev;
                    }, []);
                });
            }
        }).catch(() => {});
    };

    let readCommand = (handler) => {
        return scanOutputDir().then((files) => {
            forEach(files, (file) => {
                if (fileCommandCache[file]) {
                    return;
                }
                fileCommandCache[file] = true;
                //
                spawnp(`adb shell cat ${file}`, [], null, {
                    stdout: true
                }).then(({
                    stdouts
                }) => {
                    spawnp(`adb shell rm -rf ${file}`);
                    handler(stdouts.join(''));
                });
            });
        });
    };

    let stopFlag = false;

    return {
        start: (handler) => {
            fileCommandCache = {};
            stopFlag = false;
            // clear command dir
            return spawnp(`adb shell rm -rf ${commandDir}/*`).then(() => {
                let tick = () => {
                    if (stopFlag) return;
                    readCommand(handler);
                    setTimeout(tick, timeGap);
                };
                // start loop
                tick();
            });
        },

        stop: () => {
            stopFlag = true;
        }
    };
};
