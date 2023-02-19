<template>
  <div class="cropper" :style="{height: '400px', display: cropperDisplay}">
    <VueCropper
      ref="croppers"
      :img="imageUrl"
      :outputSize="option.outputSize"
      :outputType="option.outputType"
      :info="option.info"
      :canScale="option.canScale"
      :autoCrop="option.autoCrop"
      :autoCropWidth="option.autoCropWidth"
      :autoCropHeight="option.autoCropHeight"
      :fixed="option.fixed"
      :fixedNumber="option.fixedNumber"
      :full="option.full"
      :fixedBox="option.fixedBox"
      :canMove="option.canMove"
      :canMoveBox="option.canMoveBox"
      :original="option.original"
      :centerBox="option.centerBox"
      :height="option.height"
      :infoTrue="option.infoTrue"
      :maxImgSize="option.maxImgSize"
      :enlarge="option.enlarge"
      :mode="option.mode"
      @realTime="realTime"
      @imgLoad="imgLoad">
    </VueCropper>
  </div>
  <div class="item-save-avatar" v-show="cropperDisplay === 'block'">
    <el-button type="primary" @click="save">保存</el-button>
  </div>
</template>

<script setup>
  import {onMounted, onUnmounted, ref} from 'vue'
	import {useStore} from "vuex"
	import 'vue-cropper/dist/index.css'
	import { VueCropper }  from "vue-cropper"
	import {ElMessage} from "element-plus"
  import emitter from "@/utils/bus"
  import { uuid } from 'vue-uuid'
  import {newPutRequest} from "@/utils/api";

	const store = useStore()

	const option = ref({
		img: '',             //裁剪图片的地址
		outputSize: 1,       //裁剪生成图片的质量(可选0.1 - 1)
		outputType: 'jpeg',  //裁剪生成图片的格式（jpeg || png || webp）
		info: true,          //图片大小信息
		canScale: true,      //图片是否允许滚轮缩放
		autoCrop: true,      //是否默认生成截图框
		autoCropWidth: 3000,  //默认生成截图框宽度
		autoCropHeight: 3000, //默认生成截图框高度
		fixed: true,         //是否开启截图框宽高固定比例
		fixedNumber: [1, 1], //截图框的宽高比例
		full: false,         //false按原比例裁切图片，不失真
		fixedBox: false,      //固定截图框大小，不允许改变
		canMove: true,      //上传图片是否可以移动
		canMoveBox: true,    //截图框能否拖动
		original: false,     //上传图片按照原始比例渲染
		centerBox: true,    //截图框是否被限制在图片里面
		height: true,        //是否按照设备的dpr 输出等比例图片
		infoTrue: false,     //true为展示真实输出图片宽高，false展示看到的截图框宽高
		maxImgSize: 3000,    //限制图片最大宽度和高度
		enlarge: 1,          //图片根据截图框输出比例倍数
		mode: '100%'  //图片默认渲染方式
	})

  const croppers = ref()
	const imageUrl = ref()
	const cropperDisplay = ref('none')


	// const previews = ref()
	const realTime = (data) => {
    emitter.emit('realTime', data)
	}

	const imgLoad = (msg) => {
		console.log("工具初始化函数=====" + msg)
	}

  const save = () => {
    croppers.value.getCropBlob(async (data) => {
      let formData = new FormData()
      let userId = store.state.loginInfo.userId;
      formData.append('userId', userId)
      const avatarFile = await new File([data], userId + '.png', { type: 'image/jpeg' })
      formData.append('avatarFile', avatarFile)
      newPutRequest('/user/updateAvatar', formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }).then(response => {
        if(response.code === 200){
          cropperDisplay.value = 'none'
          console.log(response.data)
          emitter.emit('updateAvatar', response.data)
          ElMessage({
            message: '更新成功(❁´◡`❁)',
            type: 'success',
          })
        }
        setTimeout(() => {
          location.reload()
        }, 1500)
      })
    })
  }

  onMounted(() => {
    emitter.on('cropper', (imageBlob) => {
      cropperDisplay.value = 'block'
      imageUrl.value = URL.createObjectURL(imageBlob)
    })
  })

  onUnmounted(() => {
    emitter.off('cropper')
  })

</script>

<style scoped>
	.dialog-footer button{
		margin-right: 10px;
	}

  .item-save-avatar{
    padding: 20px;
    display: flex;
    justify-content: center;
  }
</style>
