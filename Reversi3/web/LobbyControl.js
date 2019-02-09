$(function () {
    // $("#uploadForm").submit(function () {
    //
    //     var file1 = this[0].files[0];
    //     var file2 = this[1].files[0];
    //
    //     var formData = new FormData();
    //     formData.append("fake-key-1", file1);
    //     formData.append("fake-key-2", file2);
    //
    //     $.ajax({
    //         method: 'POST',
    //         data: formData,
    //         url: this.action,
    //         processData: false, // Don't process the files
    //         contentType: false, // Set content type to false as jQuery will tell the server its a query string request
    //         timeout: 4000,
    //         error: function (e) {
    //             console.error("Failed to submit");
    //             $("#result").text("Failed to get result from server " + e);
    //         },
    //         success: function (r) {
    //             $("#result").text(r);
    //         }
    //     });
    //
    // });
});

function loadGameClicked(event) {
    var file = event.target.files[0];
    var reader = new FileReader();
    //   var creatorName = getUserName();
    reader.readAsText(file);
    reader.onload = function () {
        console.log("Reader on load");
        var content = reader.result;

        $.ajax(
            {
                type: 'POST',
                url: '/gameServlet',
                data:{ file:content},
                error: function () {
                    console.error("Failed to submit");
                    console.log("fail");
                },
                success: function (r) {
                    console.log("Success");
                }
            }
        );
    };
}
