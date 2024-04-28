const Befunge = require('befunge93');
const fs = require('fs')
var arguments = process.argv

let befunge = new Befunge();

fs.readFile(arguments[2], (err, input) => {
    if (err) throw err;
        befunge.run(input.toString())
            .then((output) => {
                console.log(output);
            });
    })