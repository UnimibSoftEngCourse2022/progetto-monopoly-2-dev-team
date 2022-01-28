const board = function () { return document.getElementById("board-frame").contentWindow.document; }
var displayNameDiv;

function addPlayer(player) {
    var space = board().getElementById("space-" + player.position);
    if(space == null) {
        setTimeout(function() {
            addPlayer(player)
        }, 500);
        return
    }
    var svg = board().createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.id = player.id;
    svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
    svg.setAttribute("viewBox", "0 0 100 100");
    svg.style = "width: 35%; position: relative; margin: 5%; fill: " + player.color + ";";
    var circle = board().createElementNS("http://www.w3.org/2000/svg", "circle");
    circle.setAttribute("cx", "50");
    circle.setAttribute("cy", "50");
    circle.setAttribute("r", "50");
    circle.style = "position: relative";
    svg.appendChild(circle);
    space.appendChild(svg);

    svg.onmouseover = function (e) {
        showPlayerName(player.displayName);
    }
    svg.onmouseout = function (e) {
        hidePlayerName();
    }
}

function movePlayer(player) {
    var element = board().getElementById(player.id);
    if(element == null) {
        addPlayer(player);
    } else {
        board().getElementById("space-" + player.position).appendChild(element);
    }
}

function showPlayerName(name) {
    if(displayNameDiv == null) {
        var boardDiv = board().getElementById("monopoly-board");
        if(boardDiv == null) {
            return
        }
        displayNameDiv = board().createElement("div");
        board().onmousemove = function (e) {
            if(displayNameDiv != null && displayNameDiv.style.display == "block") {
                displayNameDiv.style.left = e.pageX + "px";
                displayNameDiv.style.top = e.pageY + "px";
            }
        }
        boardDiv.appendChild(displayNameDiv);
        displayNameDiv.id = "display-name";
        displayNameDiv.style = "background: rgba(255, 255, 255, 0.8); position: absolute; font: system-ui; border-radius: 5px;";
        displayNameDiv.style.padding = "2px"
    } else if (displayNameDiv.parentNode == null) {
        var boardDiv = board().getElementById("monopoly-board");
        if(boardDiv != null) {
            boardDiv.appendChild(displayNameDiv);
        }
    }
    displayNameDiv.style.display = "block";
    displayNameDiv.innerHTML = name;
}

function hidePlayerName() {
    if(displayNameDiv != null) {
        displayNameDiv.style.display = "none"
    }
}

function removePlayer(nameId) {
    var svg = board().getElementById(nameId);
    if(svg != null) {
        svg.parentNode.removeChild(svg);
    }
}