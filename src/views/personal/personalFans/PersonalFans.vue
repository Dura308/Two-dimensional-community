<template>
  <div class="item-header">
    <span>全部粉丝</span>
  </div>
  <div class="item-fans-list">
    <ul class="list-content">
      <li class="list-item" v-for="(item, index) in fansList">
        <div class="all-fans">
          <div class="item-fans-card">
            <div class="card-avatar">

              <el-popover
                placement="right-end"
                :width="336"
                :offset="10"
                :popper-style="popperStyle"
                @before-enter="emitter.emit('getCardInfo' + index + 'personalFans')"
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
                           :uniqueId="index + 'personalFans'"/>
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
      v-model:current-page="fansPagination.currentPage"
      v-model:page-size="fansPagination.pageSize"
      :background="true"
      :pager-count="5"
      layout="prev, pager, next, jumper, total"
      :total="fansPagination.total"
      :hide-on-single-page="true"
      @current-change="getFansList"
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

  const fansPagination = reactive({
    currentPage: 1,
    pageSize: 6,
    total: '',
  })

  const fansList = ref()

  const handleCurrentChange = () => {
    console.log('handleCurrentChange' + fansPagination.currentPage)
  }

  const getFansList = () => {
    newGetRequest('/user/getAllFans', {
      userId: route.query.userId,
      pageNum: fansPagination.currentPage
    }).then(response => {
      if(response.code === 200){
        fansList.value = response.data.fansList
        fansPagination.total = response.data.total
        console.log(fansList.value)
      }
    })
  }

  const popperStyle = {
    borderRadius: '8px',
    padding: '0'
  }

  onMounted(() => {
    getFansList()
  })

</script>

<style scoped>


  .all-fans{
    display: flex;
    align-items: center;
    justify-content: space-between
  }

  .item-fans-card{
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

  .item-fans-list > .list-content{
    list-style-type: none;
    padding: 0 20px 20px;
    margin: 0;
  }

  .list-content > .list-item{
    padding: 20px 0;
    border-bottom: 1px solid #eee;
  }
</style>
