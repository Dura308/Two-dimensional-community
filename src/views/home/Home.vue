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

  // window.setInterval(() => {
  //   setTimeout(() => {
  //     console.log('轮询中.....')
  //     newGetRequest('home/getCommentMsg', {
  //       userId: store.state.loginInfo.userId
  //     }).then(response => {
  //       if(response.code === 200){
  //         console.log(response.data)
  //         // ElNotification({
  //         //   title: '新的通知',
  //         //   message: ',' + store.state.loginInfo.nickName,
  //         //   duration: 3000,
  //         // })
  //       }
  //     })
  //   }, 0)
  // }, 1000)

  const commentForward = () => {
    return new Promise(resolve => {

      newGetRequest('/home/getCommentMsg', {
        userId: store.state.loginInfo.userId
      }).then(response => {
        if(response.code === 200){
          console.log(response.data)
          resolve(response.data)
        }
      })
    })
  }

  function start () {
    setTimeout(() => {
      console.log('轮询中.....')
      commentForward().then(response => {
        console.log(response)
        ElNotification({
          title: '新的通知',
          message: response.userId + '用户给你的' + response.contentId + '评论了',
          duration: 3000,
        })
      })
      start()
    }, 3000)
  }
  // const start =


  onMounted(() => {
    emitter.on('updateAvatar', (avatar) => {
      store.commit('updateAvatar', avatar)
    })
    start()
  })

  onUnmounted(() => {
    emitter.off('updateAvatar')
  })
</script>

<style scoped>

</style>
