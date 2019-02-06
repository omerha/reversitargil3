$(function () { // onload...do
    $("#formLogin").submit(function () {
        /*
                             now we're going to capture *all* the fields in the
                             form and submit it via ajax.

                             :input is a macro that grabs all input types, select boxes
                             textarea, etc.  Then using the context of the form from
                             the initial '#contactForm' to narrow down our selector

                                      var inputs = [];
                                      $(":input", this).each(function() {
                                        inputs.push(this.name + '=' + escape(this.value));
                                      });
                             now if I join our inputs using '&' we'll have a query string
                                  var parameters = inputs.join('&');
        */

        var parameters = $(this).serialize();


        $.ajax({
            data: parameters,
            url: "loginServlet",
            timeout: 2000,
            error: function () {
                console.error("Failed to submit");
                console.log("fail");
            },
            success: function (r) {
              console.log("Success");
            }
        });

        // return value of the submit operation
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    })
});
