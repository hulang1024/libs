/**
 * $.arrayParam("p", [1,2,3]) == "p=1&p=2&p=3"
 * @author hulang
 */
$.arrayParam = function(name, valueArray){
    return valueArray.map(function(val){
        return name + "=" + val;
    }).join("&");
};
/**
 * $.arrayPickParam("p", [{a:2},{a:2},{a:3}], "a") == "p=1&p=2&p=3"
 * @author hulang
 */
$.arrayPickParam = function(name, objectArray, key){
    return $.arrayParam(name, objectArray.map(function(o) { return _.pick(o, key)[key]; }));
}
