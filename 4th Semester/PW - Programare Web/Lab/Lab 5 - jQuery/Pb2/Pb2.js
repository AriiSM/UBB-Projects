$(document).ready(function() {
    $('#fdata').change(function() {
        calculateAge($(this).val());
    });

    $('form[name="myForm"]').submit(function(e) {
        e.preventDefault(); // Prevent form submission

        var error = '';

        var fname = $('#fname').val();
        var fdata = $('#fdata').val();
        var fvarsta = $('#fvarsta').val();
        var femail = $('#femail').val();

        // Validare nume
        if ($.trim(fname) === '') {
            error += 'Campul nume nu este completat.\n';
            $('#fname').css('border', 'thick solid red');
        } else {
            $('#fname').css('border', 'thick solid green');
        }

        // Validare data
        if ($.trim(fdata) === '') {
            error += 'Campul data nu este completat.\n';
            $('#fdata').css('border', 'thick solid red');
        } else {
            $('#fdata').css('border', 'thick solid green');
        }

        // Validare varsta
        if ($.trim(fvarsta) === '') {
            error += 'Campul varsta nu este completat.\n';
            $('#fvarsta').css('border', 'thick solid red');
        } else {
            $('#fvarsta').css('border', 'thick solid green');
        }

        // Validare email
        if ($.trim(femail) === '') {
            error += 'Campul email nu este completat.\n';
            $('#femail').css('border', 'thick solid red');
        } else {
            $('#femail').css('border', 'thick solid green');
        }

        if (error !== '') {
            alert(error);
            return false;
        } else {
            alert('Datele sunt completate corect.');
            return false;
        }
    });
});

function calculateAge(dateString) {
    var today = new Date();
    var birthDate = new Date(dateString);
    var age = today.getFullYear() - birthDate.getFullYear();
    var m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    $('#varsta').val(age);
}
