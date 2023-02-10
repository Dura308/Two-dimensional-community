<template>
  <div class="item-newPicture">
    <el-card class="box-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>发布文字</span>
        </div>
      </template>
      <div class="card-body">
        <el-form :model="form">
          <el-form-item>

          </el-form-item>
        </el-form>
        <div>
          <el-input

            :autosize="{ minRows: 4, maxRows: 6 }"
            type="textarea"
            placeholder="在这里可以输入你想说的话哦..."
          />
        </div>
      </div>
    </el-card>
    <el-button class="btn-submit" type="primary">提交发布</el-button>
  </div>
</template>

<script lang="ts" setup>
  import { reactive } from 'vue'
  import uploadPicture from '@/components/upload/upload-picture'

  // do not use same name with ref
  const form = reactive({

  })

  const onSubmit = () => {
    console.log('submit!')
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
    padding: 0 120px 20px 120px;
  }

  .btn-submit{
    margin: 3% auto auto;
  }

</style>
