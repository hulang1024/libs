/**
@author hulang
@version 1.0
*/
var DateSelector = function(options) {
    var elem = $(options.elem);
    
    // 初始化参数
    var nowYear = new Date().getFullYear();
    var minYear = options.minYear || nowYear;
    var maxYear = options.maxYearIsNow ? nowYear : options.maxYear || minYear + 50;
    options.formatter = options.formatter || function(value, selectEnables) {
        var vals = [];
        selectEnables['year'] && vals.push(leftPad(value.year, 4, 0));
        selectEnables['month'] && vals.push(leftPad(value.month, 2, 0));
        selectEnables['day'] && vals.push(leftPad(value.day, 2, 0));
        return vals.join('-');
    };
    
    var selects = []; // selects
    var keys = ['year', 'month', 'day']; // select keys
    var selectEnables = {'year': true, 'month': true, 'day': true}; // select启用情况表
    
    
    // 初始化年选择列表
    var yearSelect = document.createElement('select');
    var years = [];
    if(options.yearOrder == 'desc') {
        // 当前年份 ~ minYear
        for(var n = maxYear; n >= minYear; n--)
            years.push(n);
    } else {
        // minYear ~ 当前年份
        for(var n = minYear; n <= maxYear; n++)
            years.push(n);
    }
    yearSelect.options.add( new Option('', 0) );
    years.forEach(function(y){
        yearSelect.options.add( new Option(y + '年', y) );
    });
    $(yearSelect).change(function(){
        updateDayOptions();
        sync();
    });
    selects.push(yearSelect);
    
    // 初始化月选择列表
    var monthSelect = document.createElement('select');
    monthSelect.options.add( new Option('', 0) );
    for(var n = 1; n <= 12; n++)
        monthSelect.options.add( new Option(leftPad(n, 2, '0') + '月', n) );
    $(monthSelect).change(function(){
        updateDayOptions();
        sync();
    });
    selects.push(monthSelect);
    
    // 初始化日选择列表
    var daySelect = document.createElement('select');
    daySelect.onchange = sync;
    selects.push(daySelect);
    
    // add class
    selects.forEach(function(select){
        $(select).addClass('dateselector-select');
        $(select).css('border', '1px solid gray');
    });
    
    // 插入DOM
    elem.hide();
    elem.after(daySelect);
    elem.after(monthSelect);
    elem.after(yearSelect);
    
    var that = {
        // 'year', 'month' 或 'day'
        setUnit: function(u) {
            if(typeof u != 'undefined') {
                var mid = keys.indexOf(u);
                for(var i = 0; i < mid + 1; i++) {
                    selectEnables[ keys[i] ] = true;
                    $(selects[i]).show();
                }
                for(var i = mid + 1; i < selects.length; i++) {
                    selectEnables[ keys[i] ] = false;
                    $(selects[i]).hide();
                }
                
                this.clear();
            }
        },
        // YYYY-MM-DD
        setValue: function(value) {
            var vals = value.split('-').map(Number);
           
            var k;
            if(vals[0] > maxYear) {
                vals[0] = maxYear;
                k = 1;
            } else if(vals[0] < minYear) {
                vals[0] = minYear;
                k = 1;
            }
            for(var i = k; i < vals.length; i++)
                vals[i] = 1;
            for(var i = 0; i < vals.length; i++) {
                selects[i].value = vals[i];
                if(i == 1) {//month
                    updateDayOptions();
                }
            }
            
            sync();
        },
        getValue: function() {
            return options.formatter(
                {year: yearSelect.value, month: monthSelect.value, day: daySelect.value},
                selectEnables);
        },
        clear: function() {
            for(var i = 0; i < selects.length; i++)
                selects[i].selectedIndex = 0;
            elem.val('');
        },
        isFull: function() {
            for(var i = 0; i < selects.length; i++)
                if(selectEnables[ keys[i] ] && selects[i].selectedIndex <= 0)
                    return false;
            return true;
        }
    };
    
    return that;
    

    function updateDayOptions() {
        /// 根据月份设置日options
        var DayOption = function(n) {
            return new Option(leftPad(n, 2, '0') + '日', n);
        }
        var numDayOpt;
        numDayOpt = daySelect.options.length - 1;
        // 如果未初始化就初始化为28天
        if(numDayOpt <= 0) {
            daySelect.options.add( new Option('', 0) );
            for(var i = 1; i <= 28; i++)
                daySelect.options.add( new DayOption(i) );
        }
        numDayOpt = daySelect.options.length - 1;
        // 根据月份最大天数，增加或删除现有options
        if(numDayOpt > 0) {
            var maxDay = maxDayOfMonth(yearSelect.value, monthSelect.value);
            var diff = maxDay - numDayOpt;
            if(diff > 0) {
                for(var i = 1; i <= diff; i++)
                    daySelect.options.add( new DayOption(numDayOpt + i) );
            } else {
                for(var i = 0, diff = Math.abs(diff); i < diff; i++)
                    daySelect.options.remove( numDayOpt - i );
            }
        }
    }
    
    function maxDayOfMonth(year, month) {
        /* 1,3,5,7,8,10,12 这几月永远31天；
           2月平年28天而闰年29天其他月份30天；     
        */
        if([1,3,5,7,8,10,12].indexOf(month) > -1) return 31;
        else if(month == 2) return isLeapYear(year) ? 29 : 28;
        else return 30;
    }
    function isLeapYear(y) {
        // 四年一闰，百年不闰，四百年再闰
        return (y % 4 == 0 && y % 100 != 0) || y % 400 == 0;  
    }

    
    function sync() {
        elem.val( that.getValue() );
    }
    
    function leftPad(str, size, c) {
        str = String(str);
        var pad = '';
        for(var n = size - str.length; n > 0; n--) pad += c;
        return pad + str;
    }
};

