$(document).ready(function() {
    $(".sort-header").click(function() {
        var table = $(this).closest("table");
        var columnIndex = $(this).index();
        var rows = table.find("tr:gt(0)").toArray().sort(comparer(columnIndex));
        this.asc = !this.asc;
        if (!this.asc) {
            rows = rows.reverse();
        }
        for (var i = 0; i < rows.length; i++) {
            table.append(rows[i]);
        }
    });
});

function comparer(index) {
    return function(a, b) {
        var valA = getCellValue(a, index);
        var valB = getCellValue(b, index);
        return $.isNumeric(valA) && $.isNumeric(valB) ? valA - valB : valA.toString().localeCompare(valB);
    };
}

function getCellValue(row, index) {
    return $(row).children("td, th").eq(index).text();
}
