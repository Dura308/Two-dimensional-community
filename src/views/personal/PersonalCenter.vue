<template>
  <div class = "personal-center">
    <el-card class = "box-card" shadow = "hover">
      <template #header>
        <div class = "card-header">
          <span>个人中心</span>
        </div>
      </template>
      <div class = "card-body" style = "height: 800px">
        <el-tabs v-model="activeName"
                 class="demo-tabs"
                 @tab-click="handleClick"
                 tab-position="left">

          <el-tab-pane label="我的信息" name="0">
            <personal-info />
          </el-tab-pane>

          <el-tab-pane label="我的头像" name="1">
            <personal-avatar />
          </el-tab-pane>

          <el-tab-pane label="我的发布" name="2">
            <personal-content />
          </el-tab-pane>

          <el-tab-pane label="我的消息" name="3">
            我的消息
          </el-tab-pane>

          <el-tab-pane label="我的积分" name="4">
            我的积分
          </el-tab-pane>

          <el-tab-pane label="我的订单" name="5">
            我的订单
          </el-tab-pane>

          <el-tab-pane label="我的关注" name="6">
            我的关注
          </el-tab-pane>

          <el-tab-pane label="我的粉丝" name="7">
            我的粉丝
          </el-tab-pane>

        </el-tabs>

      </div>
    </el-card>
  </div>
</template>

<script lang = "ts" setup>

  import {onBeforeMount, onBeforeUpdate, onMounted, ref} from "vue";
  import {useStore} from "vuex";
  import {useRoute} from "vue-router";
  import type { TabsPaneContext } from 'element-plus'
  import PersonalInfo from './personalInfo/PersonalInfo'
  import PersonalAvatar from './personalAvatar/PersonAvatar'
  import PersonalContent from './personalContent/PersonalContent'
  import {newGetRequest} from "@/utils/api";
  import {ElMessage} from "element-plus";

  const store = useStore()
  const route = useRoute()

  onBeforeMount(() => {
    console.log(1)
    try {
      let token = localStorage.getItem('token')
      store.commit('parseToken', token)
    } catch (e) {
      ElMessage.error(e)
    }

    console.log(2)
  })

  const activeName = ref('0')

  const handleClick = (tab: TabsPaneContext, event: Event) => {
    console.log(tab, event)
  }

</script>

<style scoped>

  .personal-center {
    display: flex;
    flex-direction: column;
    width: 50%;
    min-width: 850px;
    margin: 3% auto auto;
  }

  .box-card {
    border-radius: 15px;
  }

  :deep(.el-card__body) {
    --el-card-padding: 20px 20px 20px 0;
  }

  .el-tabs--left .el-tabs__item.is-left {
    text-align: left;
  }

  :deep(.el-tabs__item){
    height: 55px;
  }
</style>
