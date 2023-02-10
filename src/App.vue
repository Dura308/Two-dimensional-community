<template>
  <router-view/>
</template>

<script lang="ts" setup>
  import router from "@/router";

</script>

<style>
  html,
  body,
  #app{
    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
    background-color: #F5F5FA;
    /*background: linear-gradient(90deg, rgba(93, 51, 247, 0.1), rgba(85, 180, 243, 0.1) 15%, rgba(239, 78, 123, .1) 30%, rgba(171, 139, 102, 0.1) 44%, rgba(80, 115, 184, .1) 58%, rgba(16, 152, 173, .1) 72%, rgba(7, 125, 179, 0.1) 86%, rgba(109, 186, 129, 0.164));*/
  }
</style>
