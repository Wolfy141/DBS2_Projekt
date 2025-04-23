
const ResToCheck=document.getElementById("ResToCheck-dial");
const RentsToCheck=document.getElementById("RentsToCheck-dial");
const NewRents=document.getElementById("NewRents-dial");

const NewRentsDiv= document.getElementById("NewRents");
const RentsToCheckDiv= document.getElementById("RentsToCheck");
const ResToCheckDív= document.getElementById("ResToCheck");

NewRentsDiv.addEventListener("click", ()=>{NewRents.showModal()});
RentsToCheckDiv.addEventListener("click", ()=>{RentsToCheck.showModal()});
ResToCheckDív.addEventListener("click", ()=>{ResToCheck.showModal()});
