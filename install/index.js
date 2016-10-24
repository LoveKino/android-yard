'use strict';

let spawnp = require('spawnp');

/**
 * @param yardDir
 *      eg: /data/user/0/com.android.freekite.patch.aosppatch
 */
module.exports = (yardDir) => {
    return spawnp('./deployYard.sh', [yardDir, '../target/yard-dex.jar'], {
        cwd: __dirname
    });
};