var StatQuerier = function(spec) {
    var parentElem = $(spec.elem);
    $(function(){
        function queryBoss() {
            parentElem.find("#cboBoss").combobox({
                valueField:'bossId',
                textField:'realName',
                url: CONFIG.baseUrl + 'shop/boss/bossPage.do',
                queryParams: {page:1, rows:10000},
                required: true,
                loadFilter:function(data){
                    data.rows.unshift({bossId:'0', realName: '全部'});
                    return data.rows;
                },
                onSelect: function(record) {
                    parentElem.find("#frmQuery [name=bossId]").val(record.bossId == '0' ? '' : record.bossId);
                    queryProvider();
                },
                onLoadSuccess: function() {
                    $(this).combobox("select", '0');
                }
            });
        }
        
        function queryProvider(cond) {
            var params = PAGE_CONFIG['providerCond'];
            $.extend(params, parentElem.find('#frmQuery').serializeObject());
            params.page = 1;
            params.rows = 1000;
            parentElem.find("#cboProvider").combobox({
                valueField: 'providerId',
                textField: 'name',
                url: CONFIG.baseUrl + 'shop/pro/proPage.do?' + $.param(params),
                loadFilter: function(data) {
                    data.rows.unshift({providerId: '0', name: "全部"});
                    return data.rows;
                },
                onLoadSuccess: function() {
                    $(this).combobox("select", '0');
                }
            });
        }
        
        queryBoss();
        queryProvider();
    });
    var startDateSelect = new DateSelector({
        elem: parentElem.find("#startTime"), minYear: 2017, maxYearIsNow: true, yearOrder: 'asc'});
    var endDateSelect = new DateSelector({
        elem: parentElem.find("#endTime"), minYear: 2017, maxYearIsNow:true, yearOrder: 'desc'});
    
    parentElem.find("#timeUnit").combobox({
        onSelect: function(record) {
            [startDateSelect, endDateSelect].forEach(function(dateSelector){
                dateSelector.setUnit(record.value);
            });
        }
    });
    
    parentElem.find("[name=query]").click(function(){
        var params = parentElem.find("#frmQuery").serializeObject();
        params.providerId = params.providerId == '0' ? '' : params.providerId;

        var timeUnit = params.timeUnit;
        var MAX_DIFF = 7;
        var format = 'YYYY-MM-DD'.substring(0, {day: 10, month: 7, year: 4}[timeUnit]);
        // 设置未输入值

        if(!startDateSelect.isFull() && !endDateSelect.isFull()) {
            // 默认查询最近一月
            params.startTime = moment().subtract(7-1, timeUnit).format(format);
            params.endTime = moment().format(format);
        }
        else if(!startDateSelect.isFull() && endDateSelect.isFull())
            params.startTime = moment(params.endTime).subtract(timeUnit,MAX_DIFF).format(format);
        else if(startDateSelect.isFull() && !endDateSelect.isFull())
            params.endTime = moment().add(2, 'year').format(format);

        startDateSelect.setValue(params.startTime);
        endDateSelect.setValue(params.endTime);
        params.startTime = startDateSelect.getValue();
        params.endTime = endDateSelect.getValue();
        
        var diffs = timeUnit != 'year' ?
            moment(params.endTime).diff(moment(params.startTime), timeUnit) : params.endTime - params.startTime;
        if(diffs < 0) {
            showAlert("提示", "开始时间大于结束时间", "info");
            return;
        }
        // 判断差值
        if(diffs + 1 > MAX_DIFF) {
            showAlert("提示", "开始时间与结束时间单位相差大于" + MAX_DIFF, "info");
            return;
        }
        
        function ymdInTitle(dateStr, timeUnit) {
            var u = ['年','月','日'];
            var strs = dateStr.substring(0, {day: 10, month: 7, year: 5}[timeUnit]);
            return strs.split('-').map(function(s, i){ return s + u[i]; }).join('');
        }
        params.timeTitle = ymdInTitle(params.startTime);
        if(params.startTime != params.endTime) {
            params.timeTitle += '到' + ymdInTitle(params.endTime);
        }
        
        spec.onQuery(params);
    });
    
    return {
        query: function() {
            parentElem.find("[name=query]").click();
        }
    };
}