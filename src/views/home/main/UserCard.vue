<template>
  <div>
    <div class="card-bg">

    </div>
    <div class="card-content">
      <div class="card-user-avatar">
        <el-avatar :src="userInfo.avatar"/>
      </div>
      <div class="card-user-nickName">
        <span>{{ userInfo.nickName }}</span>
      </div>
      <div class="card-social-info">
        <span>{{ userInfo.attentionNum }}关注</span>
        <span>{{ userInfo.fansNum }}粉丝</span>
        <span>{{ userInfo.allLikesNum }}获赞</span>
      </div>
      <div class="card-user-intro">
        <span>{{ userInfo.userIntro }}</span>
      </div>
      <div class="card-btn-wrap">
        <el-button v-if="userInfo.isAttention === false" type="primary" @click="attention">关注</el-button>
        <el-button v-else class="btn-cancel-attention" type="primary" @click="cancelAttention" color="#F1F2F3"></el-button>
        <el-button plain>私信</el-button>
      </div>
    </div>
  </div>
</template>

<script lang = "ts" setup>

  import {defineProps, onMounted, onUnmounted, ref} from "vue";
  import {newGetRequest, newPostRequest} from "../../../utils/api";
  import {useStore} from "vuex";
  import {ElMessage} from "element-plus";
  import cardBg from '@/assets/img-card-bg.png'
  import emitter from "@/utils/bus";
  import axios from "axios";
  import url from "../../../index";

  const store = useStore()
  const props = defineProps({
    userId: '',
    cardUserId: '',
    uniqueId: ''
  })

  onMounted(() => {
    emitter.on('getCardInfo' + props.uniqueId, () => {
      getUserCardInfo()
    })
  })

  onUnmounted(() => {
    emitter.off('getCardInfo')
    emitter.off('attention')
    emitter.off('cancelAttention')
  })

  const userInfo = ref({
    userIntro: '',
    nickName: '',
    userGender: '',
    avatar: '',
    userId: '',
    attentionNum: '',
    fansNum: '',
    allLikesNum: '',
    isAttention: ''
  })

  const getUserCardInfo = () => {
    return new Promise((resolve, reject) => {
      console.log('获取卡片信息')
      newGetRequest('/content/getCardInfo',{
        userId: props.userId,
        cardUserId: props.cardUserId
      }).then(response => {
        userInfo.value = response.data
        console.log('getCardInfo1')
        resolve(true)
      }).catch(err => {
        reject(err)
      })
    })
  }

  //关注
  const attention = () => {
    let fansUserId = String(props.userId)
    let attentionUserId = String(props.cardUserId)
    if(fansUserId === attentionUserId){
      ElMessage({
        message: '小主不能关注自己哦`(*>﹏<*)′',
        grouping: true,
        type: 'warning',
      })
      return
    }
    let formData = new FormData()
    formData.append('fansUserId', fansUserId)
    formData.append('attentionUserId', attentionUserId)
    newPostRequest('/user/attention',formData).then(response => {
      if (response.code === 200){
        userInfo.value.isAttention = true
        ElMessage({
          message: '关注成功(*^_^*)',
          type: 'success',
        })
      }
    })
  }

  //取消关注
  const cancelAttention = () => {
    let fansUserId = String(props.userId)
    let attentionUserId = String(props.cardUserId)
    let formData = new FormData()
    formData.append('fansUserId', fansUserId)
    formData.append('attentionUserId', attentionUserId)
    newPostRequest('/user/cancelAttention',formData).then(response => {
      if (response.code === 200){
        userInfo.value.isAttention = false
        ElMessage({
          message: '取消关注成功Σ(っ °Д °;)っ',
          type: 'success',
        })
      }
    })
  }

</script>

<style scoped>
  .card-bg{
    width: 100%;
    height: 65px;
    background-color: pink;
    border-radius: 8px 8px 0 0;
  }

  .card-content{
    padding: 10px 10px 10px 65px;
    display: flex;
    flex-direction: column;
  }

  .card-content > .card-user-avatar{
    position: absolute;
    left: 12px;
    display: flex
  }

  .card-content > .card-user-nickName, .card-social-info{
    margin-bottom: 8px
  }

  .card-user-nickName > span{
    font-size: 18px;
    font-weight: bolder
  }

  .card-social-info > span{
    padding-right: 15px;
    font-size: 12px;
  }

  .card-user-intro > span{
    font-size: 12px;
  }

  .card-content > .card-user-intro{
    margin-bottom: 12px
  }

  .card-btn-wrap > .el-button{
    width: 96px;
  }

  .btn-cancel-attention::before{
    content: "已关注";
    color: #606266;
  }

  .btn-cancel-attention:hover::before{
    content: "取消关注";
    color: #606266;
  }
</style>
