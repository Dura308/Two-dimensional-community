 <template>
  <el-main class = "tofu-main">
    <div class = "mainBox">
      <el-carousel class = "tofu-carousel">
        <el-carousel-item v-for = "item in 4" :key = "item">
          <h3 class = "small justify-center" text = "2xl">{{ item }}</h3>
        </el-carousel-item>
      </el-carousel>

      <div class="tofu-square" v-infinite-scroll="load">
        <el-row :gutter="12">
          <el-col :span="8">
            <div id="square-1">
              <template v-for="index in list.length">
                <SquareCard v-if="(index - 1) % 3 === 0" :height = "randomHeight()" :list = "list[index - 1]"/>
              </template>
            </div>
          </el-col>

          <el-col :span="8">
            <div id="square-2">
              <template v-for="index in list.length" >
                <SquareCard v-if="(index - 1) % 3 === 1" :height = "randomHeight()" :list = "list[index - 1]"/>
              </template>
            </div>
          </el-col>

          <el-col :span="8">
            <div id="square-3">
              <template v-for="index in list.length">
                <SquareCard v-if="(index - 1) % 3 === 2" :height = "randomHeight()" :list = "list[index - 1]"/>
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
  const contentInfo = reactive({
    avatar: '',
    nickName: 'nick-name',
  })

  const randomHeight = () => {
    return String(Math.random() * (600 - 100) + 100) + 'px'
  }

  const list = ref([])
  const count = ref(16)
  const randomList = () => {
    let i = list.value.length
    for (i; i < count.value; i++) {
      const cardInfo = {
        contentId: i + 1,
        id: i,
        nickName: 'tofu用户' + i,
        share: Math.floor(Math.random() * 9999),
        message: Math.floor(Math.random() * 9999),
        like: Math.floor(Math.random() * 9999),
      }
      list.value.push(cardInfo)
    }
    count.value += 16
  }

  const load = () => {
    setTimeout(() => {
      randomList()
    }, 2000)
  }

  onMounted(() => {
    randomList()
    console.log(list.value)
  })

</script>

<style scoped>
  .tofu-main {
    background-color: #F5F5FA;
    width: 100%;
    height: calc(100vh - 64px);
    margin: auto;
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
