<template>
  <el-tabs v-model="activeName"
           class="demo-tabs"
           tab-position="right"
           @tab-click="handleClick">
    <el-tab-pane label="图片" name="first">
      <picture-content />
    </el-tab-pane>
    <el-tab-pane label="视频" name="second">Config</el-tab-pane>
    <el-tab-pane label="文字" name="third">Role</el-tab-pane>
    <el-tab-pane label="提问" name="fourth">Task</el-tab-pane>
  </el-tabs>
</template>
<script lang="ts" setup>
  import { ref } from 'vue'
  import type { TabsPaneContext } from 'element-plus'
  import PictureContent from './PictureContent'

  const activeName = ref('first')

  const handleClick = (tab: TabsPaneContext, event: Event) => {
    console.log(tab, event)
  }
</script>
<style scoped>
  .demo-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
  }
</style>
