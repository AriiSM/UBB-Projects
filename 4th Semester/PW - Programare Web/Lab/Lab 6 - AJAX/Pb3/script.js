let initData = null;
let currentId = null;
let dataChanged = false;

const getUserIds = () => {
    $.ajax({
        type: "GET",
        url: "userids.php",
        success: (result) => {
            $("#userIds").html(result);
        }
    });
};

const compareWithInit = () => {
    $("#saveBtn").prop("disabled", true);
    dataChanged = false;
    if (JSON.stringify(initData) !== JSON.stringify($("#updateForm").serializeArray())) {
        $("#saveBtn").prop("disabled", false);
        dataChanged = true;
    }
};

const getUserForm = (userId) => {
    $.ajax({
        type: "GET",
        url: "users.php?id=" + userId,
        success: (result) => {
            $("#updateForm").html(result);
            initData = $("#updateForm").serializeArray();
            currentId = userId;
            $("input").on("input", compareWithInit);
            compareWithInit(); // Trigger initial comparison
        }
    });
};

$(document).ready(() => {
    getUserIds();
    $("#userIds").change(function() {
        if (dataChanged && confirm("You haven't saved the user, do you want to save it?")) {
            $("#updateForm").trigger("submit");
        }
        dataChanged = false;
        const value = $(this).val();
        if (value) {
            getUserForm(value[0]);
        }
    });
    $("#updateForm").submit(function(ev) {
        ev.preventDefault();
        if (!confirm("Are you sure you want to update this user?")) {
            return;
        }
        const data = $(this).serializeArray();
        data.push({ name: "id", value: currentId });
        $.ajax({
            type: "POST",
            url: "userupdate.php",
            data: $.param(data),
            success: (result) => {
                alert(result);
                initData = $("#updateForm").serializeArray();
                $("#saveBtn").prop("disabled", true);
                dataChanged = false;
            }
        });
    });
});
