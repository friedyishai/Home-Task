/*******************************************************/
// HTML Elements
/*******************************************************/
const setSizeDiv = document.querySelector('.set-size');
const setLengthDiv = document.querySelector('.set-win-length');
const statusDiv = document.querySelector('.status');
const resetDiv = document.querySelector('.reset');
let cells;
let winSequence;

/*******************************************************/
// Information variables
/*******************************************************/
const minSize = 3;
const minWinLength = 3;
const startCellSize = 300;
let gameInProgress;
let xTurn;
let counter;
let size;
let lengthToWin;

/*******************************************************/
// Events Handlers
/*******************************************************/

/*for board's set size input*/
const setSizeHandler = () => {
    let val = parseInt(setSizeDiv.lastElementChild.value);
    let len = parseInt(setLengthDiv.lastElementChild.value);

    if (val < minSize) {
        val = minSize;
        alert('The minimum size of game board is ' + minSize + ' x ' + minSize);
        setSizeDiv.lastElementChild.value = '' + minSize;
    }

    if (len > val) {
        setLengthDiv.lastElementChild.value = '' + val;
    }

    initBoard();
}

/*for board's set length of win input*/
const setWinLengthHandler = () => {
    let val = parseInt(setSizeDiv.lastElementChild.value);
    let len = parseInt(setLengthDiv.lastElementChild.value);

    if (len < minSize) {
        alert('The minimum length to win is ' + minSize);
        setLengthDiv.lastElementChild.value = '' + lengthToWin;
    }

    if (len > val) {
        alert('The maximum length to win is ' + size);
        setLengthDiv.lastElementChild.value = '' + val;
    }

    initBoard();
}

/*for reser button*/
const resetClickHandler = () => {
    initBoard();
};


/*for game board cells*/
const cellClickHandler = (e) => {
    const currentCell = e.target;
    const list = currentCell.classList;

    if (!gameInProgress) {
        alert('The game is over, press Reset to start a new one.');
        return;
    }

    if (list.contains('invalid')) {
        alert('This cell is occupied.');
        return;
    }

    currentCell.classList.add('invalid');

    if (xTurn) {
        list.add('x');
    } else {
        list.add('o');
    }
    xTurn = !xTurn;
    counter++;

    let currentStatus = checkStatus();

    if (currentStatus === 'x') { // X won.
        gameInProgress = false;
        statusDiv.innerHTML = 'The winner is X!';
        for (let i = 0; i < lengthToWin; i++) {
            winSequence[i].classList.remove('x');
            winSequence[i].classList.add('x-win');
        }
        invalidBoard();
    } else if (currentStatus === 'o') { // O won.
        gameInProgress = false;
        statusDiv.innerHTML = '<span class="o-status">The winner is O!</span>';
        for (let i = 0; i < lengthToWin; i++) {
            winSequence[i].classList.remove('o');
            winSequence[i].classList.add('o-win');
        }
        invalidBoard();
    } else if (currentStatus === 'Draw') { // Draw.
        gameInProgress = false;
        statusDiv.innerHTML = 'It\'s a Tie!';
    } else { //Continue play.
        if (xTurn) {
            statusDiv.innerHTML = 'X\'s Turn';
        } else {
            statusDiv.innerHTML = '<span class="o-status">O\'s Turn</span>';
        }
    }
};

/*******************************************************/
// Event Listeners
/*******************************************************/
setSizeDiv.addEventListener('change', setSizeHandler);
setLengthDiv.addEventListener('change', setWinLengthHandler);
resetDiv.addEventListener('click', resetClickHandler);

/*******************************************************/
// Functions
/*******************************************************/

/*Initial the game board, by default size of board is 3x3*/
function initBoard() {
    size = parseInt(setSizeDiv.lastElementChild.value);
    lengthToWin = parseInt(setLengthDiv.lastElementChild.value);

    setLengthDiv.lastElementChild.setAttribute('max', '' + size);

    gameInProgress = true;
    xTurn = true;
    counter = 0;
    statusDiv.innerHTML = 'X\'s Turn ';

    let cellsList = '';

    /*creating cells list*/
    for (let i = 0; i < size * size; i++) {
        cellsList += '<div class="cell"></div>';
    }

    /*adding cells list to game board*/
    let board = document.querySelector('.game-board');
    board.innerHTML = cellsList;
    board.style.gridTemplateColumns = 'repeat(' + size + ', 1fr)';
    board.style.gridTemplateRows = 'repeat(' + size + ', 1fr)';

    cells = document.querySelectorAll('.cell');

    /*set cells size*/
    for (const cell of cells) {
        cell.addEventListener('click', cellClickHandler);
        let cellSize = startCellSize / size;
        cell.style.fontSize = cellSize + 'px';
        cell.style.height = cellSize + 'px';
        cell.style.width = cellSize + 'px';
    }
}

