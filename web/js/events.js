function addOrganization(orgTable, key) {

    var tableRef = document.getElementById(orgTable);
    var newRow = tableRef.insertRow(-1);

    var newCell = newRow.insertCell(0);
    var newElem = document.createElement('input');
    newElem.setAttribute("name", key);
    newElem.setAttribute("type", "text");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(1);
    newElem = document.createElement('input');
    newElem.setAttribute("name", key);
    newElem.setAttribute("type", "url");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(2);
    newElem = document.createElement('input');
    newElem.setAttribute("name", key);
    newElem.setAttribute("type", "month");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(3);
    newElem = document.createElement('input');
    newElem.setAttribute("name", key);
    newElem.setAttribute("type", "month");
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(4);
    newElem = document.createElement('textarea');
    newElem.setAttribute("name", key);
    newElem.setAttribute("rows", "2");
    newElem.setAttribute("cols", "50");
    newCell.appendChild(newElem);

}

window.SomeDeleteRowFunction = function SomeDeleteRowFunction(o) {
    var p = o.parentNode.parentNode;
    p.parentNode.removeChild(p);
}