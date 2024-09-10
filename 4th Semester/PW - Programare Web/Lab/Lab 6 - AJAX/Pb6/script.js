const getFilteredNotebooks = (filterBy, filterValue) => {
    $.ajax({
        type: "GET",
        url: `notebooks.php?filterBy=${filterBy}&filterValue=${filterValue}`,
        success: response => $("#notebooksTable").html(response)
    });
}

const getAllNotebooks = () => {
    $.ajax({
        type: "GET",
        url: "notebooks.php",
        success: response => $("#notebooksTable").html(response)
    });
}

$(document).ready(() => {
    getAllNotebooks();
    $("#clearFilters").click(getAllNotebooks);
    $("#filterNotebooks").submit(function(ev) {
        ev.preventDefault();
        const filterBy = $(this).find('select[name="filterBy"]').val();
        const filterValue = $(this).find('input[name="filterValue"]').val();
        if (filterValue === "") {
            alert("Please enter a filtering value!");
            return;
        }
        getFilteredNotebooks(filterBy, filterValue);
    });
});
