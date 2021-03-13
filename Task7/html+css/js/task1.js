'use strict';

/**
 * You must return a date that comes in a predetermined number of seconds after 01.06.2020 00:00:002020
 * @param {number} seconds
 * @returns {Date}
 *
 * @example
 *      31536000 -> 01.06.2021
 *      0 -> 01.06.2020
 *      86400 -> 02.06.2020
 */
function secondsToDate(seconds) {
    if(seconds < 0) {
        throw "Seconds should be positive";
    }
    var t = new Date("2020, 1, 6");
    t.setSeconds(seconds);
    return t;
}

/**
 * You must create a function that returns a base 2 (binary) representation of a base 10 (decimal) string number
 * ! Numbers will always be below 1024 (not including 1024)
 * ! You are not able to use parseInt
 * @param {number} decimal
 * @return {string}
 *
 * @example
 *      5 -> "101"
 *      10 -> "1010"
 */
function toBase2Converter(decimal) {
    return decimal.toString(2);
}

/**
 * You must create a function that takes two strings as arguments and returns the number of times the first string
 * is found in the text.
 * @param {string} substring
 * @param {string} text
 * @return {number}
 *
 * @example
 *      'a', 'test it' -> 0
 *      't', 'test it' -> 2
 *      'T', 'test it' -> 2
 */
function substringOccurrencesCounter(substring, text) {
    return (text.match(substring) || []).length;
}

/**
 * You must create a function that takes a string and returns a string in which each character is repeated once.
 *
 * @param {string} string
 * @return {string}
 *
 * @example
 *      "Hello" -> "HHeelloo"
 *      "Hello world" -> "HHeello  wworrldd" // o, l is repeated more then once. Space was also repeated
 */
function repeatingLitters(string) {
    var repeatedString = "";
    string.split("").forEach(l => {
        if (string.indexOf(l) == string.lastIndexOf(l)) {
            repeatedString += l.repeat(2);
        } else {
            repeatedString += l.repeat(1);
        }
    });
    return repeatedString;
}

/**
 * You must write a function redundant that takes in a string str and returns a function that returns str.
 * ! Your function should return a function, not a string.
 *
 * @param {string} str
 * @return {function}
 *
 * @example
 *      const f1 = redundant("apple")
 *      f1() ➞ "apple"
 *
 *      const f2 = redundant("pear")
 *      f2() ➞ "pear"
 *
 *      const f3 = redundant("")
 *      f3() ➞ ""
 */
function redundant(str) {
    function redundant() {
        return str;
    };
    return redundant;
}

/**
 * https://en.wikipedia.org/wiki/Tower_of_Hanoi
 *
 * @param {number} disks
 * @return {number}
 */
function towerHanoi(disks) {
    if(disks <= 0) {
        throw "amount of disks should be positive";
    }
    return Math.pow(2, disks) - 1;
}

/**
 * You must create a function that multiplies two matricies (n x n each).
 *
 * @param {array} matrix1
 * @param {array} matrix2
 * @return {array}
 *
 */
function matrixMultiplication(matrix1, matrix2) {
    if(matrix1[0].length != matrix2.length) {
        throw "amount of columns of the first matrix should equal amount of rows of the second matrix";
    }
    var aRowsNumber = matrix1.length, aColumnsNumber = matrix1[0].length,
        bColumnsNumber = matrix2[0].length,
        multiplication = new Array(aRowsNumber);
    for (var r = 0; r < aRowsNumber; r++) {
        multiplication[r] = new Array(bColumnsNumber);
        for (var c = 0; c < bColumnsNumber; c++) {
            multiplication[r][c] = 0;
            for (var i = 0; i < aColumnsNumber; i++) {
                multiplication[r][c] += matrix1[r][i] * matrix2[i][c];
            }
        }
    }
    return multiplication;
}

/**
 * Create a gather function that accepts a string argument and returns another function.
 * The function calls should support continued chaining until order is called.
 * order should accept a number as an argument and return another function.
 * The function calls should support continued chaining until get is called.
 * get should return all of the arguments provided to the gather functions as a string in the order specified in the order functions.
 *
 * @param {string} str
 * @return {string}
 *
 * @example
 *      gather("a")("b")("c").order(0)(1)(2).get() ➞ "abc"
 *      gather("a")("b")("c").order(2)(1)(0).get() ➞ "cba"
 *      gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get()  ➞ "hello"
 */
function gather(str) {

}