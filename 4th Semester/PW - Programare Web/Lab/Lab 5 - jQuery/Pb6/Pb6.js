$(document).ready(function() {
    var empty, dim = 4;

    generateTable();

    $(document).keydown(function(e) {
        e = e || window.event;
        switch (e.keyCode) {
            case 38: // Up arrow
                if (empty - dim >= 0)
                    swap(empty, empty - dim);
                else
                    alert('I cant move up');
                break;
            case 40: // Down arrow
                if (empty + dim <= dim * dim)
                    swap(empty, empty + dim);
                else
                    alert('I cant move down');
                break;
            case 37: // Left arrow
                if (empty % dim != 0)
                    swap(empty, empty - 1);
                else
                    alert('I cant move left');
                break;
            case 39: // Right arrow
                if (empty % dim != dim - 1)
                    swap(empty, empty + 1);
                else
                    alert('I cant move right');
                break;
        }
    });

    function generateTable() {
        var elem;
        var numbers = generateNumbers(dim * dim);
        var output = "";
        for (let i = 0; i < dim; i++) {
            output += '<tr>';
            for (let j = 0; j < dim; j++) {
                output += `<td id='${i*dim+j}' >`;
                elem = numbers.pop();
                if (elem != dim * dim)
                    output += elem;
                else {
                    empty = i * dim + j;
                }
                output += '</td>';
            }
            output += '</tr>';
        }
        $("#myTable").html(output);
    }

    function swap(id1, id2) {
        var aux = $("#" + id1).html();
        $("#" + id1).html($("#" + id2).html());
        $("#" + id2).html(aux);
        empty = id2;
    }

    function shuffle(a) {
        for (let i = a.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [a[i], a[j]] = [a[j], a[i]];
        }
        return a;
    }

    function generateNumbers(n) {
        array = [];
        for (let i = 1; i <= n; i++) {
            array.push(i);
        }
        shuffle(array);
        return array;
    }
});
