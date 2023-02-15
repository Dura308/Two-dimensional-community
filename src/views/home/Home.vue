<template>
  <div class="common-layout">
    <el-container>
      <div style="width: 100%;">
        <el-header/>
        <el-main/>
        <el-footer/>
      </div>
    </el-container>
  </div>
</template>

<script lang = "ts" setup>
  import ElHeader from './header/Header'
  import ElMain from './main/Main'
  import ElFooter from './footer/Footer'
  import {computed, onBeforeMount, onMounted} from "vue";
  import {useStore} from "vuex";
  import {ElNotification} from "element-plus";

  //解决vuex数据在页面刷新被重置的问题
  const store = useStore()

  //在页面加载时读取localStorage里的状态信息
  if (localStorage.getItem("token")) {
    store.commit("setToken", localStorage.getItem("token"))
  }

  //在页面刷新时将vuex里的信息保存到localStorage里
  window.addEventListener("beforeunload", () => {
    localStorage.setItem("store", JSON.stringify(store.state))
  })

</script>

<style scoped>

</style>
