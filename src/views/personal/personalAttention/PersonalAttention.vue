<template>
  <div class="item-header">
    <span>全部关注</span>
  </div>
  <div class="item-attention-list">
    <ul class="list-content">
      <li class="list-item" v-for="(item, index) in attentionList">
        <div class="all-attention">
          <div class="item-attention-card">
            <div class="card-avatar">

              <el-popover
                placement="right-end"
                :width="336"
                :offset="10"
                :popper-style="popperStyle"
                @before-enter="emitter.emit('getCardInfo' + index + 'personalAttention')"
                :show-arrow="false"
                trigger="hover">
              <template #reference>
                <el-link type="primary"
                         :underline="false">
                  <el-avatar :size="60"
                             :src="item.avatar"/>
                </el-link>
              </template>
              <user-card :userId="route.query.userId"
                         :cardUserId="item.userId"
                         :uniqueId="index + 'personalAttention'"/>
              </el-popover>

            </div>
            <div class="card-content">
              <div class="content-nickName">
                <span>{{ item.nickName }}</span>
              </div>
              <div class="content-intro">
                <span v-if="item.userIntro === null">这位施主还没有写个签呢~~~</span>
                <span>{{ item.userIntro }}</span>
              </div>
            </div>
          </div>
          <div class="card-btn-wrap">
            <el-button plain>私信</el-button>
          </div>
        </div>
      </li>
    </ul>
  </div>
  <div class="item-pagination" style="display: flex;align-items: center;justify-content: center;margin-top: 30px">
    <el-pagination
      v-model:current-page="attentionPagination.currentPage"
      v-model:page-size="attentionPagination.pageSize"
      :background="true"
      :pager-count="5"
      layout="prev, pager, next, jumper, total"
      :total="attentionPagination.total"
      :hide-on-single-page="true"
      @current-change="getAttentionList"
    />
  </div>

</template>

<script lang = "ts" setup>

  import {onMounted, reactive, ref} from "vue";
  import {newGetRequest} from "@/utils/api";
  import {useRoute} from "vue-router";
  import UserCard from '@/views/home/main/UserCard'
  import emitter from "@/utils/bus";

  const route = useRoute()

  const attentionPagination = reactive({
    currentPage: 1,
    pageSize: 6,
    total: '',
  })

  const attentionList = ref()

  const handleCurrentChange = () => {
    console.log('handleCurrentChange' + attentionPagination.currentPage)
  }

  const getAttentionList = () => {
    newGetRequest('/user/getAllAttention', {
      userId: route.query.userId,
      pageNum: attentionPagination.currentPage
    }).then(response => {
      if(response.code === 200){
        attentionList.value = response.data.attentionList
        attentionPagination.total = response.data.total
        console.log(attentionList.value)
      }
    })
  }

  const popperStyle = {
    borderRadius: '8px',
    padding: '0'
  }

  onMounted(() => {
    getAttentionList()
  })

</script>

<style scoped>


  .all-attention{
    display: flex;
    align-items: center;
    justify-content: space-between
  }

  .item-attention-card{
    display: flex;
  }

  .card-avatar{
    display: flex;
    align-items: center;
    cursor: pointer;
  }

  .card-content{
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    padding-left: 30px
  }

  .content-nickName > span{
    font-size: 16px;
  }

  .content-intro > span{
    font-size: 14px;
    color: #6d757a;
  }

  .card-btn-wrap{
    display: flex;
    align-items: center;
  }

  .item-header{
    border-bottom: 1px solid #eee;
    padding-bottom: 20px
  }

  .item-header > span{
    font-size: 18px;
  }

  .item-attention-list > .list-content{
    list-style-type: none;
    padding: 0 20px 20px;
    margin: 0;
  }

  .list-content > .list-item{
    padding: 20px 0;
    border-bottom: 1px solid #eee;
  }
</style>
