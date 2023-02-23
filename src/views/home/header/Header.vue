<template>
  <el-affix :offset="0" >
    <el-header class="tofu-header">
      <el-row :gutter="20">
        <el-col :span="3">
          <el-link class = "header-nav-bar" href = "#" :underline = "false">
            <el-image :src = HomeLogo></el-image>
          </el-link>
        </el-col>
        <el-col :span="6">
          <ul class="left-entry">
            <li><el-link class = "header-nav-bar" href = "#">推荐</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">关注</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">榜单</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">问答</el-link></li>
            <li><el-link class = "header-nav-bar" href = "#">cos</el-link></li>
          </ul>
        </el-col>
        <el-col :span="6">
          <div class="center-search">
            <el-form id="searchForm">
              <input
                v-model="input1"
                class="input-search"
                placeholder="Please Input"/>
              <el-button class="btn_search" :icon="Search" circle text/>
            </el-form>
          </div>
        </el-col>
        <el-col :span="9">
          <ul class="right-entry">
            <li>
              <user-avatar />
            </li>
            <li><el-link class = "header-nav-bar" href = "#" :icon = House>首页</el-link></li>
            <li>
              <el-badge :value="12" class="item">
                <el-link class = "header-nav-bar" href = "#" :icon = Bell>消息</el-link>
              </el-badge>
            </li>
            <li><el-link class = "header-nav-bar" href = "#" :icon = Coin>积分商城</el-link></li>
            <li>
              <release-menu />
            </li>
          </ul>
        </el-col>
      </el-row>
    </el-header>
  </el-affix>

</template>

<script lang = "ts" setup>
  import {ref} from 'vue'
  import {
    Search,
    House,
    Bell,
    Coin,
    UserFilled,
  } from '@element-plus/icons-vue'
  import HomeLogo from '@/assets/tofu社区.png'
  import {useStore} from "vuex";
  import {useRouter} from "vue-router";
  import ReleaseMenu from './ReleaseMenu'
  import UserAvatar from './UserAvatar'

  const store = useStore()
  const router = useRouter()
  const input1 = ref('')

</script>

<style scoped>
  .tofu-header{
    min-width: 1200px;
    height: 64px;
    padding-top: 1px;
    background-color: white;
    display: flex;
    justify-content: center;
    align-items: center;
    border-bottom: solid 1px var(--el-border-color);
    box-shadow: var(--el-box-shadow-light);
  }

  ul{
    list-style-type: none;
    padding-left: 0;
  }

  ul li{
    padding: 10px;
  }

  .el-link{
    font-size: 16px;
  }

  .tofu-header .left-entry,
  .tofu-header .right-entry{
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    flex-shrink: 0;
  }

  .left-entry li, .right-entry li{
    text-align: center;
  }

  .el-row{
    align-items: center;
    display: flex;
  }

  #searchForm{
    display: flex;
    align-items: center;
    background-color: white;
    border: solid 1px var(--el-border-color);
    border-radius: 50px 50px 50px 50px;
  }

  .input-search{
    height: 25px;
    width: 80%;
    margin-left: 10px;
    border-radius: 8px;
    border: none;
    outline: none;
  }

  .btn_search{
    margin-left: 10px;
    margin-right: 10px;
  }

</style>
