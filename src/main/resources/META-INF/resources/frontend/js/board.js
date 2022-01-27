const board = function () { return document.getElementById("board-frame").contentWindow.document; }

function addPlayer(player) {
    var space = board().getElementById("space-" + player.position);
    if(space == null) {
        setTimeout(function() {
            addPlayer(player)
        }, 500);
        return
    }
    var svg = board().createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.id = player.name;
    svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    svg.setAttribute("viewBox", "0 0 100 100");
    svg.style = "width: 30%; position: relative; margin: 5%; fill: " + player.color + ";";
    var circle = board().createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", "50");
    circle.setAttribute("cy", "50");
    circle.setAttribute("r", "50");
    circle.style = "position: relative";
    svg.appendChild(circle);
    space.appendChild(svg);
}

function movePlayer(player) {
    var element = board().getElementById(player.name);
    if(element == null) {
        addPlayer(player);
    } else {
        board().getElementById("space-" + player.position).appendChild(element);
    }
}