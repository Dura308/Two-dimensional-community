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
  import {onMounted, onUnmounted} from "vue";
  import emitter from "@/utils/bus"
  import {useStore} from "vuex";
  import {ElNotification} from "element-plus";
  import {newGetRequest} from "@/utils/api";
  import url from "@/index";

  //解决vuex数据在页面刷新被重置的问题
  const store = useStore()

  //在页面加载时读取localStorage里的状态信息
  if (localStorage.getItem("token")) {
    store.commit("parseToken", localStorage.getItem("token"))
  }

  //在页面刷新时将vuex里的信息保存到localStorage里
  window.addEventListener("beforeunload", () => {
    localStorage.setItem("store", JSON.stringify(store.state))
  })


  if(typeof(EventSource) !== "undefined") {   //判断是否支持EventSource
    let source = new EventSource(url + '/sse/sub/' + store.state.loginInfo.userId); //为http://localhost:8080/sse/push
    source.addEventListener('open', () => {
      console.log('建立连接.....')
    }, false)
    source.addEventListener('message', (e) => {
      console.log('收到消息>>>' + e.data)
    })
  }else {
    console.log('你的浏览器不支持SSE')
  }

  onMounted(() => {
    emitter.on('updateAvatar', (avatar) => {
      store.commit('updateAvatar', avatar)
    })
  })

  onUnmounted(() => {
    emitter.off('updateAvatar')
  })
</script>

<style scoped>

</style>
