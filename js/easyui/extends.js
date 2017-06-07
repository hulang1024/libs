$.extend($.fn.datagrid.methods, {
   clear: function(o) {
       o.datagrid("loadData", {total:0,rows:[]});
   }
});