<template>
  <el-dialog
    v-model="bindEmailDialogVisible"
    title=""
    center
    :before-close="beforeClose"
    width="600px">
    <el-steps :active="stepsActive" align-center>
      <el-step title="验证身份" />
      <el-step title="绑定邮箱" />
      <el-step title="绑定成功" />
    </el-steps>
    <div class="item-verify-phone">
      <div class="item-phoneHidden">
        <el-input v-model="phoneHidden"
                  size="large"
                  style="padding: 10px"
                  disabled>
          <template #prepend>原手机号</template>
        </el-input>
        <div class="item-vfc">
          <el-input class="item-input-vfc"
                    v-model="vfc"
                    placeholder="请输入验证码"
                    size="large"/>

          <div class="item-btn-vfc">
            <el-button v-if="showCountDown === false"
                       size="large"
                       @click="getVfc">
              获取验证码
            </el-button>
            <el-button v-else
                       size="large"
                       disabled>
              重新获取({{countDownValue}}s)
            </el-button>
          </div>
        </div>

      </div>
    </div>
    <template #footer>
      <span class="dialog-footer">

        <el-button type="primary"
                   size="large"
                   style="width: 200px"
                   @click="next">
          下一步
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
  import {computed, onMounted, onUnmounted, reactive, ref} from 'vue'
  import emitter from "@/utils/bus";
  import {useStore} from "vuex";

  const store = useStore()
  const bindEmailDialogVisible = ref(false)
  const stepsActive = ref(1)
  const showCountDown = ref(false)
  const countDownValue = ref()
  const vfc = ref()
  const phone = computed(() => {
    return store.state.userInfo.phone
  })

  let phoneHidden = computed(() => {
    let p = String(phone.value)
    return p.substring(0, 3) + '*****' + p.substring(8, p.length)
  })



  const beforeClose = (done) => {
    stepsActive.value = 1
    done()
  }

  const next = () => {
    stepsActive.value++
  }

  /** 验证码倒计时 */
  const countdown = () => {
    showCountDown.value = true
    countDownValue.value = 60
    const start = setInterval(() => {
      if(countDownValue.value > 1){
        countDownValue.value--
      }else {
        showCountDown.value = false
        clearInterval(start)
      }
    }, 1000)
  }

  const getVfc = () => {
    countdown()
  }

  onMounted(() => {
    emitter.on('setDialog', (dialog) => {
      if(dialog === 'bindEmail'){
        bindEmailDialogVisible.value = true
      }
    })
  })

  onUnmounted(() => {
    emitter.off('setDialog')
  })

</script>
<style scoped>
  .dialog-footer button:first-child {
    margin-right: 10px;
  }

  .input-extra{
    position: absolute;
    right: 0;
    top: 0;
  }

  .item-verify-phone{
    display: flex;
    justify-content: center;
    margin-top: 20px
  }

  .item-phoneHidden{
    display: flex;
    flex-direction: column;
    width: 400px
  }

  .item-vfc{
    display: flex;
    align-items: center;
    padding: 10px
  }

  .item-input-vfc{
    padding-right: 10px
  }

</style>
