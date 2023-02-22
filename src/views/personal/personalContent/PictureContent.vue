<template>

    <el-row>
      <el-col>
        <div>
          <el-scrollbar height="750px">
            <template v-for="item in pictureContent">
              <picture-card :list="item"/>
              <el-divider />
            </template>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>

</template>

<script lang = "ts" setup>

  import {useRoute} from "vue-router";
  import {onMounted, ref} from "vue";
  import {newGetRequest} from "@/utils/api";
  import PictureCard from './contentCard/PictureCard'

  const route = useRoute()
  const pictureContent = ref([])
  const getPictureContent = () => {
    newGetRequest('/user/getContent', {
      userId: route.query.userId,
      type: 'picture'
    }).then(response => {
      console.log(response.data)
      pictureContent.value = response.data
    })
  }
  onMounted(() => {
    getPictureContent()
  })
</script>

<style scoped>

  :deep(.el-divider--horizontal){
    margin: 18px auto;
  }
</style>
