/**
 * setup Array method
 * @author hulang
 * @date 2017-04-18
 * */
Array.prototype.map = Array.prototype.map || function(fn) { return _.map(this, fn); };
Array.prototype.forEach = Array.prototype.forEach || function(fn) { return _.forEach(this, fn); };