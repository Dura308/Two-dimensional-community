<template>
  <div class="item-newVideo"
       v-loading="loading"
       element-loading-text="正在上传...">
    <el-card class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>发布视频</span>
        </div>
      </template>
      <div class="card-body">
        <div>
          <el-upload
            class="upload-demo"
            :auto-upload="false"
            ref="uploadRef"
            :http-request="httpRequest"
            drag
            action=""
            multiple>
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              <em>点击上传视频</em><br>
              <em>或拖拽上传视频</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
<!--                jpg/png files with a size less than 500kb-->
              </div>
            </template>
          </el-upload>
        </div>
        <div>
          <el-input
            v-model="text"
            :autosize="{ minRows: 4, maxRows: 6 }"
            type="textarea"
            placeholder="在这里可以输入你想说的话哦..."/>
        </div>
      </div>
    </el-card>
    <el-button class="btn-submit" type="primary" @click="submitUpload">提交发布</el-button>
  </div>
</template>

<script lang="ts" setup>
  import {UploadFilled} from '@element-plus/icons-vue'
  import { ref } from 'vue'
  import type { UploadInstance } from 'element-plus'
  import {useRoute} from "vue-router";
  import {newPostRequest} from "@/utils/api";
  import {ElMessage} from "element-plus";

  const uploadRef = ref<UploadInstance>()
  const loading = ref(false)
  const route = useRoute()
  const text = ref('')

  const httpRequest = (param) => {
    console.log(param.file)
    loading.value = true
    let formData = new FormData()
    formData.append('videoFile', param.file)
    formData.append('userId', String(route.query.userId))
    formData.append('nickName', String(route.query.nickName))
    formData.append('contentType', 'video')
    formData.append('text', text.value)

    newPostRequest('/content/newVideo', formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(response => {
      loading.value = false
      if (response.code === 200){
        ElMessage({
          message: '发布成功，即将关闭当前页面',
          type: 'success'
        })
        setTimeout(() => {
          window.close()
        }, 2000)
      }else {
        ElMessage.error(response.data)
      }
    })

  }
  const submitUpload = () => {
    uploadRef.value!.submit()
  }


</script>

<style scoped>
  .item-newVideo{
    display: flex;
    flex-direction: column;
    width: 50%;
    min-width: 850px;
    margin: 3% auto auto;
  }

  .box-card{
    border-radius: 15px;
  }

  .card-body{
    padding: 0 120px 20px 120px;
  }

  .btn-submit{
    margin: 3% auto auto;
  }

</style>
