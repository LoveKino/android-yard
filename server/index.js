'use strict';

let adbPC = require('./adbPC');
let acceptCommand = require('./acceptCommand');

module.exports = () => {
    return adbPC(acceptCommand);
};
