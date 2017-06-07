
/**
 * 字符串格式化（类方法）
 * 示例:
 * String.format("a_{0}b{1}", 1) == "a_1b{1}"
 * String.format("a_{0}b{1}", 1, 'cat') == "a_1bcat"
 * "a_{0}b{1}".format(1, 'cat') == "a_1bcat"
 * @param 包含占位的字符串 string
 * @param 实际参数 object
 * @return 格式化后的字符串 string
 * @author hulang
 * @date 2017-03-15
 * */
String.format = function() {
  if(arguments.length == 0)
    return "";
  if(arguments.length == 1)
    return arguments[0];
  if(!arguments[0])
      return "";
  var result = arguments[0];
  for (var i = 1; i < arguments.length; i++) {
    result = result.replace(new RegExp('\\{' + (i - 1) + '\\}', 'gm'), arguments[i]);
  }
  return result;
};
/**
 * 字符串格式化
 * 示例:
 * "a_{0}b{1}".format(1, 'cat') == "a_1bcat"
 * @param 实际参数 object
 * @return 格式化后的字符串 string
 * @author hulang
 * @date 2017-03-15
 * */
String.prototype.format = function() {
  if(arguments.length == 0) {
    return "";
  }
  var result = this;
  for (var i = 0; i < arguments.length; i++) {
      result = result.replace(new RegExp('\\{' + (i) + '\\}', 'gm'), arguments[i]);
  }
  return result;
};
