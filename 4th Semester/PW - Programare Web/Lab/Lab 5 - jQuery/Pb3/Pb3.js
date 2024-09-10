$(document).ready(function() {
    var valori = [];
    var tile_ids = [];
    var flipped = 0;
    var variabile = ['A', 'A', 'B', 'B', 'C', 'C', 'D', 'D', 'E', 'E', 'F', 'F', 'G', 'G', 'H', 'H', 'I', 'I', 'J', 'J', 'K', 'K', 'L', 'L','M','M','N','N','O','O','P','P','Q','Q','R','R'];

    Array.prototype.tile_shuffle = function() {
        var i = this.length,
            j, temp;
        while (--i > 0) {
            j = Math.floor(Math.random() * (i + 1));
            temp = this[j];
            this[j] = this[i];
            this[i] = temp;
        }
    }

    function FlipTile(tile, val) {
        if (tile.innerHTML == "" && valori.length < 2) {
            tile.style.background = 'lightpink';
            tile.innerHTML = val;
            if (valori.length == 0) {
                valori.push(val);
                tile_ids.push(tile.id);
            } else if (valori.length == 1) {
                valori.push(val);
                tile_ids.push(tile.id);
                if (valori[0] == valori[1]) {
                    flipped += 2;
                    valori = [];
                    tile_ids = [];
                    if (flipped == variabile.length) {
                        alert("Game over!");
                        $('#board').html('');
                        newPatrat();
                    }
                } else {
                    function reIntoarcere() {
                        var tile_1 = $('#' + tile_ids[0]);
                        var tile_2 = $('#' + tile_ids[1]);
                        tile_1.css('background', 'pink');
                        tile_1.html('');
                        tile_2.css('background', 'pink');
                        tile_2.html('');
                        valori = [];
                        tile_ids = [];
                    }
                    setTimeout(reIntoarcere, 700);
                }
            }
        }
    }

    function newPatrat() {
        flipped = 0;
        var output = '';
        variabile.tile_shuffle();
        for (var i = 0; i < variabile.length; i++) {
            output += '<div id="tile_' + i + '"></div>';
        }
        $('#board').html(output);

        $('#board div').each(function(index) {
            $(this).click(function() {
                FlipTile(this, variabile[index]);
            });
        });
    }

    newPatrat();
});
