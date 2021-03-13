var rewire = require('rewire');
var chai = require('chai');
var should = chai.should();
var task1 = rewire("../task1.js");


describe('Task1', function() {
    it("seconds to date right output", () => {
        result = task1.__get__('secondsToDate');
        result(0).should.eql(new Date("2020, 1, 6"));
    });
    it("decimal to 2 base right output", () => {
        result = task1.__get__('toBase2Converter');
        result(5).should.equal("101");
    });
    it("substring counter right output", () => {
        result = task1.__get__('substringOccurrencesCounter');
        result("a", "test it").should.equal(0);
    });
    it("repeat letters right output", () => {
        result = task1.__get__('repeatingLitters');
        result("Hello world").should.equal("HHeello  wworrldd");
    });
    it("return func from func right output", () => {
        result = task1.__get__('redundant');
        redundant = result("b");
        redundant().should.equal("b");
    });
    it("tower hanoi right output", () => {
        result = task1.__get__('towerHanoi');
        result(3).should.equal(7);
    });
    it("matrix multiplication right output", () => {
        result = task1.__get__('matrixMultiplication');
        result([[1, 2], [3, 4]], [[5, 6], [7, 8]]).should.eql([[19, 22], [43, 50]]);
    });
});