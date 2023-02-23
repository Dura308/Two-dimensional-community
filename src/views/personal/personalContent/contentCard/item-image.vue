<template>
  <ul style="display: flex;justify-content: space-evenly;">
    <li v-for = "(item, index) in props.imageList">
      <el-image
        :class="imageClass"
        :src = "item"
        :zoom-rate = "1.2"
        :preview-src-list = "props.imageList"
        :initial-index = "index"
        fit = "cover"
      />
    </li>
  </ul>

</template>

<script lang = "ts" setup>

  import {defineProps, onBeforeMount, ref} from "vue";

  const props = defineProps({
    imageList: [],
    imageStyle: {}
  })

  const imageClass = ref()

  const getImageStyle = () => {
    return new Promise(resolve => {
      let length = props.imageList.length
      if(length === 1){
        resolve('image-style1')
      }
      if(length === 2){
        resolve('image-style2')
      }
      if(length === 3){
        resolve('image-style3')
      }
    })
  }

  onBeforeMount(() => {
    console.log('imageList' + props.imageList)
    getImageStyle().then(response => {
      imageClass.value = response
      console.log(imageClass.value)
    })

  })

</script>

<style scoped>

  .image-style1{
    width: 500px;
    max-height: 500px;
  }

  .image-style2{
    width: 246px;
    height: 246px;
  }

  .image-style3{
    width: 161px;
    height: 161px;
  }
</style>
