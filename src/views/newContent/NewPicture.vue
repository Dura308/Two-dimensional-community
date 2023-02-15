<template>
  <div class="item-newPicture"
       v-loading="loading"
       element-loading-text="正在上传...">
    <el-card class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>发布图片</span>
        </div>
      </template>
      <div class="card-body">
        <el-form>
          <el-form-item>
            <upload-picture ref="upload"/>
          </el-form-item>
        </el-form>
        <div>
          <el-input
            v-model="text"
            :autosize="{ minRows: 4, maxRows: 6 }"
            type="textarea"
            placeholder="在这里可以输入你想说的话哦..."
          />
        </div>
      </div>
    </el-card>
    <el-button class="btn-submit" type="primary" @click="onSubmit">提交发布</el-button>
  </div>
</template>

<script lang="ts" setup>
  import {ref} from 'vue'
  import uploadPicture from '@/components/upload/upload-picture'
  import {useStore} from "vuex";
  import axios from "axios";
  import url from "@/index";
  import {useRoute} from "vue-router";
  import {compressionFile} from "@/utils/compressImage";
  import {ElMessage} from "element-plus";
  import {newPostRequest} from "@/utils/api";

  const store = useStore()
  const route = useRoute()
  // do not use same name with ref

  const text = ref('')
  const upload = ref<any>()

  const loading = ref(false)

  const onSubmit = async () => {
    loading.value = true
    let formData = new FormData()
    for (const file of upload.value.fileList) {
      //图片压缩
      formData.append('pictureFile',await compressionFile(file.raw, 'image/jpeg', 0.5))
    }
    formData.append('userId', String(route.query.userId))
    formData.append('nickName', String(route.query.nickName))
    formData.append('contentType', 'picture')
    formData.append('text', text.value)

    newPostRequest('/content/newPicture', formData, {
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
</script>

<style scoped>
  .item-newPicture{
    display: flex;
    flex-direction: column;
    width: 50%;
    margin: 3% auto auto;
  }

  .box-card{
    border-radius: 15px;
  }

  .card-body{
   padding: 0 15% 20px 15%;
  }

  .btn-submit{
    margin: 3% auto auto;
  }

</style>
