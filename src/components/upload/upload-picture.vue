<template>
  <el-upload ref="uploadRef"
             :file-list="fileList"
             :limit="3"
             action="#"
             list-type="picture-card"
             :on-change="fileChange"
             :on-preview="handlePictureCardPreview"
             :on-remove="handleRemove"
             :on-exceed="handleExceed"
             :auto-upload="false">
    <el-icon><Plus /></el-icon>

  </el-upload>

  <el-dialog v-model="dialogVisible">
    <img w-full :src="dialogImageUrl" alt="Preview Image" />
  </el-dialog>
</template>
<script lang="ts" setup>
  import {computed, defineExpose, onMounted, ref} from 'vue'
  import { Delete, Download, Plus, ZoomIn } from '@element-plus/icons-vue'
  import type {UploadFile, UploadProps} from 'element-plus'
  import {useStore} from "vuex";
  import {useRoute} from "vue-router";
  import type { UploadInstance, UploadUserFile } from 'element-plus'
  import {ElMessage} from "element-plus";

  const store = useStore()
  const route = useRoute()

  const headers = computed(() => {
    return {
      'token': route.query.token
    }
  })

  const dialogImageUrl = ref('')
  const dialogVisible = ref(false)
  const disabled = ref(false)
  const uploadRef = ref<UploadInstance>()
  const fileList = ref<UploadUserFile[]>([])

  const handleRemove = (uploadFile, uploadFiles) => {
    fileList.value = uploadFiles
  }

  const handleExceed = () => {
    ElMessage.warning(
      `一次最多上传 3 张图片哦 ~ ~`
    )
  }

  const handlePictureCardPreview = (file: UploadFile) => {
    dialogImageUrl.value = file.url!
    dialogVisible.value = true
  }

  const fileChange = (uploadFile, uploadFiles) => {
    fileList.value = uploadFiles
  }

  defineExpose({
    fileList
  })

</script>
<style scoped>

  .w-100\%,
  .w-full,
  [w-full=""] {
    width: 100%;
  }

  .el-upload-list--picture-card .el-upload-list__item-thumbnail {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
</style>
