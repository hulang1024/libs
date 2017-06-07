
/**
Easyui界面复用组件
@author hulang
@date 2017-05-04
 */
var UICommon = {
    datagrid: {
        formatter: {
            generators: {}
        }
    }
};

UICommon._fullCellValueDialog = function(dgId, rowIndex, field) {
    var row = $("#" + dgId).datagrid("getRows")[rowIndex];
    var divDlg = $("<div>" + row[field] + "</div>");
    divDlg.appendTo($(document.body));
    divDlg.dialog({
        title: "查看完整内容",
        width: 500,
        height: 400,
        closed: false,
        cache: false,  
        modal: true   
    });
}
UICommon.datagrid.formatter.generators.omit = function(params) {
    params.min = params.min || 10;
    return function(value, rowData, rowIndex) {
        if(!value) return "";
        var code = 'UICommon._fullCellValueDialog('
            + "'" + params.dgId + "'" + ','
            + rowIndex + ','
            + "'" + params.field + "'" + ')';
        return '<a class="easyui-linkbutton" data-options="iconCls:\'color:fff\'" href="#"'
            + 'onclick="' + code + '" >'
            + (value.substring(0, params.min) + (value.length > params.min ? " ..." : "")) + '</a>';
    };
}
UICommon.datagrid.formatter.generators.dict = function(dict) {
    var map = dict.toMap();
    return function(value,rowData,rowIndex) {
        return map[value];
    }
}
UICommon.datagrid.formatter.wraptip = function(value, rowData, rowIndex) {
    if(!value) return "";
    return '<span title="' + value  + '" >' + value + '</span>';
}
UICommon.datagrid.formatter.money = function(value,rowData,rowIndex) {
    return value.toString().indexOf(".") == -1 ? value + ".00" : value;
}
