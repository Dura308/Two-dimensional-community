<template>
  <div class="el-card-bottom">
    <div class="bottom-item">
      <el-link :underline="false">
        <i>
          <svg class="icon"
               id="collect"
               :style="{color: isCollect ? '#38B6FF' : '#767676'}"
               aria-hidden="true"
               @click="userOperation('collect')">
            <use xlink:href="#icon-shoucang"></use>
          </svg>
        </i>
      </el-link>
      <span>{{isCollect ? '已收藏' : '收藏'}}</span>
    </div>
    <div class="bottom-item">
      <el-popover
        :width="-1"
        style="background-color: #F6F7F8"
        placement="top-start"
        trigger="hover">
        <template #reference>
          <el-link :underline="false">
            <el-icon size="20" class="no-inherit">
              <Share />
            </el-icon>
          </el-link>
        </template>
        <template #default>
          <div class="el-popover-share">
            <div class="share-item" @click="share('qq')">
              <i>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-qq"></use>
                </svg>
              </i>
              <span>QQ</span>
            </div>
            <div class="share-item" @click="share('weixin')">
              <i>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-weixin"></use>
                </svg>
              </i>
              <span>微信</span>
            </div>
            <div class="share-item" @click="share('weibo')">
              <i>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-weibo"></use>
                </svg>
              </i>
              <span>微博</span>
            </div>
            <div class="share-item" @click="share('qq-zone')">
              <i>
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-qq-zone"></use>
                </svg>
              </i>
              <span>QQ空间</span>
            </div>
          </div>
        </template>
      </el-popover>
      <span>{{ contentInfo.shareNum }}</span>
    </div>
    <div class="bottom-item">
      <el-link :underline="false" @click="expandComments">
        <el-icon size="20" class="no-inherit">
          <ChatLineRound />
        </el-icon>
        <span>{{commentsVisible ? '收起评论' : contentInfo.commentNum }}</span>
      </el-link>
    </div>
    <div class="bottom-item" @click="userOperation('like')">
      <vue-star :isLike="isLike"/>
      <span>{{ contentInfo.likeNum }}</span>
    </div>
  </div>
  <el-collapse-transition>
    <div class="comment-part" v-if="commentsVisible">
      <div class="comment-form">
        <el-avatar :src="avatarUrl" ></el-avatar>
        <el-input class="w-50 m-2 input-comment" v-model="rootReplyMsg" placeholder="发表一条友善的评论..."></el-input>
        <el-button size="large" @click="replySubmit(true)">发布</el-button>
      </div>
      <div class="comment-list">
        <ul class="root-reply" v-for="(item, index) in comments">
          <li class="root-reply-info">
            <div class="root-user-info">
              <div class="root-user-avatar">
                <el-avatar :src="item.avatar"></el-avatar>
              </div>
              <div class="root-user-nickName">
                {{item.nickName}}
              </div>
            </div>
            <span style="display: flex">{{item.comment}}</span>
            <div class="reply-bottom">
              <span class="reply-time">{{item.createdTime}}</span>
              <span class="reply-like">赞</span>
              <span class="reply-dislike">踩</span>
              <span class="reply-btn" @click="reply(item)">回复</span>
            </div>
          </li>
          <ul class="sub-reply">
            <li class="sub-reply-info" v-for="(child, childIndex) in item.child">
              <div class="sub-user-info">
                <div class="sub-user-avatar">
                  <el-avatar :src="child.avatar" size="small"></el-avatar>
                </div>
                <div class="sub-user-nickName">
                  {{child.nickName}}
                </div>
                &emsp;回复
                <el-link :underline="false" type="primary">
                  @{{child.parentNickName}}
                </el-link>
              </div>
              <span>{{' : ' + child.comment}}</span>
              <div class="reply-bottom">
                <span class="reply-time">{{child.createdTime}}</span>
                <span class="reply-like">赞</span>
                <span class="reply-dislike">踩</span>
                <span class="reply-btn"  @click="reply(child)">回复</span>
              </div>
            </li>
          </ul>
        </ul>
      </div>
      <div class="reply-form" v-if="replyFormVisible">
        <el-avatar :src="avatarUrl" ></el-avatar>
        <el-input class="w-50 m-2 input-comment" :placeholder="'回复 @' + replyTo.nickName + ' : '" v-model="replyMsg"></el-input>
        <el-button size="large" @click="replySubmit(false)">发布</el-button>
      </div>
    </div>
  </el-collapse-transition>
</template>

