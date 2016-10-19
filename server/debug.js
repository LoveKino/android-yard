'use strict';

let adbCon = require('./index');

let {
    connect
} = adbCon();

connect('/data/user/0/com.freekite.android.yard.adbcontest1/files/aosp_hook/command.json', {
    add: (a, b) => {
        return a + b;
    },
    test: (a, b) => {
        a['new'] = b;
        return a;
    }
});
