<template>
  <a href="#" class="like-btn" ref="likeButton" @click="dianzan">
    <!-- like button icon svg -->
    <svg class="like-icon" ref="likeIcon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24">
      <path d="M12 4.419c-2.826-5.695-11.999-4.064-11.999 3.27 0 7.27 9.903 10.938 11.999
               15.311 2.096-4.373 12-8.041 12-15.311 0-7.327-9.17-8.972-12-3.27z" />
    </svg>
  </a>
</template>

<script lang="ts" setup>

  import {getCurrentInstance, inject, onMounted} from "vue"
  import {ElNotification} from "element-plus";
  import {useStore} from "vuex";
  const store = useStore()
  const instance = getCurrentInstance()
  let likeButton = instance?.refs.likeButton
  let likeIcon = instance?.refs.likeIcon

  const dianzan = () => {
    if (store.state.token === null) {
      return
    }
    likeButton.classList.toggle("active");
    if (likeButton.classList.contains("active")) {
      createClones(likeButton);
    }
  }

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
      let clone = likeIcon.cloneNode(true)
      let size = randomNum(10, 20);
      button.appendChild(clone);
      clone.setAttribute("width", String(size));
      clone.setAttribute("height", String(size));
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

</script>

<style scoped>

  .like-btn {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .like-btn:active {
    transform: scale(0.9, 0.9);
  }

  .like-btn.active {
    animation: pop 0.6s 1;
  }

  .like-btn path {
    fill: #767676;
  }

  .like-btn.active path {
    fill: #ec3e3e;
  }

  @keyframes pop {
    0% {
      transform: scale(1, 1);
    }
    25% {
      transform: scale(1.3, 0.7);
    }
    40% {
      transform: scale(0.8, 1.3);
    }
    55% {
      transform: scale(0.9, 1.1);
    }
    75% {
      transform: scale(1.1, 0.7);
    }
    100% {
      transform: scale(1, 1);
    }
  }

  .clone {
    position: absolute;
    animation: moveOutwards 0.9s 1;
    opacity: 0;
  }

  @keyframes moveOutwards {
    from {
      opacity: 1;
      transform: translate(0, 0);
    }
  }
</style>
