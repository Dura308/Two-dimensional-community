 <template>
  <el-main class = "tofu-main">
    <el-tooltip
      class="box-item"
      effect="light"
      content="回到顶部"
      placement="top"
      show-after="500"
    >
      <el-backtop target=".tofu-main" :right="100" :bottom="100" />
    </el-tooltip>
    <div class = "mainBox">
      <el-carousel class = "tofu-carousel">
        <el-carousel-item v-for = "item in carouselImg" :key = "item">
          <el-image :src="item" style="display:contents;"></el-image>
          <h3 class = "small justify-center" text = "2xl">{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>

      <div class="tofu-square" v-infinite-scroll="getContent">
        <el-row :gutter="12">
          <el-col :span="8">
            <div id="square-1">
              <template v-for="index in list.length">
                <SquareCard v-if="(index - 1) % 3 === 0" :list = "list[index - 1]"/>
              </template>
            </div>
          </el-col>

          <el-col :span="8">
            <div id="square-2">
              <template v-for="index in list.length" >
                <SquareCard v-if="(index - 1) % 3 === 1" :list = "list[index - 1]"/>
              </template>
            </div>
          </el-col>

          <el-col :span="8">
            <div id="square-3">
              <template v-for="index in list.length">
                <SquareCard v-if="(index - 1) % 3 === 2" :list = "list[index - 1]"/>
              </template>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>
  </el-main>
</template>

<script lang = "ts" setup>
  import {UserFilled} from "@element-plus/icons-vue"
  import SquareCard from "@/views/home/main/SquareCard"
  import {computed, onMounted, reactive, ref} from "vue";
  import axios from "axios";
  import url from "../../../index";
  import {ElMessage} from "element-plus";
  import {useStore} from "vuex";
  import {newGetRequest} from "../../../utils/api";


  const store = useStore()

  //轮播图片
  const carouselImg = [
    'https://p3-bcy-sign.bcyimg.com/banciyuan/0829b31842744c978f965f138f5a75e1~tplv-banciyuan-sq90.image?x-expires=1691480999&x-signature=D2P0kBFCvMhlgI%2FBCyOCsT4SBBk%3D',
    'https://p3-bcy-sign.bcyimg.com/banciyuan/c5751b7f4cbd49a69e5255345c4655fc~tplv-banciyuan-sq90.image?x-expires=1691480999&x-signature=0NH2Ampew4G4L8XfddZMAdGJPg0%3D',
    'https://p3-bcy-sign.bcyimg.com/banciyuan/a520aead11ed4ee4a63e59ffc03e10bc~tplv-banciyuan-sq90.image?x-expires=1691480999&x-signature=NXlO2n6vDDBOu0xmV2EjR6maq8A%3D',
    'https://p3-bcy-sign.bcyimg.com/banciyuan/02037359e119499483d2ed16d255dab8~tplv-banciyuan-sq90.image?x-expires=1691480999&x-signature=%2B5cXGFfGt2VBbYZHj%2FJitQHxBiQ%3D'
  ]

  const list = ref([])
  const pageNum = ref(1)

  const getContent = () => {
    console.log(pageNum.value)
    newGetRequest('/content/getContent', {
      pageNum: pageNum.value,
      userId: store.state.loginInfo.userId
    }).then(response => {
      if(response.code === -1){
        ElMessage({
          message: response.data,
          grouping: true,
          type: 'error',
        })
        return;
      }
      pageNum.value++
      for (let item of response.data) {
        list.value.push(item)
      }
    })
  }

</script>

<style scoped>
  .tofu-main {
    background-color: #F5F5FA;
    width: 100%;
    height: calc(100vh - 64px);
    margin: auto;
    overflow-x: hidden;
  }

  .mainBox {
    margin: auto;
    width: 80%;
    min-width: 1200px;
  }

  .el-carousel {
    margin-bottom: 10px;
    height: 300px;
    border-radius: 8px;
  }

  .el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
    text-align: center;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
  }

  .el-card{
    border-radius: 8px;
    margin-bottom: 10px;
    --el-card-padding: 0px 10px 0 10px;
  }

  .user-info{
    display: flex;
    align-items: center;
  }

  .user-content{
    background-color: #D3DCE6;
    margin-bottom: 10px;
  }

  .nick-name{
    margin-left: 10px;
  }
</style>
