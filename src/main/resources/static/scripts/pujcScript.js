/*pokažkdý co je nová relace nebo tak něco, vyhodit modal, že je lepší se přihlásit a ktomu text proč.*/


const stDialog = document.getElementById('account-ad');
const ndDialog = document.getElementById('res-affirm');
const thDialog = document.getElementById('res-affirm-empt');
const rdDialog = document.getElementById('fin-res-affirm');
const RezBtn= document.getElementById('resv-fin');
const FinBtn= document.getElementById('btn-affirm');
const finTable = document.getElementById('tbl-affirm');//seznam ze kterého bude výpůjčka

 RezBtn.addEventListener('click', function(e) {
     FillFinTable();
     if (finTable.children.length > 0) {
         showDialog(ndDialog);
     } else {
         showDialog(thDialog);
     }
 })

FinBtn.addEventListener('click', function(e) {
    closeDialog(ndDialog);
    //TODO: zjistit jestli proběhla rezervace a uložila se do dbs-->ukázat rdDialog
    showDialog(rdDialog);
    //TODO: pokud se nepovedlo, ukázat dialog, kterej říká že se něco nepovedlo

})

function closeDialog(dialog) {
    dialog.close();
}
function showDialog(dialog) {
    if (dialog===ndDialog) {
        /*vypsání kontrolní tabulky*/
    }
    dialog.showModal();
}
function closeDAndRedirect(dialog, waystring) {
    dialog.close();
    location.href=waystring;
}

function startFunction(){
    /*todo:if, jestli je přihlášený-asi stejný jako u  toho singbuttonu*/
    showDialog(stDialog);
}
/*todo: vyplnění tabulky podle specifikací ve formuláři (VIEW) + po kliknutí na jméno redirect na stránku itemu(itemSite.html), která bude vykreslená podle informací itemu*/

function FillFinTable(){
    const rows = document.querySelectorAll('#res-select-table tr');

    while (finTable.firstChild) {
        finTable.removeChild(finTable.firstChild);
    }

    rows.forEach(row => {
        const checkbox = row.querySelector('td input[type="checkbox"]');
        if (checkbox && checkbox.checked) {
            const clone = document.createElement('tr');

            for (let i = 1; i < row.cells.length; i++) {
                const cell = row.cells[i];
                const newCell = document.createElement('td');
                newCell.innerHTML = cell.innerHTML;
                clone.appendChild(newCell);
            }

            finTable.appendChild(clone);
        }
    });
}