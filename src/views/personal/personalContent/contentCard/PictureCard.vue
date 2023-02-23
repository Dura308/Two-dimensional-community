<template>
    <div class="personal-content-picture" style="padding: 0 45px">
      <el-row>
        <el-col>
          <div class="content-main" id="content-main">
            <div class="content-header">
              <div class="header-avatar">
                <el-avatar :size="50" :src="contentUserAvatar"/>
              </div>
              <div class="header-info">
                <div class="info-nickName">
                  <span>{{ contentInfo.nickName }}</span>
                </div>
                <div class="info-created-time">
                  <span>{{ contentInfo.createdTime }}</span>
                </div>
              </div>
            </div>
            <div class="content-text">
              <span>{{ contentInfo.text }}</span>
            </div>
            <div class="content-picture">
              <item-image :imageList="imageList"/>
            </div>
            <card-button-wrap :params="{isCollect, isLike, contentInfo}"/>
          </div>
        </el-col>
      </el-row>
    </div>
</template>

<script lang = "ts" setup>

  import {computed, defineProps, onBeforeMount, PropType, ref} from "vue";
  import CardButtonWrap from '@/views/home/main/CardButtonWrap'
  import ItemImage from './item-image'

  interface List{
    isLike?: boolean,
    isCollect?: boolean,
    contentInfo?: {},
    content?: []
  }

  const props = defineProps({
    list: {
      type: Array as PropType<List>
    }
  })
  const isLike = ref()
  const isCollect = ref()
  const contentInfo = computed(() => {
    return props.list.contentInfo
  })
  const content = computed(() => {
    return props.list.content
  })
  const contentUserAvatar = computed(() => {
    return 'http://47.109.51.114:8089/avatar/' + props.list.contentInfo.userId + '.png'
  })
  const imageList = ref([])
  const getImageList = () => {
    return new Promise(resolve => {
      for (let i = 0; i < content.value.length; i++) {
        imageList.value.push(content.value[i].url)
      }
      resolve('getImageList执行')
    })
  }

  onBeforeMount(() => {
    isLike.value = props.list.isLike
    isCollect.value = props.list.isCollect
    getImageList().then(response => {
      console.log(response)
    })
  })

</script>

<style scoped>

  .content-main{
    display: flex;
    flex-direction: column;
    max-width: 500px
  }

  .content-header{
    display: flex;
    align-items: center;
  }

  .content-header,
  .content-text,
  .content-picture{
    padding-bottom: 5px
  }

  .content-picture > ul {
    margin-top: 0;
    padding: 0
  }

  .header-avatar{
    display: flex;
    align-items: center
  }

  .header-info{
    padding-left: 10px;
    display: flex;
    flex-direction: column
  }

  .info-nickName > span{
    font-size: 16px
  }

  .info-created-time > span{
    font-size: 12px;
    color: #9499A0
  }


  .demo-image__error .image-slot {
    font-size: 30px;
  }
  .demo-image__error .image-slot .el-icon {
    font-size: 30px;
  }
  .demo-image__error .el-image {
    width: 100%;
    height: 200px;
  }

  ul{
    list-style-type: none;
  }

</style>
