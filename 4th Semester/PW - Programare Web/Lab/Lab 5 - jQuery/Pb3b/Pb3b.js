var valori = [];
var tile_ids = [];
var flipped = 0;
var variabile = ['Bilie.jpg', 'Bilie.jpg', 'Curcubeu.png', 'Curcubeu.png', 'Dipper.jpg', 'Dipper.jpg', 'Dipper1.jpg', 'Dipper1.jpg', 'Emo.jpg', 'Emo.jpg', 'Gemenii.png', 'Gemenii.png', 'Gemenii1.png', 'Gemenii1.png', 'Ginger.png', 'Ginger.png', 'Maven.jpg', 'Maven.jpg', 'Maven1.jpg', 'Maven1.jpg', 'Maven2.png', 'Maven2.png', 'Nebunul.png', 'Nebunul.png','Ochelari.png','Ochelari.png','Pig.jpg','Pig.jpg','Pig1.jpg','Pig1.jpg','Pig2.jpg','Pig2.jpg','UnchiulStan.png','UnchiulStan.png','Pig3.jpg','Pig3.jpg'];

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
        tile.innerHTML = '<img src="' + val + '">';
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
                    $("#board").empty();
                    newPatrat();
                }
            } else {
                function reIntoarcere() {
                    var tile_1 = $("#" + tile_ids[0]);
                    var tile_2 = $("#" + tile_ids[1]);
                    tile_1.css("background", "pink");
                    tile_1.empty();
                    tile_2.css("background", "pink");
                    tile_2.empty();
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
    $("#board").html(output);
    for (var i = 0; i < variabile.length; i++) {
        $("#tile_" + i).click(function() {
            var index = parseInt($(this).attr("id").split("_")[1]);
            FlipTile($(this)[0], variabile[index]);
        });
    }
}
$(document).ready(function() {
    newPatrat();
});
