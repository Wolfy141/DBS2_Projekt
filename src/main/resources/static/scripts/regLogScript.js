/*vykreslení buď toho nebo onoho*/

/*po registraci je nutný se přihlásit, po přihlášení redirect na profil*/


const login = document.getElementById("login");
const registration = document.getElementById("registration");

function setVisible(element) {
    element.style.display = "flex";
}

function setInvisible(element) {
    element.style.display = "none";
}

const regSwitch = document.getElementById("regSwitch");
regSwitch.addEventListener("click", function() {
    setInvisible(login);
    setVisible(registration);
});

const logSwitch = document.getElementById("logSwitch");
logSwitch.addEventListener("click", function() {
    setInvisible(registration);
    setVisible(login);
});
//todo: po přihlášení otevřít
//      -worker profil
//      -customer profil - s personalised data

//todo: nastavit aby profil odkazoval na specializovanej profil podle role