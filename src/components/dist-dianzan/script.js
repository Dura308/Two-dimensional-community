let button = document.querySelector(".like-btn");
let icon = document.querySelector(".like-icon");

button.addEventListener("click", () => {
  button.classList.toggle("active");
  if (button.classList.contains("active")) {
    createClones(button);
  }
});


// generating random number between given two numbers
function randomNum(min, max) {
  return Math.floor(Math.random() * (max - min + 1) + min);
}

// getting either positive or negative value
function negativePositive() {
  return Math.random() < 0.5 ? -1 : 1;
}

function createClones(button) {
  let numberOfClones = randomNum(2, 4);

  for (let i = 1; i <= numberOfClones; i++) {
    let clone = icon.cloneNode(true);
    let size = randomNum(20, 40);
    button.appendChild(clone);
    clone.setAttribute("width", size);
    clone.setAttribute("height", size);
    clone.classList.add("clone");
    clone.style.transform =
      "translate(" +
      negativePositive() * randomNum(30, 60) +
      "px," +
      negativePositive() * randomNum(30, 60) +
      "px)";

    let removeNode = setTimeout(() => {
      button.removeChild(clone);
      clearTimeout(removeNode);
    }, 800);
  }
}
