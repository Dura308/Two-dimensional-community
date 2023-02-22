<template>

    <div class="personal-content-picture" style="padding: 0 45px">
      <el-row>
        <el-col>
          <div class="content-main">
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
              <ul>
                <li v-for="(item, index) in imageList">
                  <el-image
                    style="width: 500px; height: 500px"
                    :src="item"
                    :zoom-rate="1.2"
                    :preview-src-list="imageList"
                    :initial-index="index"
                    fit="cover"
                  />
                </li>
              </ul>
            </div>
            <div class="content-btn-wrap">
              <div class="bottom-item">
                <el-link :underline="false">
                  <i>
                    <svg class="icon"
                         id="collect"
                         :style="{color: isCollect ? '#38B6FF' : '#767676'}"
                         aria-hidden="true"
                         @click="userOperation('collect')">
                      <use xlink:href="#icon-collect"></use>
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
          </div>
        </el-col>
      </el-row>
    </div>

</template>

<script lang = "ts" setup>

  import {computed, defineProps, onMounted, PropType, reactive, ref, watch} from "vue";
  import {UserFilled, ChatLineRound, Star, Share} from "@element-plus/icons-vue"
  import VueStar from '@/components/dist-dianzan/dianzan'
  import "@/icon.js"

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

  onMounted(() => {
    for (let i = 0; i < content.value.length; i++) {
      imageList.value.push(content.value[i].url)
    }
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

  .content-btn-wrap{
    display: flex;
    margin-bottom: 15px;
    justify-content: space-around
  }

  .content-btn-wrap .bottom-item{
    display: flex;
    align-items: center;
  }

  .content-btn-wrap .bottom-item span{
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
