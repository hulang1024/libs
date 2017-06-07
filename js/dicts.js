/**
@class 字典，用于表示业务中数据解释，例如订单状态值
@author hulang
*/
function Dict(dictItems) {
    this.items = dictItems;
}
Dict.prototype.toMap = function() {
    return _.reduce(this.items, function(m, o){
        m[o.value] = o.text;
        return m;
    }, new Object());
}
/**
@class 字典项
@param value 值
@param text 值描述文本
 */
function DictItem(value, text) {
    this.value = value;
    this.text = text;
}

var DICT_SET = {};

DICT_SET['sex'] = new Dict([
    new DictItem(1, "男"),
    new DictItem(0, "女")
]);

DICT_SET['yesno'] = new Dict([
    new DictItem(1, "是"),
    new DictItem(0, "否")
]);