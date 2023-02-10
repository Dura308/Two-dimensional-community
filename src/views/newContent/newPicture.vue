<template>
  <div class="item-newPicture">
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

  const store = useStore()
  const route = useRoute()
  // do not use same name with ref

  const text = ref()
  const upload = ref<any>()

  const onSubmit = () => {

    let formData = new FormData()
    upload.value.fileList.forEach(file => {
      formData.append('pictureFile',file.raw)
    })
    formData.append('userId', String(route.query.userId))
    formData.append('nickName', String(route.query.nickName))
    formData.append('contentType', 'picture')
    formData.append('text', text.value)
    axios.post(url + "/content/newPicture", formData,{
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then(function (response) {
      console.log(response)
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
