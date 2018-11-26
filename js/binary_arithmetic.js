function logicalNot(b) {
    if (b == 0) return 1;
    if (b == 1) return 0;
}

function logicalAnd(a, b) {
    return (a == 1 && b == 1) ? 1 : 0;
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

function invertBits(bits, invertSignBit) {
    var iBits = [];
    iBits[7] = logicalNot(bits[7]);
    iBits[6] = logicalNot(bits[6]);
    iBits[5] = logicalNot(bits[5]);
    iBits[4] = logicalNot(bits[4]);
    iBits[3] = logicalNot(bits[3]);
    iBits[2] = logicalNot(bits[2]);
    iBits[1] = logicalNot(bits[1]);
    if (invertSignBit)
        iBits[0] = logicalNot(bits[0]);
    return iBits;
}

/* 反码 */
function onesComplementBinary(bits) {
    var signBit = bits[0];
    var cBits = [];
    if (signBit == 1) {
        //取反
        cBits = invertBits(bits, false);
    } else {
        cBits[7] = bits[7];
        cBits[6] = bits[6];
        cBits[5] = bits[5];
        cBits[4] = bits[4];
        cBits[3] = bits[3];
        cBits[2] = bits[2];
        cBits[1] = bits[1];
        cBits[0] = bits[0];
    }
    return cBits;
}

function binaryAdd1(bits) {
    return addBits(bits, [0, 0, 0, 0, 0, 0, 0, 1]);
}

/* 补码 */
function binaryToTwosComplementBinary(bits) {
    var signBit = bits[0];
    if (signBit == 1) {
        bits = onesComplementBinary(bits);
        bits = binaryAdd1(bits); //加1
    }
    return bits;
}

function subtractBits(a, b) {
    return addBits(a, binaryAdd1(invertBits(b, true)));
}

function decimalToTrueCodeBinary(n) {
    var bits = decimalToMathBinary(n);
    //补0足8位
    for (var i = 8 - bits.length; i > 0; i--) {
        bits.unshift(0);
    }
    //在最高位设置符号位
    bits[0] = n >= 0 ? 0 : 1;
    return bits;
}

/* 十进制整数转换到数学二进制 */
function decimalToMathBinary(n) {
    var bits = [];
    n = n >= 0 ? n : -n;
    while (n > 0) {
        bits.unshift(n % 2);
        n = Math.floor(n / 2);
    }
    return bits;
}

/*二进制原码转换到十进制正整数*/
function mathBinaryToDecimal(bits) {
    var n = 0;
    for (var i = bits.length - 1; i >= 0; i--) {
        n += bits[i] * Math.pow(2, bits.length - i - 1);
    }
    return n;
}

function twosComplementBinaryToDecimal(bits) {
    var signBit = bits[0];
    return mathBinaryToDecimal(binaryToTwosComplementBinary(bits).slice(1)) * (signBit == 0 ? 1 : -1);
}

function add(a, b) {
    return twosComplementBinaryToDecimal(addBits(
        binaryToTwosComplementBinary(decimalToTrueCodeBinary(a)),
        binaryToTwosComplementBinary(decimalToTrueCodeBinary(b))));
}

function subtract(a, b) {
    return twosComplementBinaryToDecimal(subtractBits(
        binaryToTwosComplementBinary(decimalToTrueCodeBinary(a)),
        binaryToTwosComplementBinary(decimalToTrueCodeBinary(b))));
}


//test
(function() {
    console.assert(mathBinaryToDecimal([0, 1, 1, 1, 1, 1, 1, 1]) == 127 && 127 == Math.pow(2, 7) - 1);
    for (var n = 0, m = -1; n < 127; n = add(n, 1)) {
        testAdd(n, 1);
        console.assert(n - m == 1);
        m = n;
    }
    testAdd(6, 7);
    testAdd(-6, 7);
    testAdd(6, -7);
    testSubtract(89, 2);
    testSubtract(2, 89);

    function testAdd(a, b) {
        var expect = a + b;
        var actual = add(a, b);
        var equals = expect == actual;
        console.log('Test add(%d, %d) = %d, %c%s', a, b, actual,
            'color:' + (equals ? 'green' : 'red'), equals ? 'ok' : 'fail');
    }

    function testSubtract(a, b) {
        var expect = a - b;
        var actual = subtract(a, b);
        var equals = expect == actual;
        console.info('Test subtract(%d, %d) = %d, %c%s', a, b, actual,
            'color:' + (equals ? 'green' : 'red'), equals ? 'ok' : 'fail');
    }
})();
