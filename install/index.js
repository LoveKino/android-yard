'use strict';

let spawnp = require('spawnp');
let path = require('path');

/**
 * @param yardDir
 *      eg: /data/user/0/com.android.freekite.patch.aosppatch
 */
module.exports = (yardDir) => {
    return spawnp('./deployYard.sh', [
        yardDir,
        path.join(__dirname, '../target/yard-dex.jar')
    ], {
        cwd: __dirname,
        stdio: 'inherit'
    });
};
