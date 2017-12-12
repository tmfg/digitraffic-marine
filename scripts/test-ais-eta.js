const https = require('https');

https.get('https://meri.digitraffic.fi/api/v1/metadata/vessels/230991010', (resp) => {
  let data = '';

  // A chunk of data has been recieved.
  resp.on('data', (chunk) => {
    data += chunk;
  });

  // The whole response has been received. Print out the result.
  resp.on('end', () => {
    console.log('JSON:', JSON.parse(data));
    let eta = JSON.parse(data).eta;
    let bits0to5 = eta & 0b11111;
    let bits6to10 = eta >>> 5 & 0b11111;
    let bits11to15 = eta >>> 10 & 0b11111;
    let bits16to19 = eta >>> 15 & 0b01111;
    console.log('eta:', eta);
    console.log('Bits 5-0: minute:', bits0to5);
    console.log('Bits 10-6: hour:', bits6to10);
    console.log('Bits 15-11: day:', bits11to15);
    console.log('Bits 19-16: month:', bits16to19);
  });

}).on("error", (err) => {
  console.log("Error: " + err.message);
});