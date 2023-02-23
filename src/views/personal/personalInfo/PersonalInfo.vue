<template>
  <el-form class="user-info-form"
           v-loading="loading"
           element-loading-text="提交成功，正在拼命修改..."
           label-width="100px"
           label-position="right">

    <el-form-item label="昵称:">
      <el-input style="width: 250px" v-model="userInfo.nickName"/>
    </el-form-item>

    <el-form-item label="用户名:">
      <span style="color: #9499A0">{{ 'tofu_' + userInfo.userId }}</span>
    </el-form-item>

    <el-form-item label="个性签名:">
      <el-input v-model="userInfo.userIntro"
                type="textarea"
                placeholder="设置您的个性签名ヾ(≧▽≦*)o"
                resize="none"
                rows="4"
                maxlength="100"/>
    </el-form-item>

    <el-form-item label = "地区:">
      <el-cascader
        style="width: 250px"
        :options="provinceAndCityData"
        v-model="userInfo.userArea">
      </el-cascader>
    </el-form-item>

    <el-form-item label="性别:">
      <el-radio-group v-model="userInfo.userGender" >
        <el-radio-button label="男" />
        <el-radio-button label="女" />
        <el-radio-button label="保密" />
      </el-radio-group>
    </el-form-item>

    <el-form-item class="item-bind-phone" label="绑定手机:" style="width: 350px">
      <span style="color: #9499A0">{{ phoneHidden(userInfo.phone) }}</span>
      <el-link v-if="userInfo.phone === null" style="float: right" :underline="false">绑定手机</el-link>
      <el-link v-else style="float: right" :underline="false">更改</el-link>
    </el-form-item>

    <el-form-item class="item-bind-email"  label="绑定邮箱:" style="width: 350px">
      <span style="color: #9499A0">{{ emailHidden(userInfo.email )}}</span>
      <el-link v-if="userInfo.email === null"
               style="float: right"
               @click="setDialog('bindEmail')"
               :underline="false">绑定邮箱</el-link>
      <el-link v-else
               style="float: right"
               @click="setDialog('updateEmail')"
               :underline="false">更改</el-link>
    </el-form-item>

  </el-form>

  <el-divider />

  <div style="display: flex;justify-content: center">
    <el-button style="width: 100px" type="primary" size="large" @click="saveInfo">保存</el-button>
  </div>

  <bind-email />

</template>

<script lang = "ts" setup>

  import {onMounted, ref} from "vue";
  import { provinceAndCityData, CodeToText, TextToCode } from 'element-china-area-data'
  import {useStore} from "vuex";
  import {newGetRequest, newPutRequest} from "@/utils/api";
  import {useRoute} from "vue-router";
  import {ElMessage} from "element-plus";
  import BindEmail from './BindEmail'
  import emitter from "@/utils/bus";

  const store = useStore()
  const route = useRoute()
  const userInfo = ref(store.state.userInfo)
  const loading = ref(false)

  const getUserInfo = () => {
    newGetRequest('/user/getUserInfo', {
      token: localStorage.getItem('token')
    }).then(response => {
      console.log(response)
      if(response.code === 200){
        userInfo.value = response.data
        userInfo.value.userArea = areaTextToCode(userInfo.value.userArea)
        store.commit('setUserInfo', response.data)
      }
    })
  }

  //隐藏电话号码
  const phoneHidden = (phone) => {
    if(phone === null){
      return '无'
    }
    phone = String(phone)
    return phone.substring(0, 3) + '*****' + phone.substring(8, phone.length)
  }

  //隐藏邮箱地址
  const emailHidden = (email) => {
    if(email === null){
      return '无'
    }
    email = String(email);
    let atIndex = email.indexOf('@');
    return email.substring(0, 3) + '*****' + email.substring(atIndex, email.length)
  }

  //判断地区是否为空
  const areaCodeToText = (data) => {
    if(data === ''){
      return ''
    }else {
      return CodeToText[data[0]] + '/' + CodeToText[data[1]]
    }
  }

  //地区文字转编码
  const areaTextToCode = (data) => {
    if(data === undefined || data === null){
      return
    }
    const area = String(data).split('/')
    const province = TextToCode[area[0]]
    const city = province[area[1]]
    return [province.code,city.code]
  }

  //提交修改
  const saveInfo = () => {
    loading.value = true
    userInfo.value.userArea = areaCodeToText(userInfo.value.userArea)
    newPutRequest('/user/updateInfo', userInfo.value)
      .then(response => {
        setTimeout(() => {
          if(response.code === 200){
            userInfo.value = response.data
            userInfo.value.userArea = areaTextToCode(userInfo.value.userArea)
            loading.value = false
            ElMessage({
              message: '修改成功(❁´◡`❁)',
              type: 'success',
            })
          }
        }, 2000)
    })
  }

  const setDialog = (dialog) => {
    emitter.emit('setDialog', dialog)
  }

  onMounted(() => {
    getUserInfo()
  })
</script>

<style scoped>

  .user-info-form{
    padding: 20px;
  }

  .item-bind-phone > :deep(.el-form-item__content),
  .item-bind-email > :deep(.el-form-item__content){
    display: flex;
    justify-content: space-between
  }
</style>
