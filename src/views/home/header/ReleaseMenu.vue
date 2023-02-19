<template>
  <el-dropdown placement="bottom-end">
    <el-button class="btn-release" type="primary" color="#FC84A6" size="large" :icon = "Edit" round>发布</el-button>
    <template #dropdown>
      <el-dropdown-menu class="release-menu">
        <el-dropdown-item>
          <el-link class="publish-item" :underline = "false" @click="newPicture">
            <el-image :src = Picture></el-image>
            <div class="text">
              <p class="type">图片</p>
              <p class="short">绘画、COS、手办、汉服、表情包…</p>
            </div>
          </el-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link class="publish-item" :underline = "false" @click="newText">
            <el-image :src = Word></el-image>
            <div class="text">
              <p class="type">文字</p>
              <p class="short">小说、漫评、段子、文章、碎碎念…</p>
            </div>
          </el-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link class="publish-item" :underline = "false" @click="newQuestion">
            <el-image :src = Question></el-image>
            <div class="text">
              <p class="type">提问</p>
              <p class="short">如何评价？如何反驳？为什么讨厌？</p>
            </div>
          </el-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <el-link class="publish-item" :underline = "false" @click="newVideo">
            <el-image :src = Video></el-image>
            <div class="text">
              <p class="type">视频</p>
              <p class="short">舞蹈、剪辑、游戏、MAD、生活...</p>
            </div>
          </el-link>
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script lang = "ts" setup>

  import {useStore} from "vuex"
  import {useRoute, useRouter} from "vue-router"
  import {Edit} from "@element-plus/icons-vue"
  import Picture from '@/assets/img-picture.png'
  import Question from '@/assets/img-question.png'
  import Word from '@/assets/img-word.png'
  import Video from '@/assets/img-video.png'
  import {reactive} from "vue";


  const store = useStore()
  const router = useRouter()
  const routeQuery = reactive({
    'token': store.state.token,
    'userId': store.state.loginInfo.userId,
    'nickName': store.state.loginInfo.nickName
  })

  /** 发布图片 */
  const newPicture = () => {
    const newPage = router.resolve({
      path: '/newPicture',
      query: routeQuery
    })
    window.open(newPage.href,'_blank')
  }

  /** 发布视频 */
  const newVideo = () => {
    const newPage = router.resolve({
      path: '/newVideo',
      query: routeQuery
    })
    window.open(newPage.href,'_blank')
  }

  /** 发布文字 */
  const newText = () => {
    const newPage = router.resolve({
      path: '/newText',
    })
    window.open(newPage.href,'_blank')
  }

  /** 发布提问 */
  const newQuestion = () => {
    const newPage = router.resolve({
      path: '/newQuestion',
    })
    window.open(newPage.href,'_blank')
  }

</script>

<style scoped>

  .btn-release{
    font-weight: lighter;
    font-size: 16px;
    color: white;
  }

  .btn-release:hover{
    color: white;
  }

  .publish-item{
    width: 280px;
    height: 66px;
    padding: 5px 15px 5px 5px;
    margin-bottom: 5px;
    border-radius: 6px;
  }

  .text{
    margin-left: 10px;
  }

  .text .type{
    font-size: 16px;
    line-height: 1;
    color: #252526;
  }

  .text .short{
    font-size: 12px;
    line-height: 1;
    color: #a1a1a6;
    margin-top: 6px;
  }

</style>
