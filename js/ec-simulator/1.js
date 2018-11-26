function Agenda() {
    var list = [];

    this.currentTime = function() {
        return 0;
    }

    this.add = function(time, action) {
        list.push()
    }
}

var agenda = new Agenda();

var afterDelay = function(delay, func) {
    agenda.add
}

function makeWire() {
    return {
        value: 0,
        actions: []
    };

}

function getSignal(wire) {
    return wire.value;
}

function setSignal(wire, value) {
    if (wire.value != value) {
        wire.value = value;
        wire.actions.forEach(function(func) {
            func();
        });
    }
}

function addAction(wire, func) {
    wire.actions.push(func);
    func();
}

var halfAdder = function(a, b, s, c) {
    var d = makeWire();
    var e = makeWire();
    orGate(a, b, d);
    andGate(a, b, c);
    inverter(c, e);
    andGate(d, e, s);
}

var fullAdder = function(a, b, cIn, sum, cOut) {
    var s = makeWire();
    var c1 = makeWire();
    var c2 = makeWire();

    halfAdder(b, cIn, s, c1);
    halfAdder(a, s, sum, c2);
    orGate(c1, c2, cOut);
}

var logicalNot = function(s) {
    return !s;
}

var logicalAnd = function(a, b) {
    return a && b;
}

var logicalOr = function(a, b) {
    return a || b;
}



var inverter = function(input, output) {
    addAction(input, function() {
        afterDelay(inverterDelay, function() {
            var newValue = logicalNot(getSignal(input));
            setSignal(output, newValue);
        });
    });
}

var andGate = function(a1, a2, output) {
    var onChange = function() {
        afterDelay(andGateDelay, function() {
            var newValue = logicalAnd(getSignal(a1), getSignal(a2));
            setSignal(output, newValue);
        });
    }
    addAction(a1, onChange);
    addAction(a2, onChange);
}

var orGate = function(a1, a2, output) {
    var w1 = makeWire();
    var w2 = makeWire();
    var w3 = makeWire();
    var onChange = function() {
        afterDelay(orGateDelay, function() {
            inverter(a1, w1);
            inverter(a2, w2);
            andGate(w1, w2, w3);
            inverter(w3, output);
        });
    }

    addAction(a1, onChange);
    addAction(a2, onChange);
}

var rippleCarryAdder = function(aWires, bWires, sumWires, c) {
    for (var i = 0; i < 8; i++) {
        fullAdder(aWires[i], bWires[i], c, sumWires[i], c);
    }
}