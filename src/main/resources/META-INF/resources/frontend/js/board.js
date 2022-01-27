const board = function () { return document.getElementById("board-frame").contentWindow.document; }

function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

function addPlayer(name, position) {
    var space = board().getElementById("space-" + position);
    if(space == null) {
        setTimeout(function() {
            addPlayer(name, position)
        }, 500);
        return
    }
    var svg = board().createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.id = name;
    svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    svg.setAttribute("viewBox", "0 0 100 100");
    svg.style = "width: 30%; position: relative; margin: 5%; fill: " + getRandomColor();
    var circle = board().createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", "50");
    circle.setAttribute("cy", "50");
    circle.setAttribute("r", "50");
    circle.style = "position: relative";
    svg.appendChild(circle);
    var space = board().getElementById("space-" + position);
    space.appendChild(svg);
}

function movePlayer(name, position) {
    var player = board().getElementById(name);
    if(player == null) {
        addPlayer(name, position);
        player = board().getElementById(name);
    }
    board().getElementById("space-" + position).appendChild(player);
}