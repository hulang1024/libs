function logicalNot(b) {
    if (b == 0) return 1;
    if (b == 1) return 0;
}

function logicalAnd(a, b) {
    return a == 1 && b == 1 ? 1 : 0;
}

function logicalOr(a, b) {
    return logicalNot(logicalAnd(logicalNot(a), logicalNot(b)));
}

function xor(a, b) {
    return logicalAnd(logicalOr(a, b), logicalNot(logicalAnd(a, b)));
}

function halfAdder(a, b, s, c) {
    s[0] = xor(a, b);
    c[0] = logicalAnd(a, b);
}

function fullAdder(cIn, a, b, sumOuts, sumOutIndex, cOut) {
    var _sumOut = [];
    var _cOut1 = [];
    var _cOut2 = [];
    halfAdder(a, b, _sumOut, _cOut1);
    halfAdder(_sumOut[0], cIn, _sumOut, _cOut2);
    sumOuts[sumOutIndex] = _sumOut[0];
    cOut[0] = logicalOr(_cOut1, _cOut2);
}

function addBits(a, b) {
    var sumBits = [];
    var c = [0];
    fullAdder(c[0], a[7], b[7], sumBits, 7, c);
    fullAdder(c[0], a[6], b[6], sumBits, 6, c);
    fullAdder(c[0], a[5], b[5], sumBits, 5, c);
    fullAdder(c[0], a[4], b[4], sumBits, 4, c);
    fullAdder(c[0], a[3], b[3], sumBits, 3, c);
    fullAdder(c[0], a[2], b[2], sumBits, 2, c);
    fullAdder(c[0], a[1], b[1], sumBits, 1, c);
    fullAdder(c[0], a[0], b[0], sumBits, 0, c);
    return sumBits;
}

/* 二进制的补码 */
function binaryToComplementBinary(bits) {
    //取反
    bits[7] = logicalNot(bits[7]);
    bits[6] = logicalNot(bits[6]);
    bits[5] = logicalNot(bits[5]);
    bits[4] = logicalNot(bits[4]);
    bits[3] = logicalNot(bits[3]);
    bits[2] = logicalNot(bits[2]);
    bits[1] = logicalNot(bits[1]);
    bits[0] = logicalNot(bits[0]);
    //加1
    return addBits(bits, [0,0,0,0,0,0,0,1]);
}

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