/*checks current status*/
function checkStatus() {
    let result = checkRows();

    if (result !== "None") // -> result = "X" or result = "O" or result = "Draw".
        return result;

    result = checkCols();

    if (result !== "None")
        return result;

    result = checkDiagonals();
    return result;
}

/*General method for checking a valid line (row / column / diagonal).*/
function checkLines(i, endOfLine, jIncrease, jBound) {
    for (let j = i; endOfLine - j >= jBound; j += jIncrease) {
        let current = cells[j];

        if (!current || !current.classList.contains('invalid')) {
            continue;
        }

        let symbol = current.classList[2];
        winSequence = [current];
        let toCheck = cells[j + jIncrease];

        while (j < endOfLine && toCheck && toCheck.classList.contains(symbol)) {
            j += jIncrease;
            winSequence.push(toCheck);
            if (winSequence.length === lengthToWin) {
                return "" + symbol;
            }
            toCheck = cells[j + jIncrease];
        }
    }

    return "None";
}

/*Checks if there is a row filled with the same character (X / O).*/
function checkRows() {
    for (let i = 0; i < size * size; i += size) {
        let endOfLine = i + size - 1;
        let result = checkLines(i, endOfLine, 1, lengthToWin - 1);
        if (result !== "None") {
            return result;
        }
    }

    return "None";
}

/*Checks if there is a column filled with the same character (X / O).*/
function checkCols() {
    for (let i = 0; i < size; i++) {
        let endOfLine = i + (size - 1) * size;
        let result = checkLines(i, endOfLine, size, (lengthToWin - 1) * size);
        if (result !== "None") {
            return result;
        }
    }

    return "None";
}

/*Checks if there is a left to right or right to left diagonal filled with the same character (X / O)*/
function checkDiagonals() {
    let result = checkLeftDiagonals();

    if (result !== "None")
        return result;

    result = checkRightDiagonals();

    return result;
}

/*Checks if 1 of the left to right diagonals filled with the same character (X / O).*/
function checkLeftDiagonals() {

    /*Main diagonal and above*/
    for (let i = 0; size - i >= lengthToWin; i++) {
        let endOfLine = size * (size - i) - 1;
        let result = checkLines(i, endOfLine, size + 1, (lengthToWin - 1) * (size + 1));
        if (result !== "None") {
            return result;
        }
    }

    /*Below main diagonal*/
    for (let i = size, k = 2; size * size - i >= lengthToWin * size; i += size, k++) {
        let endOfLine = size * size - k;
        let result = checkLines(i, endOfLine, size + 1, (lengthToWin - 1) * (size + 1));
        if (result !== "None") {
            return result;
        }
    }

    return "None";
}

/*Checks if 1 of the right to left diagonals filled with the same character (X / O).*/
function checkRightDiagonals() {

    /*Main diagonal and above*/
    for (let i = size - 1, k = size; size * size - i > size * (lengthToWin - 1); i += size, k--) {
        let endOfLine = size * size - k;
        let result = checkLines(i, endOfLine, size - 1, (lengthToWin - 1) * (size - 1));
        if (result !== "None") {
            return result;
        }
    }

    /*Below main diagonal*/
    for (let i = size - 2; i >= lengthToWin - 1; i--) {
        let endOfLine = size * i;
        let result = checkLines(i, endOfLine, size - 1, (lengthToWin - 1) * (size - 1));
        if (result !== "None") {
            return result;
        }
    }

    return boardIsFull() ? "Draw" : "None";
}

/*Checks if the board is full*/
function boardIsFull() {
    return counter === size * size;
}

/*Disables all board buttons at the end of the game**/
function invalidBoard() {
    for (let i = 0; i < cells.length; i++) {
        let current = cells[i];

        if (!current.classList.contains('invalid')) {
            current.classList.add('invalid');
        }
    }
}

/*******************************************************/
// Default Start
/*******************************************************/
initBoard();