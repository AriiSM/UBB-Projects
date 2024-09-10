$(document).ready(function() {
    // Aici se muta un obiect dintr-o lista in alta
    $("#unu").on("dblclick", "option", function() {
        var item = $(this).text();
        $(this).remove();
        $("#doi").append($("<option></option>").text(item));
    });

    $("#doi").on("dblclick", "option", function() {
        var item = $(this).text();
        $(this).remove();
        $("#unu").append($("<option></option>").text(item));
    });
});
