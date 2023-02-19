<template>
  <div style="display: flex;justify-content: center">
    <div :style="previewStyle1">
      <div :style="previews.div">
        <img class="realTime-image" :src="previews.url" :style="previews.img">
      </div>
    </div>
  </div>
  <div class="upload-img">
    <el-upload
      class="upload-demo"
      action="#"
      :show-file-list="false"
      :on-change="handleChange"
      :auto-upload="false">
      <el-button type="primary">更换头像</el-button>
    </el-upload>
  </div>
  <div>
    <avatar-cropper />
  </div>
</template>

<script lang = "ts" setup>
  import {onMounted, onUnmounted, ref} from "vue";
  import {useStore} from "vuex";
  import { UploadFilled } from '@element-plus/icons-vue'
  import AvatarCropper from './avatarCropper'
  import emitter from '@/utils/bus'


  const store = useStore()
  const userInfo = ref(store.state.loginInfo)

  const handleChange = (file) => {
    emitter.emit('cropper', file.raw)
  }

  const previews = ref({
    url: userInfo.value.avatar + '?v=' + Math.random(),
    img: {
      width: '200px',
      height: '200px',
      borderRadius: '50%'
    }
  })

  const previewStyle1 = ref()

  onMounted(() => {
    emitter.on('realTime', (data) => {
      previews.value = data
      previewStyle1.value = {
        width: data.w + "px",
        height: data.h + "px",
        overflow: "hidden",
        borderRadius: "50%",
        margin: "0",
        zoom: 0.5
      }
    })
  })

  onUnmounted(() => {
    emitter.off('realTime')
  })

</script>

<style scoped>

  .upload-img{
    display: flex;
    justify-content: center;
    padding: 20px;
  }
</style>
