function addPosition(posTable, key) {

    var keyInt = key;

    var tableRef = document.getElementById(posTable);
    var newRow = tableRef.insertRow();

    var newCell = newRow.insertCell(0);
    var newElem = document.createElement('input');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("type", "month");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(1);
    newElem = document.createElement('input');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("type", "month");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(2);
    newElem = document.createElement('textarea');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("rows", "2");
    newElem.setAttribute("cols", "50");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(3);
    newElem = document.createElement('input');
    newElem.setAttribute("id", "delPos");
    newElem.setAttribute("value", "Удалить поз.");
    newElem.setAttribute("type", "button");
    newElem.setAttribute("onClick", "SomeDeleteRowFunction(this)")
    newCell.appendChild(newElem);
}

function addOrganization(orgTable, key, rows) {

    var keyInt = key + '.' + rows;

    var tableRef = document.getElementById(orgTable);
    var newRow = tableRef.insertRow(-1);

    var newCell = newRow.insertCell(0);
    var newElem = document.createElement('input');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("type", "text");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(1);
    newElem = document.createElement('input');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("type", "url");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(2);
    var nestedTable = document.createElement('table');
    var tr = nestedTable.insertRow();

    var td = tr.insertCell();
    newElem = document.createElement('input');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("type", "month");
    td.appendChild(newElem);

    td = tr.insertCell();
    newElem = document.createElement('input');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("type", "month");
    td.appendChild(newElem);

    td = tr.insertCell();
    newElem = document.createElement('textarea');
    newElem.setAttribute("name", keyInt);
    newElem.setAttribute("rows", "2");
    newElem.setAttribute("cols", "50");
    td.appendChild(newElem);

    newCell.appendChild(nestedTable);

    newCell = newRow.insertCell(3);
    newElem = document.createElement('input');
    newElem.setAttribute("id", "delOrg");
    newElem.setAttribute("src", "img/delete.png");
    newElem.setAttribute("type", "image");
    newElem.setAttribute("onClick", "SomeDeleteRowFunction(this)")
    newCell.appendChild(newElem);
}

window.SomeDeleteRowFunction = function SomeDeleteRowFunction(o) {
    var p = o.parentNode.parentNode;
    p.parentNode.removeChild(p);
}