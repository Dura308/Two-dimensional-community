<template>
  <el-card shadow="hover">
    <div class="user-info">
      <el-popover placement="right-end"
                  :width="336"
                  :offset="10"
                  :popper-style="popperStyle"
                  @before-enter="emitter.emit('getCardInfo' + contentInfo.contentId + 'squareCard')"
                  :show-arrow="false"
                  trigger="hover">
        <template #reference>
          <el-link type="primary"
                   :underline="false">
            <el-avatar
              :src="contentUserAvatar"
              :size="35"/>
          </el-link>
        </template>

        <user-card :userId="loginInfo.userId"
                   :cardUserId="contentInfo.userId"
                   :uniqueId="contentInfo.contentId + 'squareCard'"/>
      </el-popover>
      <div class="nick-name">
        <p><el-link>{{ contentInfo.nickName }}</el-link></p>
      </div>
    </div>
    <div class="user-content">
      <span style="font-size: 14px;cursor: pointer">{{contentInfo.text}}</span>
      <div class="content">

        <div class="content-picture" v-if="contentInfo.contentType === 'picture'">
          <el-scrollbar max-height="468px">
            <template v-for="(item, index) in content">
              <el-image :src="item.url" ></el-image>
            </template>
          </el-scrollbar>
        </div>

        <div class="content-video" v-else-if="contentInfo.contentType === 'video'">
          <template v-for="(item, index) in content">
            <video :src="item.url"
                   :controls="videoControl"
                   @mouseover="videoControl === true"
                   @mouseout="videoControl === false"
                   style="width: 100%"/>
          </template>
        </div>
      </div>
    </div>

    <card-button-wrap :params="{isCollect, isLike, contentInfo}"/>
  </el-card>
</template>

<script lang = "ts" setup>
  import {
    computed,
    defineProps,
    onBeforeMount,
    PropType,
    ref,
  } from "vue";
  import {useStore} from "vuex";
  import emitter from '@/utils/bus';
  import UserCard from './UserCard'
  import CardButtonWrap from './CardButtonWrap'

  interface List{
    isLike?: boolean,
    isCollect?: boolean,
    contentInfo?: {},
    content?: {}
  }

  const videoControl = ref(true)
  const userCardVisible = ref(false)
  const popperStyle = {
    borderRadius: '8px',
    padding: '0'
  }
  const store = useStore()
  const loginInfo = computed(() => {
    return store.state.loginInfo
  })

  const props = defineProps({
    list: {
      type: Array as PropType<List>
    }
  })

  const contentUserAvatar = computed(() => {
    return 'http://47.109.51.114:8089/avatar/' + props.list.contentInfo.userId + '.png'
  })

  const isLike = ref()
  const isCollect = ref()
  const contentInfo = computed(() => {
    return props.list.contentInfo
  })
  const content = computed(() => {
    return props.list.content
  })

  onBeforeMount(() => {
    isLike.value = props.list.isLike
    isCollect.value = props.list.isCollect
  })

</script>

<style scoped>
  .el-card{
    border-radius: 8px;
    margin-bottom: 10px;
  }

  :deep(.el-card__body){
    --el-card-padding: 5px 10px 0 10px;
  }

  .user-info{
    display: flex;
    align-items: center;
  }

  .item-user-card{
    position: absolute;
  }

  .user-content{
    margin-bottom: 20px;
  }

  .content{
    margin-top: 5px;
    cursor: pointer;
  }

  .nick-name{
    margin-left: 10px;
  }

</style>
