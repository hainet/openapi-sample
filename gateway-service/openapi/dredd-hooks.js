const hooks = require('hooks');

hooks.before('/resources/{id} > PUT > 201 > application/json;charset=utf-8', transaction => {
    transaction.fullPath = '/resources/100';
});