<script lang = "ts" setup>

  import {computed, defineProps, onBeforeMount, onMounted, PropType, ref} from "vue";
  import {newGetRequest, newPostRequest} from "@/utils/api";
  import {ElNotification} from "element-plus";
  import {collectOperation, likeOperation} from "@/components/contentActions/Actions";
  import {useStore} from "vuex";
  import VueStar from '@/components/dist-dianzan/dianzan'
  import {ChatLineRound, Share} from "@element-plus/icons-vue"
  import "@/icon.js"


  interface params{
    isCollect?: boolean,
    isLike?: boolean,
    contentInfo?: {},
  }

  const props = defineProps({
    params: {
      type: Array as PropType<params>
    }
  })

  const store = useStore()
  const loginInfo = computed(() => {
    return store.state.loginInfo
  })

  const isLike = ref()
  const isCollect = ref()
  const contentInfo = computed(() => {
    return props.params.contentInfo
  })

  //---------------展开评论---------------
  const commentsVisible = ref(false)
  const expandComments = () => {
    commentsVisible.value = !commentsVisible.value
    if(commentsVisible.value){
      getComments(contentInfo.value.contentId)
      return
    }
    replyFormVisible.value = false
  }

  //判断是否登录
  const isLogin = () => {
    return localStorage.getItem('token') === null
  }

  const userOperation = (mode) => {
    console.log(isLogin()+'1')
    if (isLogin()) {
      store.commit('changeLoginDialogVisible')
      return
    }
    console.log(isLogin()+'2')
    if (mode === 'collect'){
      collectAction()
    }else if (mode === 'like'){
      likeAction()
    }
  }

  const share = (mode) => {
    ElNotification({
      title: '分享成功',
      message: '通过' + mode + '分享',
      duration: 0,
    })
  }

  const collectAction = () => {
    let formData = new FormData()
    formData.append('userId', loginInfo.value.userId)
    formData.append('contentId', contentInfo.value.contentId)
    collectOperation(formData).then(response => {
      if(response === true){
        isCollect.value = !isCollect.value
      }
    })
  }

  const likeAction = () => {
    let formData = new FormData()
    formData.append('userId', loginInfo.value.userId)
    formData.append('contentId', contentInfo.value.contentId)
    likeOperation(formData).then(response => {
      if(response === true){
        isLike.value = !isLike.value
        isLike.value ? contentInfo.value.likeNum++ : contentInfo.value.likeNum--
      }
    })
  }

  //-----头像地址--------
  const avatarUrl = computed(() => {
    return store.state.loginInfo.avatar
  })

  const comments = ref()
  const getComments = (contentId) => {
    console.log('获取内容' + contentId + '评论中')
    newGetRequest('/home/getComments', {
      contentId: contentId
    }).then(response => {
      comments.value = response.data
    })
  }

  const replyFormVisible = ref(false)
  const replyTo = ref()
  const replyMsg = ref()
  const rootReplyMsg = ref()
  const reply = (replyInfo) => {
    replyFormVisible.value = true
    if(replyFormVisible){
      replyTo.value = replyInfo
    }
  }

  const replySubmit = (rootReply) => {
    if (isLogin()) {
      store.commit('changeLoginDialogVisible')
      return
    }
    newPostRequest('/home/reply', {
      commentId: null,
      userId: loginInfo.value.userId,
      nickName: loginInfo.value.nickName,
      avatar: loginInfo.value.avatar,
      parentNickName: rootReply ? null : replyTo.value.nickName,
      contentId: contentInfo.value.contentId,
      comment: rootReply ? rootReplyMsg.value : replyMsg.value,
      parentId: rootReply ? null : replyTo.value.commentId,
      rootParentId: rootReply ? null : (replyTo.value.rootParentId === null ? replyTo.value.commentId : replyTo.value.rootParentId),
      createdTime: null,
      child: null
    }).then(response => {
      if(response.code === 200){
        contentInfo.value.commentNum++
        getComments(contentInfo.value.contentId)
        replyFormVisible.value = false
        replyMsg.value = ''
        rootReplyMsg.value = ''
      }
    })
  }

  onBeforeMount(() => {
    isLike.value = props.params.isLike
    isCollect.value = props.params.isCollect
  })

  onMounted(() => {
    // console.log(localStorage.getItem('token'))
  })

</script>

<style scoped>
  .el-card-bottom{
    display: flex;
    margin-bottom: 15px;
    justify-content: space-around
  }

  .el-card-bottom .bottom-item{
    display: flex;
    align-items: center;
  }

  .el-card-bottom .bottom-item span{
    margin-left: 6px;
  }

  .el-popover .el-popover-share{
    display: flex;
  }

  .icon {
    width: 4em; height: 2em;
    vertical-align: -0.15em;
    fill: currentColor;
    overflow: hidden;
  }

  #collect{
    width: 20px;
    height: 20px;
  }

  .share-item{
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
  }

  .comment-form,
  .reply-form{
    display: flex;
    justify-content: space-between;
  }

  ul{
    list-style-type: none;
  }

  .root-reply{
    padding-left: 0;
  }

  .root-reply-info{
    position: relative;
    padding:14px 0 0 50px;
    line-height: 35px;
  }

  .root-user-info{
    display: inline-flex;
    align-items: center;
  }

  .root-user-nickName{
    font-size: 14px;
  }

  .root-user-nickName, .sub-user-nickName{
    cursor: pointer;
    color: #61666d;;
  }

  .root-user-avatar{
    width: 40px;
    height: 40px;
    position: absolute;
    cursor: pointer;
    left: 0;
    top: 12px;
  }

  .sub-reply{
    padding-left: 50px;
    padding-right: 20px;
  }

  .sub-reply-info{
    position: relative;
    padding: 8px 0 8px 30px;
    line-height: 28px;
    font-size: 14px;
  }

  .sub-user-info{
    display: inline-flex;
    align-items: center;
  }

  .sub-user-avatar{
    width: 24px;
    height: 24px;
    position: absolute;
    cursor: pointer;
    left: 0;
  }

  .reply-bottom{
    display: flex;
    align-items: center;
    font-size: 13px;
    color: #9499A0;
  }

  .reply-time{
    margin-right: 10px;
  }
  .reply-like,
  .reply-dislike,
  .reply-btn{
    margin-right: 10px;
    cursor: pointer;
  }

  .reply-like:hover,
  .reply-dislike:hover,
  .reply-btn:hover{
    color: cornflowerblue;
  }

  .input-comment{
    width: 73%;
  }

  .comment-part{
    margin-bottom: 15px;
  }
</style>
