'use strict';

let adbCon = require('./index');

let {
    connect
} = adbCon();

let log = console.log; // eslint-disable-line

let caller = connect('/data/user/0/com.freekite.android.yard.adbcontest1/files/aosp_hook/command.json', {
    add: (a, b) => {
        return a + b;
    },
    test: (a, b) => {
        a['new'] = b;
        return a;
    }
});

setTimeout(() => {
    caller('subtraction', [4, 2]).then(ret => {
        log(ret);
    }).catch(err => {
        log(err);
    });
}, 4000);
