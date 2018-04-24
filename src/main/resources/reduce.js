var page = require('webpage').create();
   page.open('file:///%s', function(status) {
           if (status !== 'success') {
                       //    console.log('Unable to access network');
           } else {
               var ua = page.evaluate(function() {
                   return document.getElementsByTagName('table')[0].innerHTML;
               });
               console.log(ua);
         }
       phantom.exit();
  });