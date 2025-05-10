const loggedIN=false;//todo:zjišťovat jestli je přihlášen a podle toho vykreslovat

const logBtn=document.getElementById("bSingd");
const logIco=document.getElementById("profile-link");

function finishHeader() {
    if (loggedIN) {
        logBtn.style.display = "none";
        logIco.style.display = "flex";
    } else {
        logIco.style.display = "none";
        logBtn.style.display = "block";
    }
}