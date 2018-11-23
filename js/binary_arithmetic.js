/*
全加器
@param c 来自相邻低位的进位
@param a 被加数
@param b 加数
@param result [和，进位]
*/
function fullAdder(c, a, b, result) {
    var s = c ^ a ^ b;
    var c = ((a & c) || (b & c)) || (a & b)
    result[0] = s;
    result[1] = c;
}

/*二进制数 加法*/
function addBits(a, b) {
    var sumBits = [];
    var r = [0, 0];
    fullAdder(r[1], a[7], b[7], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[6], b[6], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[5], b[5], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[4], b[4], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[3], b[3], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[2], b[2], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[1], b[1], r);
    sumBits.unshift(r[0]);
    fullAdder(r[1], a[0], b[0], r);
    sumBits.unshift(r[0]);
    return sumBits;
}

/*二进制数 减法*/
function subtractBits(a, b) {
    return addBits(a, binaryToComplementBinary(b));
}

/*有符号十进制整数转换到原码二进制*/
function signedDecimalToBinary(n) {
    var bits = [];
    n = n > 0 ? n : -n;
    while (n > 0) {
        bits.unshift(n % 2);
        n = Math.floor(n / 2);
    }
  	//补0
    for (var i = 8 - bits.length; i > 0; i--) {
        bits.unshift(0);
    }
    return bits;
}

/*有符号十进制整数转换到补码二进制*/
function signedDecimalToComplementBinary(n) {
    var bits = signedDecimalToBinary(n);
    if (n >= 0) {
        return bits;
    } else {
        return binaryToComplementBinary(bits);
    }
}

/*二进制转换到有符号十进制整数*/
function binaryToSignedDecimal(bits) {
    var signBit = bits[0];
    //求原码
    if (signBit == 1) {
        bits = binaryToComplementBinary(bits);
    }
    var n = 0;
    for (var i = bits.length - 1; i > 0; i--) {
        n += bits[i] * Math.pow(2, bits.length - i - 1);
    }
    return n * (signBit == 0 ? 1 : -1);
}

/* 二进制的补码 */
function binaryToComplementBinary(bits) {
    //取反
    bits[7] = +!bits[7];
    bits[6] = +!bits[6];
    bits[5] = +!bits[5];
    bits[4] = +!bits[4];
    bits[3] = +!bits[3];
    bits[2] = +!bits[2];
    bits[1] = +!bits[1];
    bits[0] = +!bits[0];
    //加1
    return addBits(bits, [0,0,0,0,0,0,0,1]);
}

function add(a, b) {
    return binaryToSignedDecimal(
        addBits(
            signedDecimalToComplementBinary(a),
            signedDecimalToComplementBinary(b)));
}

function subtract(a, b) {
    return binaryToSignedDecimal(
        subtractBits(
            signedDecimalToComplementBinary(a),
            signedDecimalToComplementBinary(b)));
}

//test

console.log(add(6, 7));
console.log(add(-6, 7));
console.log(add(6, -7));
console.log(subtract(89, 2));
console.log(subtract(2, 89));